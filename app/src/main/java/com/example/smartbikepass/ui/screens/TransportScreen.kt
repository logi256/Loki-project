package com.example.smartbikepass.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartbikepass.data.model.ApplicationEntity
import com.example.smartbikepass.ui.components.StatusBadge
import com.example.smartbikepass.ui.theme.AmberWarning
import com.example.smartbikepass.ui.theme.BackgroundLight
import com.example.smartbikepass.ui.theme.BorderLight
import com.example.smartbikepass.ui.theme.CrimsonRed
import com.example.smartbikepass.ui.theme.DeepNavy
import com.example.smartbikepass.ui.theme.ElectricCyan
import com.example.smartbikepass.ui.theme.EmeraldGreen
import com.example.smartbikepass.ui.theme.NavyLight
import com.example.smartbikepass.ui.theme.SkyBlue
import com.example.smartbikepass.ui.theme.SkyBlueLight
import com.example.smartbikepass.ui.theme.TextPrimary
import com.example.smartbikepass.ui.theme.TextSecondary
import com.example.smartbikepass.ui.theme.appOutlinedTextFieldColors
import com.example.smartbikepass.ui.theme.AppInputTextStyle
import com.example.smartbikepass.viewmodel.BikePassViewModel

import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.VerifiedUser

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransportScreen(
    viewModel: BikePassViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToPrincipal: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val applications by viewModel.transportApplications.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) }
    var reviewingApp by remember { mutableStateOf<ApplicationEntity?>(null) }
    var remarksText by remember { mutableStateOf("") }

    val pendingList = applications.filter { it.status.equals("pending", ignoreCase = true) }
    val verifiedList = applications.filter {
        it.status.equals("transport_verified", ignoreCase = true) ||
        it.status.equals("approved", ignoreCase = true)
    }
    val rejectedList = applications.filter { it.status.equals("transport_rejected", ignoreCase = true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "Transport In-Charge Portal",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            "Initial Document Verification",
                            fontSize = 11.sp,
                            color = SkyBlue
                        )
                    }
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
                actions = {
                    IconButton(onClick = {
                        viewModel.logout()
                        onNavigateBack()
                    }) {
                        Icon(
                            Icons.Default.ExitToApp,
                            contentDescription = "Logout",
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
        ) {
            // Quick Official Header & Portal Switcher Banner
            Surface(
                color = Color.White,
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(SkyBlueLight),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.VerifiedUser,
                                    contentDescription = null,
                                    tint = SkyBlue,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    "TRANSPORT VERIFICATION DESK",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = DeepNavy,
                                    letterSpacing = 0.5.sp
                                )
                                Text(
                                    "RC Book, License & Safety Audit",
                                    fontSize = 10.sp,
                                    color = TextSecondary
                                )
                            }
                        }

                        if (onNavigateToPrincipal != null) {
                            Button(
                                onClick = onNavigateToPrincipal,
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFFBEB), contentColor = AmberWarning),
                                border = BorderStroke(1.dp, AmberWarning.copy(alpha = 0.5f))
                            ) {
                                Icon(Icons.Default.DirectionsBike, contentDescription = null, tint = AmberWarning, modifier = Modifier.size(13.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Principal Desk →", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Quick Stats Chips Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Pending Chip
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (selectedTab == 0) SkyBlueLight else Color(0xFFF8FAFC))
                                .border(1.dp, if (selectedTab == 0) SkyBlue else BorderLight, RoundedCornerShape(10.dp))
                                .clickable { selectedTab = 0 }
                                .padding(vertical = 8.dp, horizontal = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("${pendingList.size}", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = if (selectedTab == 0) SkyBlue else DeepNavy)
                                Text("Pending", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = TextSecondary)
                            }
                        }

                        // Verified Chip
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (selectedTab == 1) EmeraldGreen.copy(alpha = 0.1f) else Color(0xFFF8FAFC))
                                .border(1.dp, if (selectedTab == 1) EmeraldGreen else BorderLight, RoundedCornerShape(10.dp))
                                .clickable { selectedTab = 1 }
                                .padding(vertical = 8.dp, horizontal = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("${verifiedList.size}", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = if (selectedTab == 1) EmeraldGreen else DeepNavy)
                                Text("Verified", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = TextSecondary)
                            }
                        }

                        // Rejected Chip
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (selectedTab == 2) CrimsonRed.copy(alpha = 0.1f) else Color(0xFFF8FAFC))
                                .border(1.dp, if (selectedTab == 2) CrimsonRed else BorderLight, RoundedCornerShape(10.dp))
                                .clickable { selectedTab = 2 }
                                .padding(vertical = 8.dp, horizontal = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("${rejectedList.size}", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = if (selectedTab == 2) CrimsonRed else DeepNavy)
                                Text("Rejected", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = TextSecondary)
                            }
                        }
                    }
                }
            }

            val currentList = when (selectedTab) {
                0 -> pendingList
                1 -> verifiedList
                else -> rejectedList
            }

            if (currentList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.AssignmentTurnedIn,
                            contentDescription = null,
                            tint = TextSecondary,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = when (selectedTab) {
                                0 -> "No pending applications to review"
                                1 -> "No verified applications yet"
                                else -> "No rejected applications"
                            },
                            fontSize = 14.sp,
                            color = TextSecondary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(currentList, key = { it.passId }) { app ->
                        TransportAppCard(
                            app = app,
                            onReviewClick = {
                                reviewingApp = app
                                remarksText = app.transportRemarks ?: "Documents & RC verified with originals"
                            }
                        )
                    }
                }
            }
        }
    }

    // Review Dialog
    val currentApp = reviewingApp
    if (currentApp != null) {
        AlertDialog(
            onDismissRequest = { reviewingApp = null },
            title = {
                Text(
                    text = "Review Application: ${currentApp.passId}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    DetailRow("Applicant", currentApp.fullName)
                    DetailRow("Roll Number", currentApp.rollNo)
                    DetailRow("Department", currentApp.department)
                    DetailRow("Year", currentApp.year)
                    DetailRow("Vehicle No", currentApp.vehicleNo, isBold = true)
                    DetailRow("Vehicle Type", currentApp.vehicleType)
                    DetailRow("Submitted At", currentApp.submittedAt)

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = BorderLight)
                    Spacer(modifier = Modifier.height(10.dp))

                    Text("Attached Documents:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("• RC Book: ${currentApp.rcBook ?: "Attached"}", fontSize = 11.sp, color = TextSecondary)
                    Text("• Driving License: ${currentApp.license ?: "Attached"}", fontSize = 11.sp, color = TextSecondary)
                    Text("• Insurance: ${currentApp.insurance ?: "Attached"}", fontSize = 11.sp, color = TextSecondary)

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = remarksText,
                        onValueChange = { remarksText = it },
                        label = { Text("Transport Remarks") },
                        placeholder = { Text("e.g. Documents verified with original") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 3,
                        colors = appOutlinedTextFieldColors(),
                        textStyle = AppInputTextStyle,
                        shape = RoundedCornerShape(8.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val pid = currentApp.passId
                        val remarks = remarksText.ifBlank { "Documents & RC verified with originals" }
                        viewModel.reviewTransport(
                            passId = pid,
                            action = "verify",
                            remarks = remarks
                        ) {
                            Toast.makeText(context, "Application verified & forwarded to Principal!", Toast.LENGTH_SHORT).show()
                            reviewingApp = null
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EmeraldGreen,
                        contentColor = Color.White
                    )
                ) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Verify & Forward", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                Row {
                    OutlinedButton(
                        onClick = {
                            val pid = currentApp.passId
                            viewModel.reviewTransport(
                                passId = pid,
                                action = "reject",
                                remarks = remarksText.ifBlank { "Rejected during transport document verification" }
                            ) {
                                Toast.makeText(context, "Application rejected", Toast.LENGTH_SHORT).show()
                                reviewingApp = null
                            }
                        },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = CrimsonRed),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CrimsonRed)
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = null,
                            tint = CrimsonRed,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Reject", color = CrimsonRed, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { reviewingApp = null },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFE2E8F0),
                            contentColor = DeepNavy
                        )
                    ) {
                        Text("Close", color = DeepNavy, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        )
    }
}

@Composable
fun TransportAppCard(
    app: ApplicationEntity,
    onReviewClick: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onReviewClick() },
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(SkyBlueLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.DirectionsBike,
                            contentDescription = null,
                            tint = SkyBlue,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = app.passId,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 15.sp,
                        color = DeepNavy
                    )
                }
                StatusBadge(status = app.status)
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = app.fullName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${app.rollNo} • ${app.department} (${app.year})",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = app.vehicleNo,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 14.sp,
                        color = SkyBlue
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = app.vehicleType,
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }
            }

            if (!app.transportRemarks.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFF1F5F9))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "Transport Note: ${app.transportRemarks}",
                        fontSize = 11.sp,
                        color = DeepNavy,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = BorderLight)
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Submitted: ${app.submittedAt.take(10)}",
                    fontSize = 11.sp,
                    color = TextSecondary
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Verify & Audit",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = SkyBlue
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        Icons.Default.ArrowForward,
                        contentDescription = null,
                        tint = SkyBlue,
                        modifier = Modifier.size(13.dp)
                    )
                }
            }
        }
    }
}
