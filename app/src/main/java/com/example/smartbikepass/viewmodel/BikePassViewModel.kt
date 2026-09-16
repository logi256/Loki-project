package com.example.smartbikepass.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.smartbikepass.data.model.ApplicationEntity
import com.example.smartbikepass.data.model.AuditLogEntity
import com.example.smartbikepass.data.model.UserEntity
import com.example.smartbikepass.data.repository.BikePassRepository
import com.example.smartbikepass.data.repository.PassStats
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface SubmitUiState {
    data object Idle : SubmitUiState
    data object Submitting : SubmitUiState
    data class Success(val passId: String) : SubmitUiState
    data class Error(val message: String) : SubmitUiState
}

sealed interface StatusCheckState {
    data object Idle : StatusCheckState
    data object Loading : StatusCheckState
    data class Found(val application: ApplicationEntity) : StatusCheckState
    data class NotFound(val passId: String) : StatusCheckState
    data class Error(val message: String) : StatusCheckState
}

class BikePassViewModel(
    private val repository: BikePassRepository
) : ViewModel() {

    // Auth State
    private val _currentUser = MutableStateFlow<UserEntity?>(null)
    val currentUser: StateFlow<UserEntity?> = _currentUser.asStateFlow()

    private val _loginError = MutableStateFlow<String?>(null)
    val loginError: StateFlow<String?> = _loginError.asStateFlow()

    // Application Submission State
    private val _submitState = MutableStateFlow<SubmitUiState>(SubmitUiState.Idle)
    val submitState: StateFlow<SubmitUiState> = _submitState.asStateFlow()

    // Status Checking State
    private val _statusCheckState = MutableStateFlow<StatusCheckState>(StatusCheckState.Idle)
    val statusCheckState: StateFlow<StatusCheckState> = _statusCheckState.asStateFlow()

    // Recent Pass IDs tracked on device for quick lookup
    private val _recentPassIds = MutableStateFlow<List<String>>(
        listOf("SBPS-D5C279BA", "SBPS-3B652BDD", "SBPS-7C88D2E1", "SBPS-9E21A4B7")
    )
    val recentPassIds: StateFlow<List<String>> = _recentPassIds.asStateFlow()

    // Data streams
    val allApplications: StateFlow<List<ApplicationEntity>> =
        repository.getAllApplicationsFlow()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val transportApplications: StateFlow<List<ApplicationEntity>> =
        repository.getApplicationsByStatusesFlow(listOf("pending", "transport_rejected"))
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val principalApplications: StateFlow<List<ApplicationEntity>> =
        repository.getApplicationsByStatusesFlow(listOf("transport_verified"))
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val auditLogs: StateFlow<List<AuditLogEntity>> =
        repository.getAllAuditLogsFlow()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val stats: StateFlow<PassStats> =
        repository.getStatsFlow()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PassStats())

    fun login(username: String, password: String, onSuccess: (role: String) -> Unit) {
        viewModelScope.launch {
            _loginError.value = null
            val user = repository.authenticate(username, password)
            if (user != null) {
                _currentUser.value = user
                onSuccess(user.role)
            } else {
                _loginError.value = "Invalid username or password"
            }
        }
    }

    fun logout() {
        _currentUser.value = null
        _loginError.value = null
    }

    fun submitApplication(
        fullName: String,
        rollNo: String,
        email: String,
        phone: String,
        department: String,
        year: String,
        vehicleNo: String,
        vehicleType: String,
        rcBook: String?,
        license: String?,
        insurance: String?
    ) {
        viewModelScope.launch {
            _submitState.value = SubmitUiState.Submitting
            val result = repository.submitApplication(
                fullName = fullName,
                rollNo = rollNo,
                email = email,
                phone = phone,
                department = department,
                year = year,
                vehicleNo = vehicleNo,
                vehicleType = vehicleType,
                rcBook = rcBook,
                license = license,
                insurance = insurance
            )

            result.fold(
                onSuccess = { passId ->
                    _submitState.value = SubmitUiState.Success(passId)
                    addRecentPassId(passId)
                },
                onFailure = { err ->
                    _submitState.value = SubmitUiState.Error(err.message ?: "Failed to submit application")
                }
            )
        }
    }

    fun resetSubmitState() {
        _submitState.value = SubmitUiState.Idle
    }

    fun checkStatus(passId: String) {
        val trimmed = passId.trim().uppercase()
        if (trimmed.isEmpty()) return

        viewModelScope.launch {
            _statusCheckState.value = StatusCheckState.Loading
            val app = repository.getApplicationByPassId(trimmed)
            if (app != null) {
                _statusCheckState.value = StatusCheckState.Found(app)
                addRecentPassId(trimmed)
            } else {
                _statusCheckState.value = StatusCheckState.NotFound(trimmed)
            }
        }
    }

    fun addRecentPassId(passId: String) {
        val current = _recentPassIds.value.toMutableList()
        if (!current.contains(passId)) {
            current.add(0, passId)
            _recentPassIds.value = current.take(10)
        }
    }

    fun reviewTransport(passId: String, action: String, remarks: String?, onComplete: () -> Unit) {
        viewModelScope.launch {
            val username = _currentUser.value?.username ?: "transport"
            repository.reviewTransport(passId, action, remarks, username)
            // If current status check is viewing this pass, refresh it
            val updated = repository.getApplicationByPassId(passId)
            if (updated != null && (_statusCheckState.value as? StatusCheckState.Found)?.application?.passId == passId) {
                _statusCheckState.value = StatusCheckState.Found(updated)
            }
            onComplete()
        }
    }

    fun reviewPrincipal(passId: String, action: String, remarks: String?, onComplete: () -> Unit) {
        viewModelScope.launch {
            val username = _currentUser.value?.username ?: "principal"
            repository.reviewPrincipal(passId, action, remarks, username)
            val updated = repository.getApplicationByPassId(passId)
            if (updated != null && (_statusCheckState.value as? StatusCheckState.Found)?.application?.passId == passId) {
                _statusCheckState.value = StatusCheckState.Found(updated)
            }
            onComplete()
        }
    }
}

class BikePassViewModelFactory(
    private val repository: BikePassRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BikePassViewModel::class.java)) {
            return BikePassViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
