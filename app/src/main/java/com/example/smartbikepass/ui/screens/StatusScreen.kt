package com.example.smartbikepass.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.HourglassEmpty
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartbikepass.data.model.ApplicationEntity
import com.example.smartbikepass.ui.components.StatusBadge
import com.example.smartbikepass.ui.theme.AmberLight
import com.example.smartbikepass.ui.theme.AmberWarning
import com.example.smartbikepass.ui.theme.AppInputTextStyle
import com.example.smartbikepass.ui.theme.BackgroundLight
import com.example.smartbikepass.ui.theme.BorderLight
import com.example.smartbikepass.ui.theme.CrimsonLight
import com.example.smartbikepass.ui.theme.CrimsonRed
import com.example.smartbikepass.ui.theme.CyanAccent
import com.example.smartbikepass.ui.theme.DeepNavy
import com.example.smartbikepass.ui.theme.ElectricCyan
import com.example.smartbikepass.ui.theme.EmeraldGreen
import com.example.smartbikepass.ui.theme.EmeraldLight
import com.example.smartbikepass.ui.theme.GradientNavyStart
import com.example.smartbikepass.ui.theme.SkyBlue
import com.example.smartbikepass.ui.theme.SkyBlueLight
import com.example.smartbikepass.ui.theme.TextPrimary
import com.example.smartbikepass.ui.theme.TextSecondary
import com.example.smartbikepass.ui.theme.appOutlinedTextFieldColors
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
                        "Track Pass Status",
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
                colors = TopAppBarDefaults.topAppBarColors(containerColor = GradientNavyStart)
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
                border = BorderStroke(1.dp, BorderLight),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(16.dp), ambientColor = DeepNavy.copy(alpha = 0.06f))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Enter Your Pass ID",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeepNavy
                    )
                    Text(
                        text = "Track your verification progress in real-time",
                        fontSize = 11.sp,
                        color = TextSecondary,
                        modifier = Modifier.padding(top = 2.dp, bottom = 10.dp)
                    )

                    OutlinedTextField(
                        value = passIdInput,
                        onValueChange = { passIdInput = it.uppercase() },
                        placeholder = { Text("e.g. SBPS-D5C279BA") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = SkyBlue) },
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
                        colors = appOutlinedTextFieldColors(),
                        textStyle = AppInputTextStyle,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("status_pass_id_input"),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = { viewModel.checkStatus(passIdInput) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("check_status_search_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DeepNavy,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Search, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Track Application Status", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                    }

                    // Recent Searches Pills
                    if (recentPasses.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "RECENT TRACKED PASSES",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextSecondary,
                            letterSpacing = 0.8.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            recentPasses.take(5).forEach { pid ->
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(if (passIdInput == pid) SkyBlueLight else Color(0xFFF1F5F9))
                                        .border(
                                            1.dp,
                                            if (passIdInput == pid) SkyBlue else BorderLight,
                                            RoundedCornerShape(12.dp)
                                        )
                                        .clickable {
                                            passIdInput = pid
                                            viewModel.checkStatus(pid)
                                        }
                                        .padding(horizontal = 12.dp, vertical = 6.dp)
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

            Spacer(modifier = Modifier.height(18.dp))

            // Result presentation
            when (val state = statusState) {
                is StatusCheckState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = SkyBlue)
                    }
                }

                is StatusCheckState.NotFound -> {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, Color(0xFFFCA5A5)),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Default.Info,
                                contentDescription = null,
                                tint = Color(0xFFEF4444),
                                modifier = Modifier.size(40.dp)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "Application Not Found",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = DeepNavy
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "No bike pass application was found for '${state.passId}'. Please check your ID format or submit a new application.",
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
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, BorderLight),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Text(
                                text = "💡 Status Tracking Guide",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = DeepNavy
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "• Pass IDs start with SBPS- (e.g. SBPS-D5C279BA)\n• Applications require Transport review followed by Principal sign-off\n• Once approved, you can view and download your QR Pass anytime.",
                                fontSize = 12.sp,
                                color = TextSecondary,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }

                is StatusCheckState.Error -> {
                    Text("Error: ${state.message}", color = CrimsonRed, fontSize = 13.sp)
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
    val isApproved = application.status.lowercase() == "approved"

    // Infinite transition for approved pulse
    val infiniteTransition = rememberInfiniteTransition(label = "status_anim")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.98f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "approved_pulse"
    )

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, if (isApproved) EmeraldGreen.copy(alpha = 0.5f) else BorderLight),
        shape = RoundedCornerShape(18.dp),
        modifier = modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(18.dp), ambientColor = if (isApproved) EmeraldGreen.copy(alpha = 0.15f) else DeepNavy.copy(alpha = 0.08f))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            // Header: Pass ID + Status Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("APPLICATION PASS ID", fontSize = 10.sp, color = TextSecondary, fontWeight = FontWeight.Bold, letterSpacing = 0.8.sp)
                    Text(
                        text = application.passId,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp,
                        color = DeepNavy
                    )
                }
                StatusBadge(status = application.status)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ==================== VISUAL APPROVAL TIMELINE TRACKER ====================
            TimelineTracker(application = application)

            Spacer(modifier = Modifier.height(16.dp))
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
            if (isApproved) {
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = onViewApprovedPass,
                    modifier = Modifier
                        .fillMaxWidth()
                        .scale(pulseScale)
                        .height(52.dp)
                        .shadow(6.dp, RoundedCornerShape(12.dp))
                        .testTag("view_digital_pass_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EmeraldGreen,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        Icons.Default.QrCode,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        "View Digital Bike Pass & QR",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun TimelineTracker(application: ApplicationEntity) {
    val status = application.status.lowercase()

    val step1Done = true // Submitted is always done
    val step2Done = status == "transport_verified" || status == "approved"
    val step2Failed = status == "transport_rejected" || (status == "rejected" && application.transportRemarks != null && application.principalReviewedAt == null)
    val step3Done = status == "approved"
    val step3Failed = status == "principal_rejected" || (status == "rejected" && application.principalReviewedAt != null)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFF1F5F9))
            .border(1.dp, BorderLight, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "VERIFICATION PIPELINE",
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                color = DeepNavy,
                letterSpacing = 1.sp
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color.White)
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    text = when {
                        step3Done -> "COMPLETED"
                        step2Failed || step3Failed -> "ACTION REQUIRED"
                        else -> "IN PROGRESS"
                    },
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = when {
                        step3Done -> EmeraldGreen
                        step2Failed || step3Failed -> CrimsonRed
                        else -> ElectricCyan
                    }
                )
            }
        }
        Spacer(modifier = Modifier.height(14.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TimelineNode(
                title = "Submitted",
                state = TimelineNodeState.COMPLETED
            )
            TimelineLine(isDone = step2Done || step2Failed)
            TimelineNode(
                title = "Transport Desk",
                state = when {
                    step2Done -> TimelineNodeState.COMPLETED
                    step2Failed -> TimelineNodeState.FAILED
                    status == "submitted" || status == "pending" -> TimelineNodeState.IN_PROGRESS
                    else -> TimelineNodeState.PENDING
                }
            )
            TimelineLine(isDone = step3Done || step3Failed)
            TimelineNode(
                title = "Principal OK",
                state = when {
                    step3Done -> TimelineNodeState.COMPLETED
                    step3Failed -> TimelineNodeState.FAILED
                    status == "transport_verified" -> TimelineNodeState.IN_PROGRESS
                    else -> TimelineNodeState.PENDING
                }
            )
        }
    }
}

