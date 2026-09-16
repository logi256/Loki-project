package com.example.smartbikepass.ui.screens

import android.widget.Toast
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartbikepass.data.model.ApplicationEntity
import com.example.smartbikepass.ui.components.StatusBadge
import com.example.smartbikepass.ui.theme.BackgroundLight
import com.example.smartbikepass.ui.theme.BorderLight
import com.example.smartbikepass.ui.theme.CrimsonRed
import com.example.smartbikepass.ui.theme.DeepNavy
import com.example.smartbikepass.ui.theme.EmeraldGreen
import com.example.smartbikepass.ui.theme.SkyBlue
import com.example.smartbikepass.ui.theme.TextPrimary
import com.example.smartbikepass.ui.theme.TextSecondary
import com.example.smartbikepass.viewmodel.BikePassViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransportScreen(
    viewModel: BikePassViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val applications by viewModel.transportApplications.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) }
    var reviewingApp by remember { mutableStateOf<ApplicationEntity?>(null) }
    var remarksText by remember { mutableStateOf("") }

    val pendingList = applications.filter { it.status == "pending" }
    val rejectedList = applications.filter { it.status == "transport_rejected" }

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
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.White,
                contentColor = DeepNavy
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Text(
                            "Pending Review (${pendingList.size})",
                            fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Text(
                            "Rejected (${rejectedList.size})",
                            fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
            }

            val currentList = if (selectedTab == 0) pendingList else rejectedList

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
                            text = if (selectedTab == 0) "No pending applications to review" else "No rejected applications",
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
                                remarksText = app.transportRemarks ?: ""
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
                        shape = RoundedCornerShape(8.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val pid = currentApp.passId
                        viewModel.reviewTransport(
                            passId = pid,
                            action = "verify",
                            remarks = remarksText
                        ) {
                            Toast.makeText(context, "Application verified & forwarded to Principal", Toast.LENGTH_SHORT).show()
                            reviewingApp = null
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen)
                ) {
                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Verify & Forward")
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
                                remarks = remarksText.ifBlank { "Rejected during transport review" }
                            ) {
                                Toast.makeText(context, "Application rejected", Toast.LENGTH_SHORT).show()
                                reviewingApp = null
                            }
                        },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = CrimsonRed)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Reject")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { reviewingApp = null },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
                    ) {
                        Text("Close", color = Color.Black)
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
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = app.passId,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp,
                    color = DeepNavy
                )
                StatusBadge(status = app.status)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = app.fullName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = TextPrimary
                    )
                    Text(
                        text = "${app.rollNo} • ${app.department}",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = app.vehicleNo,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 13.sp,
                        color = SkyBlue
                    )
                    Text(
                        text = app.vehicleType,
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }
            }

            if (!app.transportRemarks.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Remarks: ${app.transportRemarks}",
                    fontSize = 11.sp,
                    color = CrimsonRed
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = BorderLight)
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Submitted: ${app.submittedAt.take(10)}",
                    fontSize = 10.sp,
                    color = TextSecondary
                )
                Text(
                    text = "Review Application →",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = SkyBlue
                )
            }
        }
    }
}
