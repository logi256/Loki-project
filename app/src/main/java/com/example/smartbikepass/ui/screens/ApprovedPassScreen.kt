package com.example.smartbikepass.ui.screens

import android.content.Intent
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.ElectricBike
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartbikepass.data.model.ApplicationEntity
import com.example.smartbikepass.ui.components.QrCodeView
import com.example.smartbikepass.ui.theme.BackgroundLight
import com.example.smartbikepass.ui.theme.BorderLight
import com.example.smartbikepass.ui.theme.CyanAccent
import com.example.smartbikepass.ui.theme.DeepNavy
import com.example.smartbikepass.ui.theme.ElectricCyan
import com.example.smartbikepass.ui.theme.EmeraldGreen
import com.example.smartbikepass.ui.theme.EmeraldLight
import com.example.smartbikepass.ui.theme.GradientNavyEnd
import com.example.smartbikepass.ui.theme.GradientNavyStart
import com.example.smartbikepass.ui.theme.SkyBlue
import com.example.smartbikepass.ui.theme.SkyBlueLight
import com.example.smartbikepass.ui.theme.TextPrimary
import com.example.smartbikepass.ui.theme.TextSecondary
import com.example.smartbikepass.viewmodel.BikePassViewModel
import com.example.smartbikepass.viewmodel.StatusCheckState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApprovedPassScreen(
    passId: String,
    viewModel: BikePassViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val statusState by viewModel.statusCheckState.collectAsState()

    LaunchedEffect(passId) {
        viewModel.checkStatus(passId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Digital Bike Pass",
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
                actions = {
                    val app = (statusState as? StatusCheckState.Found)?.application
                    if (app != null) {
                        IconButton(
                            onClick = {
                                val sendIntent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(
                                        Intent.EXTRA_TEXT,
                                        "SMART BIKE PASS\nPass ID: ${app.passId}\nStudent: ${app.fullName} (${app.rollNo})\nVehicle No: ${app.vehicleNo}\nStatus: APPROVED"
                                    )
                                    type = "text/plain"
                                }
                                context.startActivity(Intent.createChooser(sendIntent, "Share Bike Pass"))
                            }
                        ) {
                            Icon(Icons.Default.Share, contentDescription = "Share", tint = Color.White)
                        }
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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

                is StatusCheckState.Found -> {
                    DigitalPassCard(app = state.application)
                }

                else -> {
                    Text("Could not load bike pass details for $passId", color = TextSecondary)
                }
            }
        }
    }
}

@Composable
fun DigitalPassCard(
    app: ApplicationEntity,
    modifier: Modifier = Modifier
) {
    // Pulse animation for verified badge
    val infiniteTransition = rememberInfiniteTransition(label = "pass_anim")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "verified_pulse"
    )

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(2.dp, EmeraldGreen.copy(alpha = 0.7f)),
        shape = RoundedCornerShape(22.dp),
        modifier = modifier
            .fillMaxWidth()
            .shadow(12.dp, RoundedCornerShape(22.dp), ambientColor = EmeraldGreen.copy(alpha = 0.15f))
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Banner with Gradient and Hologram Verified Badge
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            listOf(GradientNavyStart, Color(0xFF064E3B), EmeraldGreen)
                        )
                    )
                    .padding(vertical = 16.dp, horizontal = 18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.2f))
                                .border(1.5.dp, Color.White.copy(alpha = 0.5f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.ElectricBike,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                "SMART BIKE PASS",
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 15.sp,
                                letterSpacing = 1.2.sp
                            )
                            Text(
                                "Authorized Vehicle Permit",
                                color = CyanAccent.copy(alpha = 0.9f),
                                fontSize = 11.sp
                            )
                        }
                    }

                    // Pulsing Approved Pill
                    Box(
                        modifier = Modifier
                            .scale(pulseScale)
                            .clip(RoundedCornerShape(14.dp))
                            .background(EmeraldGreen)
                            .border(1.5.dp, Color.White.copy(alpha = 0.8f), RoundedCornerShape(14.dp))
                            .padding(horizontal = 12.dp, vertical = 5.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Verified,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                "APPROVED",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 0.8.sp
                            )
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Pass ID Banner
                Text("OFFICIAL DIGITAL PASS ID", fontSize = 10.sp, color = TextSecondary, fontWeight = FontWeight.Bold, letterSpacing = 0.8.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = app.passId,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = DeepNavy,
                    letterSpacing = 1.5.sp
                )

                Spacer(modifier = Modifier.height(18.dp))

                // QR Code with glowing frame
                val qrData = "${app.passId}|${app.vehicleNo}|${app.fullName}"
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(18.dp))
                        .background(Color(0xFFF8FAFC))
                        .border(2.dp, SkyBlue.copy(alpha = 0.25f), RoundedCornerShape(18.dp))
                        .padding(14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    QrCodeView(data = qrData, size = 180.dp)
                }

                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Present at Main Gate Scanner",
                    fontSize = 12.sp,
                    color = TextSecondary,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(18.dp))
                HorizontalDivider(color = BorderLight)
                Spacer(modifier = Modifier.height(16.dp))

                // Authorized Vehicle Plate Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            Brush.verticalGradient(
                                listOf(Color(0xFFF1F5F9), Color(0xFFE2E8F0))
                            )
                        )
                        .border(1.5.dp, BorderLight, RoundedCornerShape(12.dp))
                        .padding(14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("AUTHORISED VEHICLE NUMBER", fontSize = 10.sp, color = TextSecondary, fontWeight = FontWeight.Bold, letterSpacing = 0.8.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = app.vehicleNo,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = DeepNavy,
                            letterSpacing = 1.5.sp
                        )
                        Text(
                            text = app.vehicleType,
                            fontSize = 12.sp,
                            color = SkyBlue,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Student details table
                PassRow("Student Name", app.fullName)
                PassRow("Roll Number", app.rollNo)
                PassRow("Department", app.department)
                PassRow("Year of Study", app.year)
                PassRow("Issued Date", app.principalReviewedAt ?: app.submittedAt)
                if (!app.principalRemarks.isNullOrBlank()) {
                    PassRow("Principal Sign", app.principalRemarks)
                }

                Spacer(modifier = Modifier.height(18.dp))
                HorizontalDivider(color = BorderLight)
                Spacer(modifier = Modifier.height(14.dp))

                // Security validity footer with shield
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.Security,
                        contentDescription = null,
                        tint = EmeraldGreen,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Valid for Current Academic Year • Digital Signature Verified",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = EmeraldGreen
                    )
                }
            }
        }
    }
}

@Composable
fun PassRow(label: String, value: String) {
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
            fontWeight = FontWeight.Bold,
            color = DeepNavy
        )
    }
}
