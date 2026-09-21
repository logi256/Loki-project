package com.example.smartbikepass.ui.screens

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.ElectricBike
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartbikepass.R
import com.example.smartbikepass.ui.theme.AmberLight
import com.example.smartbikepass.ui.theme.AmberWarning
import com.example.smartbikepass.ui.theme.AppInputTextStyle
import com.example.smartbikepass.ui.theme.BackgroundLight
import com.example.smartbikepass.ui.theme.BorderLight
import com.example.smartbikepass.ui.theme.CardHighlight
import com.example.smartbikepass.ui.theme.CyanAccent
import com.example.smartbikepass.ui.theme.DeepNavy
import com.example.smartbikepass.ui.theme.ElectricCyan
import com.example.smartbikepass.ui.theme.EmeraldGreen
import com.example.smartbikepass.ui.theme.EmeraldLight
import com.example.smartbikepass.ui.theme.GradientNavyEnd
import com.example.smartbikepass.ui.theme.GradientNavyMid
import com.example.smartbikepass.ui.theme.GradientNavyStart
import com.example.smartbikepass.ui.theme.NavyLight
import com.example.smartbikepass.ui.theme.SkyBlue
import com.example.smartbikepass.ui.theme.SkyBlueLight
import com.example.smartbikepass.ui.theme.TextPrimary
import com.example.smartbikepass.ui.theme.TextSecondary
import com.example.smartbikepass.ui.theme.appOutlinedTextFieldColors
import com.example.smartbikepass.viewmodel.BikePassViewModel
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: BikePassViewModel,
    onStudentLoginSuccess: () -> Unit,
    onAdminLoginSuccess: (role: String) -> Unit,
    onNavigateBack: (() -> Unit)? = null,
    onExploreAsGuest: () -> Unit = onStudentLoginSuccess,
    initialTab: Int = 0,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(initialTab) }

    // Student fields: rollno and dob
    var studentRollNo by remember { mutableStateOf("") }
    var studentDob by remember { mutableStateOf("") }
    val studentLoginError by viewModel.studentLoginError.collectAsState()

    // Staff fields: userid, password (defaulting to transport desk for convenience)
    var adminUserId by remember { mutableStateOf("transport") }
    var adminPassword by remember { mutableStateOf("transport123") }
    var passwordVisible by remember { mutableStateOf(false) }
    val adminLoginError by viewModel.loginError.collectAsState()

    val context = LocalContext.current

    // Infinite transition for floating bike badge and pulsing gate indicator
    val infiniteTransition = rememberInfiniteTransition(label = "hero_anim")
    val floatAnim by infiniteTransition.animateFloat(
        initialValue = -5f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float_offset"
    )
    val pulseAnim by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.25f,
        animationSpec = infiniteRepeatable(
            animation = tween(1300, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(1300, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )

    // Date picker dialog for Student DOB
    val datePickerDialog = remember {
        val calendar = Calendar.getInstance()
        calendar.set(2004, Calendar.JANUARY, 1)
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val dayStr = String.format(Locale.getDefault(), "%02d", dayOfMonth)
                val monthStr = String.format(Locale.getDefault(), "%02d", month + 1)
                studentDob = "$dayStr/$monthStr/$year"
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.app_logo),
                            contentDescription = "Smart Campus Bike Pass Logo",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .border(1.dp, Color.White.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                "Smart Campus Pass",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                "University Mobility System",
                                fontSize = 11.sp,
                                color = CyanAccent.copy(alpha = 0.85f)
                            )
                        }
                    }
                },
                navigationIcon = {
                    if (onNavigateBack != null) {
                        IconButton(onClick = onNavigateBack) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = GradientNavyStart
                )
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
            // ==================== HERO ARTWORK & BRAND BANNER ====================
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(22.dp),
                border = BorderStroke(1.dp, BorderLight),
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp, RoundedCornerShape(22.dp), ambientColor = DeepNavy.copy(alpha = 0.1f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                ) {
                    // Campus Mobility Art Banner
                    Image(
                        painter = painterResource(id = R.drawable.img_college_campus),
                        contentDescription = "Campus Smart Bike Mobility",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Gradient overlay for readability and depth
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        GradientNavyStart.copy(alpha = 0.65f),
                                        GradientNavyStart.copy(alpha = 0.96f)
                                    )
                                )
                            )
                    )

                    // Animated Floating Badge & Gate Status
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomStart)
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Floating App Logo Badge
                            Box(
                                modifier = Modifier
                                    .offset(y = floatAnim.dp)
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .border(2.dp, Color.White.copy(alpha = 0.8f), RoundedCornerShape(14.dp))
                                    .shadow(6.dp, RoundedCornerShape(14.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.app_logo),
                                    contentDescription = "Smart Campus Pass Logo",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column {
                                Text(
                                    text = if (selectedTab == 0) "Student Portal" else "Administration",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White
                                )
                                Text(
                                    text = if (selectedTab == 0)
                                        "Instant pass status & verification"
                                    else
                                        "Approvals, security & gate dispatch",
                                    fontSize = 12.sp,
                                    color = Color.White.copy(alpha = 0.85f)
                                )
                            }
                        }

                        // Live Smart Gate Status Pill with pulsing dot
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color.Black.copy(alpha = 0.45f))
                                .border(1.dp, Color.White.copy(alpha = 0.25f), RoundedCornerShape(20.dp))
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .scale(pulseAnim)
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(EmeraldGreen.copy(alpha = pulseAlpha))
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "LIVE",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    letterSpacing = 1.sp
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // ==================== ANIMATED TAB CONTROLLER ====================
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, BorderLight),
                modifier = Modifier.fillMaxWidth()
            ) {
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color.White,
                    contentColor = DeepNavy,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = if (selectedTab == 0) ElectricCyan else DeepNavy,
                            height = 3.dp
                        )
                    },
                    divider = {}
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(vertical = 12.dp)
                            ) {
                                Icon(
                                    Icons.Default.School,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp),
                                    tint = if (selectedTab == 0) ElectricCyan else TextSecondary
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "Student",
                                    fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Medium,
                                    color = if (selectedTab == 0) DeepNavy else TextSecondary,
                                    fontSize = 15.sp
                                )
                            }
                        },
                        modifier = Modifier.testTag("tab_student_login")
                    )

                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(vertical = 12.dp)
                            ) {
                                Icon(
                                    Icons.Default.Security,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp),
                                    tint = if (selectedTab == 1) DeepNavy else TextSecondary
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "Staff / Official",
                                    fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Medium,
                                    color = if (selectedTab == 1) DeepNavy else TextSecondary,
                                    fontSize = 15.sp
                                )
                            }
                        },
                        modifier = Modifier.testTag("tab_admin_login")
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // ==================== ANIMATED CONTENT TRANSITION ====================
            AnimatedContent(
                targetState = selectedTab,
                transitionSpec = {
                    if (targetState > initialState) {
                        (slideInHorizontally(animationSpec = tween(320)) { width -> width } + fadeIn(animationSpec = tween(320)))
                            .togetherWith(slideOutHorizontally(animationSpec = tween(300)) { width -> -width } + fadeOut(animationSpec = tween(300)))
                    } else {
                        (slideInHorizontally(animationSpec = tween(320)) { width -> -width } + fadeIn(animationSpec = tween(320)))
                            .togetherWith(slideOutHorizontally(animationSpec = tween(300)) { width -> width } + fadeOut(animationSpec = tween(300)))
                    }
                },
                label = "login_tab_content"
            ) { tabIndex ->
                if (tabIndex == 0) {
                    // ==================== STUDENT TAB CONTENT ====================
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(18.dp),
                            border = BorderStroke(1.dp, BorderLight),
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(4.dp, RoundedCornerShape(18.dp), ambientColor = DeepNavy.copy(alpha = 0.05f))
                        ) {
                            Column(modifier = Modifier.padding(22.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(38.dp)
                                            .clip(CircleShape)
                                            .background(SkyBlueLight),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            Icons.Default.School,
                                            contentDescription = null,
                                            tint = SkyBlue,
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            "Student Sign In",
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = DeepNavy
                                        )
                                        Text(
                                            "Enter institutional Roll No & Date of Birth",
                                            fontSize = 12.sp,
                                            color = TextSecondary
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(18.dp))

                                // Error Banner with slide animation
                                AnimatedVisibility(
                                    visible = studentLoginError != null,
                                    enter = fadeIn() + slideInHorizontally(),
                                    exit = fadeOut()
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(Color(0xFFFEE2E2))
                                            .border(1.dp, Color(0xFFEF4444), RoundedCornerShape(10.dp))
                                            .padding(12.dp)
                                    ) {
                                        Text(
                                            text = "⚠️ $studentLoginError",
                                            color = Color(0xFFB91C1C),
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(14.dp))
                                }

                                // 1. Roll Number Input
                                Text(
                                    "ROLL NUMBER",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DeepNavy,
                                    letterSpacing = 0.8.sp
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                OutlinedTextField(
                                    value = studentRollNo,
                                    onValueChange = { studentRollNo = it.uppercase() },
                                    placeholder = { Text("e.g. 24CS042, 22ME108") },
                                    leadingIcon = {
                                        Icon(Icons.Default.Person, contentDescription = null, tint = SkyBlue)
                                    },
                                    keyboardOptions = KeyboardOptions(
                                        capitalization = KeyboardCapitalization.Characters
                                    ),
                                    singleLine = true,
                                    colors = appOutlinedTextFieldColors(),
                                    textStyle = AppInputTextStyle,
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("student_rollno_input")
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                // 2. Date of Birth Input
                                Text(
                                    "DATE OF BIRTH (DD/MM/YYYY)",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DeepNavy,
                                    letterSpacing = 0.8.sp
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                OutlinedTextField(
                                    value = studentDob,
                                    onValueChange = { studentDob = it },
                                    placeholder = { Text("DD/MM/YYYY (e.g. 15/08/2004)") },
                                    leadingIcon = {
                                        Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = SkyBlue)
                                    },
                                    trailingIcon = {
                                        IconButton(onClick = { datePickerDialog.show() }) {
                                            Icon(
                                                Icons.Default.CalendarMonth,
                                                contentDescription = "Pick Date of Birth",
                                                tint = DeepNavy
                                            )
                                        }
                                    },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                                    singleLine = true,
                                    colors = appOutlinedTextFieldColors(),
                                    textStyle = AppInputTextStyle,
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("student_dob_input")
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                // Quick Student Demo Chips
                                Text(
                                    "QUICK AUTO-FILL DEMO:",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextSecondary,
                                    letterSpacing = 0.6.sp
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    listOf(
                                        Pair("24CS042", "15/08/2004"),
                                        Pair("23EC108", "22/03/2003"),
                                        Pair("22ME089", "10/11/2002")
                                    ).forEach { (roll, dob) ->
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(SkyBlueLight)
                                                .border(1.dp, SkyBlue.copy(alpha = 0.35f), RoundedCornerShape(8.dp))
                                                .clickable {
                                                    studentRollNo = roll
                                                    studentDob = dob
                                                }
                                                .padding(vertical = 6.dp, horizontal = 4.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                Text(
                                                    text = roll,
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = DeepNavy
                                                )
                                                Text(
                                                    text = dob,
                                                    fontSize = 9.sp,
                                                    color = TextSecondary
                                                )
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(20.dp))

                                // Vibrant Gradient Sign In Button
                                Button(
                                    onClick = {
                                        viewModel.loginStudent(studentRollNo, studentDob) {
                                            onStudentLoginSuccess()
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(52.dp)
                                        .shadow(4.dp, RoundedCornerShape(12.dp))
                                        .testTag("student_login_button"),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = DeepNavy,
                                        contentColor = Color.White
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            "Sign In as Student",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 15.sp,
                                            color = Color.White
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Icon(
                                            Icons.AutoMirrored.Filled.ArrowForward,
                                            contentDescription = null,
                                            tint = ElectricCyan,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }


                    }
                } else {
                    // ==================== ADMIN & STAFF TAB CONTENT ====================
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(20.dp),
                            border = BorderStroke(1.5.dp, Color(0xFFE2E8F0)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(8.dp, RoundedCornerShape(20.dp), ambientColor = DeepNavy.copy(alpha = 0.12f))
                        ) {
                            Column(modifier = Modifier.padding(22.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(44.dp)
                                                .clip(RoundedCornerShape(14.dp))
                                                .background(
                                                    Brush.linearGradient(
                                                        listOf(DeepNavy, NavyLight)
                                                    )
                                                )
                                                .border(1.dp, ElectricCyan.copy(alpha = 0.5f), RoundedCornerShape(14.dp)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                Icons.Default.Security,
                                                contentDescription = null,
                                                tint = ElectricCyan,
                                                modifier = Modifier.size(24.dp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column {
                                            Text(
                                                "Staff & Official Portal",
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = DeepNavy
                                            )
                                            Text(
                                                "Administrative & Approval Desks",
                                                fontSize = 12.sp,
                                                color = TextSecondary
                                            )
                                        }
                                    }

                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(SkyBlueLight)
                                            .border(1.dp, SkyBlue.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            "OFFICIAL",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = SkyBlue,
                                            letterSpacing = 0.8.sp
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(18.dp))

                                // Error Banner
                                AnimatedVisibility(
                                    visible = adminLoginError != null,
                                    enter = fadeIn() + slideInHorizontally(),
                                    exit = fadeOut()
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(Color(0xFFFEE2E2))
                                            .border(1.dp, Color(0xFFEF4444), RoundedCornerShape(12.dp))
                                            .padding(12.dp)
                                    ) {
                                        Text(
                                            text = "⚠️ $adminLoginError",
                                            color = Color(0xFFB91C1C),
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(14.dp))
                                }

                                // 1. DIRECT DESK LOGIN CARDS
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        "INSTANT DESK AUTHORIZATION",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = DeepNavy,
                                        letterSpacing = 1.sp
                                    )
                                    Text(
                                        "ONE-TAP ACCESS",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ElectricCyan,
                                        letterSpacing = 0.5.sp
                                    )
                                }
                                Spacer(modifier = Modifier.height(10.dp))

                                // Transport Desk Card
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            Toast.makeText(context, "Entering Transport In-Charge Desk...", Toast.LENGTH_SHORT).show()
                                            viewModel.quickLoginRole("transport", onAdminLoginSuccess)
                                        }
                                        .testTag("direct_transport_card"),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color.White),
                                    border = BorderStroke(1.5.dp, Color(0xFFBAE6FD)),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(
                                                Brush.horizontalGradient(
                                                    listOf(Color(0xFFF0F9FF), Color.White)
                                                )
                                            )
                                            .padding(14.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(44.dp)
                                                    .clip(RoundedCornerShape(12.dp))
                                                    .background(
                                                        Brush.linearGradient(
                                                            listOf(SkyBlue, Color(0xFF0284C7))
                                                        )
                                                    )
                                                    .border(1.dp, SkyBlueLight, RoundedCornerShape(12.dp)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    Icons.Default.VerifiedUser,
                                                    contentDescription = null,
                                                    tint = Color.White,
                                                    modifier = Modifier.size(24.dp)
                                                )
                                            }
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Column {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Text(
                                                        "Transport Desk",
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 15.sp,
                                                        color = DeepNavy
                                                    )
                                                    Spacer(modifier = Modifier.width(6.dp))
                                                    Box(
                                                        modifier = Modifier
                                                            .clip(RoundedCornerShape(4.dp))
                                                            .background(SkyBlue.copy(alpha = 0.15f))
                                                            .padding(horizontal = 5.dp, vertical = 1.dp)
                                                    ) {
                                                        Text("STAGE 1", fontSize = 8.sp, fontWeight = FontWeight.ExtraBold, color = SkyBlue)
                                                    }
                                                }
                                                Spacer(modifier = Modifier.height(2.dp))
                                                Text(
                                                    "RC Book, License & Helmet Verification",
                                                    fontSize = 11.sp,
                                                    color = TextSecondary
                                                )
                                            }
                                        }
                                        Button(
                                            onClick = {
                                                Toast.makeText(context, "Entering Transport In-Charge Desk...", Toast.LENGTH_SHORT).show()
                                                viewModel.quickLoginRole("transport", onAdminLoginSuccess)
                                            },
                                            shape = RoundedCornerShape(10.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = DeepNavy),
                                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text("Enter", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = ElectricCyan, modifier = Modifier.size(14.dp))
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                // Principal Desk Card
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            Toast.makeText(context, "Entering Principal's Office Desk...", Toast.LENGTH_SHORT).show()
                                            viewModel.quickLoginRole("principal", onAdminLoginSuccess)
                                        }
                                        .testTag("direct_principal_card"),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color.White),
                                    border = BorderStroke(1.5.dp, Color(0xFFFDE68A)),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(
                                                Brush.horizontalGradient(
                                                    listOf(Color(0xFFFFFBEB), Color.White)
                                                )
                                            )
                                            .padding(14.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(44.dp)
                                                    .clip(RoundedCornerShape(12.dp))
                                                    .background(
                                                        Brush.linearGradient(
                                                            listOf(AmberWarning, Color(0xFFD97706))
                                                        )
                                                    )
                                                    .border(1.dp, Color(0xFFFEF3C7), RoundedCornerShape(12.dp)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    Icons.Default.Security,
                                                    contentDescription = null,
                                                    tint = Color.White,
                                                    modifier = Modifier.size(24.dp)
                                                )
                                            }
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Column {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Text(
                                                        "Principal's Office",
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 15.sp,
                                                        color = DeepNavy
                                                    )
                                                    Spacer(modifier = Modifier.width(6.dp))
                                                    Box(
                                                        modifier = Modifier
                                                            .clip(RoundedCornerShape(4.dp))
                                                            .background(AmberWarning.copy(alpha = 0.15f))
                                                            .padding(horizontal = 5.dp, vertical = 1.dp)
                                                    ) {
                                                        Text("FINAL SIGN-OFF", fontSize = 8.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFFB45309))
                                                    }
                                                }
                                                Spacer(modifier = Modifier.height(2.dp))
                                                Text(
                                                    "Final Approval & QR Pass Generation",
                                                    fontSize = 11.sp,
                                                    color = TextSecondary
                                                )
                                            }
                                        }
                                        Button(
                                            onClick = {
                                                Toast.makeText(context, "Entering Principal's Office Desk...", Toast.LENGTH_SHORT).show()
                                                viewModel.quickLoginRole("principal", onAdminLoginSuccess)
                                            },
                                            shape = RoundedCornerShape(10.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = DeepNavy),
                                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text("Enter", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = AmberWarning, modifier = Modifier.size(14.dp))
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(22.dp))

                                // Section Divider
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    HorizontalDivider(modifier = Modifier.weight(1f), color = BorderLight)
                                    Text(
                                        "  CUSTOM CREDENTIAL LOGIN  ",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = TextSecondary,
                                        letterSpacing = 0.8.sp
                                    )
                                    HorizontalDivider(modifier = Modifier.weight(1f), color = BorderLight)
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                // 1. Admin User ID Field
                                Text(
                                    "OFFICIAL USER ID",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DeepNavy,
                                    letterSpacing = 0.8.sp
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                OutlinedTextField(
                                    value = adminUserId,
                                    onValueChange = { adminUserId = it },
                                    placeholder = { Text("e.g. transport, principal") },
                                    leadingIcon = {
                                        Icon(Icons.Default.AccountCircle, contentDescription = null, tint = SkyBlue)
                                    },
                                    singleLine = true,
                                    colors = appOutlinedTextFieldColors(),
                                    textStyle = AppInputTextStyle,
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("admin_userid_input")
                                )

                                Spacer(modifier = Modifier.height(14.dp))

                                // 2. Password Field
                                Text(
                                    "PASSWORD",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DeepNavy,
                                    letterSpacing = 0.8.sp
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                OutlinedTextField(
                                    value = adminPassword,
                                    onValueChange = { adminPassword = it },
                                    placeholder = { Text("••••••••") },
                                    leadingIcon = {
                                        Icon(Icons.Default.Lock, contentDescription = null, tint = SkyBlue)
                                    },
                                    trailingIcon = {
                                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                            Icon(
                                                imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                                contentDescription = "Toggle password visibility",
                                                tint = DeepNavy
                                            )
                                        }
                                    },
                                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                    singleLine = true,
                                    colors = appOutlinedTextFieldColors(),
                                    textStyle = AppInputTextStyle,
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("admin_password_input")
                                )

                                Spacer(modifier = Modifier.height(20.dp))

                                // Sign In Button
                                Button(
                                    onClick = {
                                        val u = adminUserId.ifBlank { "transport" }
                                        val p = adminPassword.ifBlank { "transport123" }
                                        viewModel.login(u, p, onSuccess = onAdminLoginSuccess)
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(52.dp)
                                        .shadow(6.dp, RoundedCornerShape(14.dp), ambientColor = DeepNavy.copy(alpha = 0.25f))
                                        .testTag("admin_login_button"),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = DeepNavy,
                                        contentColor = Color.White
                                    ),
                                    shape = RoundedCornerShape(14.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Security, contentDescription = null, tint = ElectricCyan, modifier = Modifier.size(18.dp))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            "Authenticate & Open Portal",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 15.sp,
                                            color = Color.White
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
