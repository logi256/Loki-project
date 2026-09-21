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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.VerifiedUser
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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.runtime.mutableIntStateOf
import com.example.smartbikepass.viewmodel.BikePassViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrincipalScreen(
    viewModel: BikePassViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToTransport: (() -> Unit)? = null,
    onNavigateToApprovedPass: ((passId: String) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val applications by viewModel.principalApplications.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) }
    var reviewingApp by remember { mutableStateOf<ApplicationEntity?>(null) }
    var remarksText by remember { mutableStateOf("Bike Pass authorized and issued for campus parking") }

    val pendingApprovalList = applications.filter { it.status.equals("transport_verified", ignoreCase = true) }
    val approvedList = applications.filter { it.status.equals("approved", ignoreCase = true) }
    val rejectedList = applications.filter { it.status.equals("principal_rejected", ignoreCase = true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "Principal Approval Portal",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            "Final Pass Authorization & QR Issue",
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
                                    .background(AmberWarning.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.VerifiedUser,
                                    contentDescription = null,
                                    tint = AmberWarning,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    "PRINCIPAL'S APPROVAL DESK",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = DeepNavy,
                                    letterSpacing = 0.5.sp
                                )
                                Text(
                                    "Final Pass Authorization & QR Issue",
                                    fontSize = 10.sp,
                                    color = TextSecondary
                                )
                            }
                        }

                        if (onNavigateToTransport != null) {
                            Button(
                                onClick = onNavigateToTransport,
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF0F9FF), contentColor = SkyBlue),
                                border = BorderStroke(1.dp, SkyBlue.copy(alpha = 0.5f))
                            ) {
                                Icon(Icons.Default.DirectionsBike, contentDescription = null, tint = SkyBlue, modifier = Modifier.size(13.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Transport Desk →", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Quick Stats Chips Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Awaiting Sign-off Chip
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (selectedTab == 0) AmberWarning.copy(alpha = 0.15f) else Color(0xFFF8FAFC))
                                .border(1.dp, if (selectedTab == 0) AmberWarning else BorderLight, RoundedCornerShape(10.dp))
                                .clickable { selectedTab = 0 }
                                .padding(vertical = 8.dp, horizontal = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("${pendingApprovalList.size}", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = if (selectedTab == 0) Color(0xFFB45309) else DeepNavy)
                                Text("Awaiting Sign-off", fontSize = 9.sp, fontWeight = FontWeight.SemiBold, color = TextSecondary)
                            }
                        }

                        // Approved Chip
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
                                Text("${approvedList.size}", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = if (selectedTab == 1) EmeraldGreen else DeepNavy)
                                Text("Approved Passes", fontSize = 9.sp, fontWeight = FontWeight.SemiBold, color = TextSecondary)
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
                                Text("Rejected", fontSize = 9.sp, fontWeight = FontWeight.SemiBold, color = TextSecondary)
                            }
                        }
                    }
                }
            }

            val currentList = when (selectedTab) {
                0 -> pendingApprovalList
                1 -> approvedList
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
                            Icons.Default.VerifiedUser,
                            contentDescription = null,
                            tint = EmeraldGreen,
                            modifier = Modifier.size(52.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = when (selectedTab) {
                                0 -> "No Applications Awaiting Approval"
                                1 -> "No Passes Approved Yet"
                                else -> "No Rejected Applications"
                            },
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = DeepNavy
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = when (selectedTab) {
                                0 -> "Applications verified by Transport will appear here for final approval."
                                1 -> "Once approved by Principal, official QR passes appear here."
                                else -> "Rejected applications will appear here."
                            },
                            fontSize = 12.sp,
                            color = TextSecondary,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
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
                        ElevatedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    if (selectedTab == 0) {
                                        reviewingApp = app
                                        remarksText = "Bike Pass authorized and issued for campus parking"
                                    } else if (selectedTab == 1 && onNavigateToApprovedPass != null) {
                                        onNavigateToApprovedPass(app.passId)
                                    }
                                },
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
                                                .background(AmberWarning.copy(alpha = 0.15f)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                Icons.Default.VerifiedUser,
                                                contentDescription = null,
                                                tint = AmberWarning,
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
                                            text = "Transport Verification: ${app.transportRemarks}",
                                            fontSize = 11.sp,
                                            color = DeepNavy,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }

                                if (selectedTab == 1 && onNavigateToApprovedPass != null) {
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Button(
                                        onClick = { onNavigateToApprovedPass(app.passId) },
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = ButtonDefaults.buttonColors(containerColor = DeepNavy),
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Icon(Icons.Default.QrCode2, contentDescription = null, tint = ElectricCyan, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("View Digital Pass & Official QR", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                    }
                                } else if (selectedTab == 0) {
                                    Spacer(modifier = Modifier.height(12.dp))
                                    HorizontalDivider(color = BorderLight)
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Verified at: ${(app.transportReviewedAt ?: app.submittedAt).take(16)}",
                                            fontSize = 10.sp,
                                            color = TextSecondary
                                        )
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = "Authorize & Sign-Off",
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = EmeraldGreen
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Icon(
                                                Icons.Default.Check,
                                                contentDescription = null,
                                                tint = EmeraldGreen,
                                                modifier = Modifier.size(14.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Principal Review Dialog
    val currentApp = reviewingApp
    if (currentApp != null) {
        AlertDialog(
            onDismissRequest = { reviewingApp = null },
            title = {
                Text(
                    text = "Principal Approval: ${currentApp.passId}",
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
                    if (!currentApp.transportRemarks.isNullOrBlank()) {
                        DetailRow("Transport Remarks", currentApp.transportRemarks)
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = BorderLight)
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = remarksText,
                        onValueChange = { remarksText = it },
                        label = { Text("Principal Approval Remarks") },
                        placeholder = { Text("e.g. Bike Pass approved") },
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
                        val remarks = remarksText.ifBlank { "Bike Pass authorized and issued for campus parking" }
                        viewModel.reviewPrincipal(
                            passId = pid,
                            action = "approve",
                            remarks = remarks
                        ) {
                            Toast.makeText(context, "Pass officially approved! QR generated.", Toast.LENGTH_SHORT).show()
                            reviewingApp = null
                            selectedTab = 1
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
                    Text("Approve & Issue Pass", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                Row {
                    OutlinedButton(
                        onClick = {
                            val pid = currentApp.passId
                            viewModel.reviewPrincipal(
                                passId = pid,
                                action = "reject",
                                remarks = remarksText.ifBlank { "Rejected by Principal" }
                            ) {
                                Toast.makeText(context, "Application rejected", Toast.LENGTH_SHORT).show()
                                reviewingApp = null
                                selectedTab = 2
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