private enum class TimelineNodeState {
    COMPLETED, IN_PROGRESS, FAILED, PENDING
}

@Composable
private fun TimelineNode(title: String, state: TimelineNodeState) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(
                    when (state) {
                        TimelineNodeState.COMPLETED -> EmeraldGreen
                        TimelineNodeState.IN_PROGRESS -> AmberWarning
                        TimelineNodeState.FAILED -> CrimsonRed
                        TimelineNodeState.PENDING -> Color(0xFFE2E8F0)
                    }
                )
                .border(
                    2.dp,
                    when (state) {
                        TimelineNodeState.COMPLETED -> EmeraldGreen.copy(alpha = 0.5f)
                        TimelineNodeState.IN_PROGRESS -> AmberWarning.copy(alpha = 0.5f)
                        TimelineNodeState.FAILED -> CrimsonRed.copy(alpha = 0.5f)
                        TimelineNodeState.PENDING -> Color(0xFFCBD5E1)
                    },
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            when (state) {
                TimelineNodeState.COMPLETED -> Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                TimelineNodeState.IN_PROGRESS -> Icon(Icons.Default.HourglassEmpty, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                TimelineNodeState.FAILED -> Icon(Icons.Default.Close, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                TimelineNodeState.PENDING -> Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFF94A3B8)))
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = title,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = when (state) {
                TimelineNodeState.COMPLETED -> EmeraldGreen
                TimelineNodeState.IN_PROGRESS -> AmberWarning
                TimelineNodeState.FAILED -> CrimsonRed
                TimelineNodeState.PENDING -> TextSecondary
            }
        )
    }
}

@Composable
private fun TimelineLine(isDone: Boolean) {
    Box(
        modifier = Modifier
            .width(44.dp)
            .height(3.dp)
            .clip(RoundedCornerShape(2.dp))
            .background(if (isDone) EmeraldGreen else Color(0xFFCBD5E1))
    )
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
