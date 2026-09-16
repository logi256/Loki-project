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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartbikepass.ui.theme.BackgroundLight
import com.example.smartbikepass.ui.theme.BorderLight
import com.example.smartbikepass.ui.theme.DeepNavy
import com.example.smartbikepass.ui.theme.EmeraldGreen
import com.example.smartbikepass.ui.theme.EmeraldLight
import com.example.smartbikepass.ui.theme.SkyBlue
import com.example.smartbikepass.ui.theme.TextPrimary
import com.example.smartbikepass.ui.theme.TextSecondary
import com.example.smartbikepass.viewmodel.BikePassViewModel
import com.example.smartbikepass.viewmodel.SubmitUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplyScreen(
    viewModel: BikePassViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToStatus: (passId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val submitState by viewModel.submitState.collectAsState()

    var fullName by remember { mutableStateOf("") }
    var rollNo by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    val departments = listOf(
        "Computer Science",
        "Electronics",
        "Mechanical",
        "Civil",
        "Electrical",
        "Information Technology",
        "Other"
    )
    var selectedDepartment by remember { mutableStateOf(departments[0]) }
    var deptExpanded by remember { mutableStateOf(false) }

    val years = listOf("1st Year", "2nd Year", "3rd Year", "4th Year")
    var selectedYear by remember { mutableStateOf(years[1]) }
    var yearExpanded by remember { mutableStateOf(false) }

    var vehicleNo by remember { mutableStateOf("") }

    val vehicleTypes = listOf(
        "Two Wheeler (Petrol)",
        "Two Wheeler (Electric)",
        "Bicycle"
    )
    var selectedVehicleType by remember { mutableStateOf(vehicleTypes[0]) }
    var vehicleTypeExpanded by remember { mutableStateOf(false) }

    var rcAttached by remember { mutableStateOf(true) }
    var licenseAttached by remember { mutableStateOf(true) }
    var insuranceAttached by remember { mutableStateOf(true) }

    var validationError by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Apply for Bike Pass",
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
                    TextButton(
                        onClick = {
                            fullName = "Arun Kumar"
                            rollNo = "24CS042"
                            email = "arun.cs@college.edu"
                            phone = "9876543210"
                            selectedDepartment = "Computer Science"
                            selectedYear = "2nd Year"
                            vehicleNo = "TN37 B9876"
                            selectedVehicleType = "Two Wheeler (Petrol)"
                            rcAttached = true
                            licenseAttached = true
                            insuranceAttached = true
                            validationError = null
                        }
                    ) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = SkyBlue, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Demo Fill", color = SkyBlue, fontSize = 12.sp)
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
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "📋 Student & Vehicle Details",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeepNavy
                    )
                    Text(
                        text = "Please fill all details accurately to prevent application delays.",
                        fontSize = 12.sp,
                        color = TextSecondary,
                        modifier = Modifier.padding(bottom = 16.dp, top = 4.dp)
                    )

                    if (validationError != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFFEE2E2))
                                .border(1.dp, Color(0xFFEF4444), RoundedCornerShape(8.dp))
                                .padding(10.dp)
                        ) {
                            Text(
                                text = "⚠️ $validationError",
                                color = Color(0xFFB91C1C),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    // Full Name
                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it; validationError = null },
                        label = { Text("Full Name *") },
                        placeholder = { Text("e.g. Rahul Kumar") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = TextSecondary) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("full_name_input"),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Roll Number
                    OutlinedTextField(
                        value = rollNo,
                        onValueChange = { rollNo = it.uppercase(); validationError = null },
                        label = { Text("Roll Number *") },
                        placeholder = { Text("e.g. CS21B001") },
                        leadingIcon = { Icon(Icons.Default.School, contentDescription = null, tint = TextSecondary) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("roll_no_input"),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Email Address
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it; validationError = null },
                        label = { Text("Email Address *") },
                        placeholder = { Text("student@college.edu") },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = TextSecondary) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Phone Number
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it; validationError = null },
                        label = { Text("Phone Number *") },
                        placeholder = { Text("10-digit mobile number") },
                        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = TextSecondary) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Department Dropdown
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = selectedDepartment,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Department *") },
                            trailingIcon = {
                                Icon(
                                    Icons.Default.ArrowDropDown,
                                    contentDescription = null,
                                    modifier = Modifier.clickable { deptExpanded = !deptExpanded }
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { deptExpanded = !deptExpanded },
                            shape = RoundedCornerShape(10.dp)
                        )
                        DropdownMenu(
                            expanded = deptExpanded,
                            onDismissRequest = { deptExpanded = false },
                            modifier = Modifier.fillMaxWidth(0.85f)
                        ) {
                            departments.forEach { dept ->
                                DropdownMenuItem(
                                    text = { Text(dept) },
                                    onClick = {
                                        selectedDepartment = dept
                                        deptExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Year Dropdown
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = selectedYear,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Year of Study *") },
                            trailingIcon = {
                                Icon(
                                    Icons.Default.ArrowDropDown,
                                    contentDescription = null,
                                    modifier = Modifier.clickable { yearExpanded = !yearExpanded }
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { yearExpanded = !yearExpanded },
                            shape = RoundedCornerShape(10.dp)
                        )
                        DropdownMenu(
                            expanded = yearExpanded,
                            onDismissRequest = { yearExpanded = false },
                            modifier = Modifier.fillMaxWidth(0.85f)
                        ) {
                            years.forEach { yr ->
                                DropdownMenuItem(
                                    text = { Text(yr) },
                                    onClick = {
                                        selectedYear = yr
                                        yearExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Vehicle Registration Number
                    OutlinedTextField(
                        value = vehicleNo,
                        onValueChange = { vehicleNo = it.uppercase(); validationError = null },
                        label = { Text("Vehicle Registration No *") },
                        placeholder = { Text("e.g. TN01AB1234") },
                        leadingIcon = { Icon(Icons.Default.DirectionsBike, contentDescription = null, tint = TextSecondary) },
                        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("vehicle_no_input"),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Vehicle Type Dropdown
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = selectedVehicleType,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Vehicle Type *") },
                            trailingIcon = {
                                Icon(
                                    Icons.Default.ArrowDropDown,
                                    contentDescription = null,
                                    modifier = Modifier.clickable { vehicleTypeExpanded = !vehicleTypeExpanded }
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { vehicleTypeExpanded = !vehicleTypeExpanded },
                            shape = RoundedCornerShape(10.dp)
                        )
                        DropdownMenu(
                            expanded = vehicleTypeExpanded,
                            onDismissRequest = { vehicleTypeExpanded = false },
                            modifier = Modifier.fillMaxWidth(0.85f)
                        ) {
                            vehicleTypes.forEach { vt ->
                                DropdownMenuItem(
                                    text = { Text(vt) },
                                    onClick = {
                                        selectedVehicleType = vt
                                        vehicleTypeExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Document Uploads Section
                    Text(
                        text = "📁 Required Document Attachments",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeepNavy
                    )
                    Text(
                        text = "Documents will be verified by the Transport In-Charge during review.",
                        fontSize = 11.sp,
                        color = TextSecondary,
                        modifier = Modifier.padding(bottom = 12.dp, top = 2.dp)
                    )

                    DocumentToggleItem(
                        title = "RC Book (Registration Certificate)",
                        attached = rcAttached,
                        onToggle = { rcAttached = !rcAttached }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    DocumentToggleItem(
                        title = "Driving License",
                        attached = licenseAttached,
                        onToggle = { licenseAttached = !licenseAttached }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    DocumentToggleItem(
                        title = "Vehicle Insurance Copy",
                        attached = insuranceAttached,
                        onToggle = { insuranceAttached = !insuranceAttached }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Submit Button
                    Button(
                        onClick = {
                            if (fullName.isBlank()) {
                                validationError = "Please enter your Full Name"
                                return@Button
                            }
                            if (rollNo.isBlank()) {
                                validationError = "Please enter your Roll Number"
                                return@Button
                            }
                            if (email.isBlank() || !email.contains("@")) {
                                validationError = "Please enter a valid Email Address"
                                return@Button
                            }
                            if (phone.isBlank() || phone.length < 8) {
                                validationError = "Please enter a valid Phone Number"
                                return@Button
                            }
                            if (vehicleNo.isBlank()) {
                                validationError = "Please enter Vehicle Registration Number"
                                return@Button
                            }
                            if (!rcAttached || !licenseAttached || !insuranceAttached) {
                                validationError = "Please ensure all 3 documents (RC, License, Insurance) are attached"
                                return@Button
                            }

                            validationError = null
                            viewModel.submitApplication(
                                fullName = fullName,
                                rollNo = rollNo,
                                email = email,
                                phone = phone,
                                department = selectedDepartment,
                                year = selectedYear,
                                vehicleNo = vehicleNo,
                                vehicleType = selectedVehicleType,
                                rcBook = "rc_doc_${rollNo.lowercase()}.png",
                                license = "dl_doc_${rollNo.lowercase()}.png",
                                insurance = "ins_doc_${rollNo.lowercase()}.png"
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("submit_application_button"),
                        enabled = submitState !is SubmitUiState.Submitting,
                        colors = ButtonDefaults.buttonColors(containerColor = DeepNavy),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        if (submitState is SubmitUiState.Submitting) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Submitting...")
                        } else {
                            Text("🚀 Submit Application", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }

    // Success Dialog with generated Pass ID
    val currentSubmitState = submitState
    if (currentSubmitState is SubmitUiState.Success) {
        AlertDialog(
            onDismissRequest = { viewModel.resetSubmitState() },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldGreen)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Application Submitted!")
                }
            },
            text = {
                Column {
                    Text(
                        "Your application has been received and forwarded to Transport In-Charge for review.",
                        fontSize = 13.sp,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Text("Your Generated Pass ID:", fontSize = 12.sp, color = TextSecondary)
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(BackgroundLight)
                            .border(1.dp, SkyBlue, RoundedCornerShape(8.dp))
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = currentSubmitState.passId,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 16.sp,
                                color = DeepNavy
                            )
                            IconButton(
                                onClick = {
                                    clipboardManager.setText(AnnotatedString(currentSubmitState.passId))
                                    Toast.makeText(context, "Pass ID copied to clipboard", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    Icons.Default.ContentCopy,
                                    contentDescription = "Copy Pass ID",
                                    tint = SkyBlue,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        "Save this Pass ID to track your status or view your QR pass when approved.",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val pid = currentSubmitState.passId
                        viewModel.resetSubmitState()
                        onNavigateToStatus(pid)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SkyBlue)
                ) {
                    Text("Track Status")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        viewModel.resetSubmitState()
                        onNavigateBack()
                    }
                ) {
                    Text("Done")
                }
            }
        )
    }
}

@Composable
fun DocumentToggleItem(
    title: String,
    attached: Boolean,
    onToggle: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(if (attached) EmeraldLight.copy(alpha = 0.5f) else Color(0xFFF1F5F9))
            .border(
                1.dp,
                if (attached) EmeraldGreen.copy(alpha = 0.5f) else BorderLight,
                RoundedCornerShape(8.dp)
            )
            .clickable { onToggle() }
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Icon(
                    Icons.Default.AttachFile,
                    contentDescription = null,
                    tint = if (attached) EmeraldGreen else TextSecondary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    fontSize = 12.sp,
                    color = DeepNavy,
                    fontWeight = FontWeight.Medium
                )
            }
            Text(
                text = if (attached) "✓ Attached" else "+ Attach",
                color = if (attached) EmeraldGreen else SkyBlue,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
