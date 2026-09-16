package com.example.smartbikepass.data.repository

import com.example.smartbikepass.data.db.ApplicationDao
import com.example.smartbikepass.data.db.AuditLogDao
import com.example.smartbikepass.data.db.UserDao
import com.example.smartbikepass.data.model.ApplicationEntity
import com.example.smartbikepass.data.model.AuditLogEntity
import com.example.smartbikepass.data.model.UserEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

data class PassStats(
    val total: Int = 0,
    val pending: Int = 0,
    val transportVerified: Int = 0,
    val approved: Int = 0,
    val rejected: Int = 0
)

class BikePassRepository(
    private val applicationDao: ApplicationDao,
    private val userDao: UserDao,
    private val auditLogDao: AuditLogDao
) {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    fun getAllApplicationsFlow(): Flow<List<ApplicationEntity>> =
        applicationDao.getAllApplicationsFlow()

    fun getApplicationsByStatusesFlow(statuses: List<String>): Flow<List<ApplicationEntity>> =
        applicationDao.getApplicationsByStatusesFlow(statuses)

    suspend fun getApplicationByPassId(passId: String): ApplicationEntity? =
        applicationDao.getByPassId(passId.trim().uppercase(Locale.ROOT))

    fun getApplicationByPassIdFlow(passId: String): Flow<ApplicationEntity?> =
        applicationDao.getByPassIdFlow(passId.trim().uppercase(Locale.ROOT))

    fun getAllAuditLogsFlow(): Flow<List<AuditLogEntity>> =
        auditLogDao.getAllLogsFlow()

    fun getStatsFlow(): Flow<PassStats> {
        return applicationDao.getAllApplicationsFlow().combine(applicationDao.getAllApplicationsFlow()) { apps, _ ->
            PassStats(
                total = apps.size,
                pending = apps.count { it.status == "pending" },
                transportVerified = apps.count { it.status == "transport_verified" },
                approved = apps.count { it.status == "approved" },
                rejected = apps.count { it.status == "transport_rejected" || it.status == "principal_rejected" }
            )
        }
    }

    suspend fun submitApplication(
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
    ): Result<String> {
        return try {
            val passId = "SBPS-" + UUID.randomUUID().toString().replace("-", "").take(8).uppercase(Locale.ROOT)
            val currentTime = dateFormat.format(Date())

            val application = ApplicationEntity(
                passId = passId,
                fullName = fullName.trim(),
                rollNo = rollNo.trim(),
                email = email.trim(),
                phone = phone.trim(),
                department = department,
                year = year,
                vehicleNo = vehicleNo.trim().uppercase(Locale.ROOT),
                vehicleType = vehicleType,
                rcBook = rcBook ?: "rc_sample.png",
                license = license ?: "dl_sample.png",
                insurance = insurance ?: "ins_sample.png",
                status = "pending",
                submittedAt = currentTime
            )

            applicationDao.insert(application)

            auditLogDao.insert(
                AuditLogEntity(
                    passId = passId,
                    action = "Application Submitted",
                    doneBy = fullName.trim(),
                    remarks = null,
                    createdAt = currentTime
                )
            )

            Result.success(passId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun reviewTransport(passId: String, action: String, remarks: String?, reviewer: String): Result<String> {
        return try {
            val newStatus = if (action == "verify") "transport_verified" else "transport_rejected"
            val currentTime = dateFormat.format(Date())

            applicationDao.updateTransportReview(
                passId = passId,
                status = newStatus,
                remarks = remarks,
                reviewedAt = currentTime
            )

            auditLogDao.insert(
                AuditLogEntity(
                    passId = passId,
                    action = "Transport: $action",
                    doneBy = reviewer,
                    remarks = remarks,
                    createdAt = currentTime
                )
            )

            Result.success(newStatus)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun reviewPrincipal(passId: String, action: String, remarks: String?, reviewer: String): Result<String> {
        return try {
            val newStatus = if (action == "approve") "approved" else "principal_rejected"
            val currentTime = dateFormat.format(Date())

            applicationDao.updatePrincipalReview(
                passId = passId,
                status = newStatus,
                remarks = remarks,
                reviewedAt = currentTime
            )

            auditLogDao.insert(
                AuditLogEntity(
                    passId = passId,
                    action = "Principal: $action",
                    doneBy = reviewer,
                    remarks = remarks,
                    createdAt = currentTime
                )
            )

            Result.success(newStatus)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun authenticate(username: String, password: String): UserEntity? {
        return userDao.authenticate(username.trim(), password.trim())
    }
}
