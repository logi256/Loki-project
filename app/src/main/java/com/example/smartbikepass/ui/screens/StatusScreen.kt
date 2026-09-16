package com.example.smartbikepass.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartbikepass.data.model.ApplicationEntity
import com.example.smartbikepass.ui.components.StatusBadge
import com.example.smartbikepass.ui.theme.BackgroundLight
import com.example.smartbikepass.ui.theme.BorderLight
import com.example.smartbikepass.ui.theme.DeepNavy
import com.example.smartbikepass.ui.theme.EmeraldGreen
import com.example.smartbikepass.ui.theme.NavyLight
import com.example.smartbikepass.ui.theme.SkyBlue
import com.example.smartbikepass.ui.theme.TextPrimary
import com.example.smartbikepass.ui.theme.TextSecondary
import com.example.smartbikepass.viewmodel.BikePassViewModel
import com.example.smartbikepass.viewmodel.StatusCheckState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatusScreen(
    viewModel: BikePassViewModel,
    initialPassId: String?,
    onNavigateBack: () -> Unit,
    onNavigateToApprovedPass: (passId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var passIdInput by remember { mutableStateOf(initialPassId ?: "") }
    val statusState by viewModel.statusCheckState.collectAsState()
    val recentPasses by viewModel.recentPassIds.collectAsState()

    LaunchedEffect(initialPassId) {
        if (!initialPassId.isNullOrBlank()) {
            passIdInput = initialPassId
            viewModel.checkStatus(initialPassId)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Check Application Status",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DeepNavy)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(BackgroundLight)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Search Box Card
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Enter Your Pass ID",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeepNavy
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = passIdInput,
                        onValueChange = { passIdInput = it.uppercase() },
                        placeholder = { Text("e.g. SBPS-D5C279BA") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TextSecondary) },
                        trailingIcon = {
                            if (passIdInput.isNotEmpty()) {
                                IconButton(onClick = { passIdInput = "" }) {
                                    Icon(Icons.Default.Clear, contentDescription = "Clear", tint = TextSecondary)
                                }
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Characters,
                            imeAction = ImeAction.Search
                        ),
                        keyboardActions = KeyboardActions(
                            onSearch = { viewModel.checkStatus(passIdInput) }
                        ),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("status_pass_id_input"),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { viewModel.checkStatus(passIdInput) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("check_status_search_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = DeepNavy),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Search Status", fontWeight = FontWeight.Bold)
                    }

                    // Recent Pass Quick Chips
                    if (recentPasses.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Quick Select:", fontSize = 11.sp, color = TextSecondary)
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            recentPasses.take(4).forEach { pid ->
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(if (passIdInput == pid) SkyBlue.copy(alpha = 0.15f) else Color(0xFFF1F5F9))
                                        .border(
                                            1.dp,
                                            if (passIdInput == pid) SkyBlue else BorderLight,
                                            RoundedCornerShape(16.dp)
                                        )
                                        .clickable {
                                            passIdInput = pid
                                            viewModel.checkStatus(pid)
                                        }
                                        .padding(horizontal = 10.dp, vertical = 5.dp)
                                ) {
                                    Text(
                                        text = pid,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (passIdInput == pid) SkyBlue else DeepNavy
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Result presentation
            when (val state = statusState) {
                is StatusCheckState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = SkyBlue)
                    }
                }

                is StatusCheckState.NotFound -> {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFCA5A5)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Default.Info,
                                contentDescription = null,
                                tint = Color(0xFFEF4444),
                                modifier = Modifier.size(36.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Application Not Found",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = DeepNavy
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "No bike pass application was found for '${state.passId}'. Please verify the ID format (e.g. SBPS-D5C279BA) or submit a new application.",
                                fontSize = 12.sp,
                                color = TextSecondary,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }

                is StatusCheckState.Found -> {
                    ApplicationStatusCard(
                        application = state.application,
                        onViewApprovedPass = { onNavigateToApprovedPass(state.application.passId) }
                    )
                }

                is StatusCheckState.Idle -> {
                    // Help card
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "💡 Tracking Tips",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = DeepNavy
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "• Pass IDs look like SBPS-XXXXXXXX (e.g. SBPS-D5C279BA)\n• Applications require Transport review followed by Principal sign-off\n• Once approved, you can view and download your scannable QR Pass here anytime.",
                                fontSize = 12.sp,
                                color = TextSecondary,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }

                is StatusCheckState.Error -> {
                    Text("Error: ${state.message}", color = Color.Red, fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
fun ApplicationStatusCard(
    application: ApplicationEntity,
    onViewApprovedPass: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header: Pass ID + Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("PASS ID", fontSize = 10.sp, color = TextSecondary, fontWeight = FontWeight.Bold)
                    Text(
                        text = application.passId,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 17.sp,
                        color = DeepNavy
                    )
                }
                StatusBadge(status = application.status)
            }

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = BorderLight)
            Spacer(modifier = Modifier.height(14.dp))

            // Details grid
            DetailRow("Applicant Name", application.fullName)
            DetailRow("Roll Number", application.rollNo)
            DetailRow("Department", application.department)
            DetailRow("Year of Study", application.year)
            DetailRow("Vehicle Reg. No", application.vehicleNo, isBold = true)
            DetailRow("Vehicle Type", application.vehicleType)
            DetailRow("Submitted Date", application.submittedAt)

            // Review Details
            if (!application.transportRemarks.isNullOrBlank() || application.transportReviewedAt != null) {
                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(color = BorderLight)
                Spacer(modifier = Modifier.height(10.dp))
                Text("Transport Review Remarks:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
                Text(
                    text = application.transportRemarks?.ifBlank { "Verified without remarks" } ?: "Verified",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
                if (application.transportReviewedAt != null) {
                    Text("Reviewed at: ${application.transportReviewedAt}", fontSize = 10.sp, color = TextSecondary)
                }
            }

            if (!application.principalRemarks.isNullOrBlank() || application.principalReviewedAt != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text("Principal Remarks:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
                Text(
                    text = application.principalRemarks?.ifBlank { "Approved" } ?: "Approved",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
                if (application.principalReviewedAt != null) {
                    Text("Reviewed at: ${application.principalReviewedAt}", fontSize = 10.sp, color = TextSecondary)
                }
            }

            // If approved -> Show Digital Pass Button
            if (application.status.lowercase() == "approved") {
                Spacer(modifier = Modifier.height(18.dp))
                Button(
                    onClick = onViewApprovedPass,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("view_digital_pass_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.QrCode, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("📱 View Digital Bike Pass", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String, isBold: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 12.sp, color = TextSecondary)
        Text(
            text = value,
            fontSize = 12.sp,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Medium,
            color = DeepNavy
        )
    }
}
