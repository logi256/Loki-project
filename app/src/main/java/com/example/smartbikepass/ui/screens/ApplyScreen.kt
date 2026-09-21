package com.example.smartbikepass.ui.screens

import android.net.Uri
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.draw.shadow
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
import com.example.smartbikepass.ui.theme.ElectricCyan
import com.example.smartbikepass.ui.theme.EmeraldGreen
import com.example.smartbikepass.ui.theme.EmeraldLight
import com.example.smartbikepass.ui.theme.SkyBlue
import com.example.smartbikepass.ui.theme.TextPrimary
import com.example.smartbikepass.ui.theme.TextSecondary
import com.example.smartbikepass.ui.theme.appOutlinedTextFieldColors
import com.example.smartbikepass.ui.theme.AppInputTextStyle
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
    val currentStudent by viewModel.currentStudent.collectAsState()

    var fullName by remember { mutableStateOf("") }
    var rollNo by remember { mutableStateOf(currentStudent?.rollNo ?: "") }
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

    var rcFileName by remember { mutableStateOf<String?>(null) }
    var rcFileUri by remember { mutableStateOf<Uri?>(null) }
    var rcFileSize by remember { mutableStateOf<String?>(null) }

    var licenseFileName by remember { mutableStateOf<String?>(null) }
    var licenseFileUri by remember { mutableStateOf<Uri?>(null) }
    var licenseFileSize by remember { mutableStateOf<String?>(null) }

    var insuranceFileName by remember { mutableStateOf<String?>(null) }
    var insuranceFileUri by remember { mutableStateOf<Uri?>(null) }
    var insuranceFileSize by remember { mutableStateOf<String?>(null) }

    var validationError by remember { mutableStateOf<String?>(null) }

    var activeUploadDialogDocType by remember { mutableStateOf<String?>(null) }
    var previewDocTitle by remember { mutableStateOf<String?>(null) }
    var previewDocFileName by remember { mutableStateOf<String?>(null) }
    var previewDocVehicleNo by remember { mutableStateOf<String?>(null) }

    val browseFileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            val (name, size) = getFileNameAndSize(context, uri)
            when (activeUploadDialogDocType) {
                "rc" -> { rcFileName = name; rcFileUri = uri; rcFileSize = size }
                "dl" -> { licenseFileName = name; licenseFileUri = uri; licenseFileSize = size }
                "insurance" -> { insuranceFileName = name; insuranceFileUri = uri; insuranceFileSize = size }
            }
            activeUploadDialogDocType = null
            validationError = null
            Toast.makeText(context, "File attached: $name", Toast.LENGTH_SHORT).show()
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            val (name, size) = getFileNameAndSize(context, uri)
            when (activeUploadDialogDocType) {
                "rc" -> { rcFileName = name; rcFileUri = uri; rcFileSize = size }
                "dl" -> { licenseFileName = name; licenseFileUri = uri; licenseFileSize = size }
                "insurance" -> { insuranceFileName = name; insuranceFileUri = uri; insuranceFileSize = size }
            }
            activeUploadDialogDocType = null
            validationError = null
            Toast.makeText(context, "Photo attached: $name", Toast.LENGTH_SHORT).show()
        }
    }

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
                            rcFileName = "TN37_B9876_RC_Copy.pdf"
                            rcFileSize = "1.8 MB"
                            licenseFileName = "DL_24CS042_TamilNadu.pdf"
                            licenseFileSize = "850 KB"
                            insuranceFileName = "BajajAllianz_Insurance_Policy.pdf"
                            insuranceFileSize = "1.2 MB"
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
                        colors = appOutlinedTextFieldColors(),
                        textStyle = AppInputTextStyle,
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
                        colors = appOutlinedTextFieldColors(),
                        textStyle = AppInputTextStyle,
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
                        colors = appOutlinedTextFieldColors(),
                        textStyle = AppInputTextStyle,
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
                        colors = appOutlinedTextFieldColors(),
                        textStyle = AppInputTextStyle,
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
                            colors = appOutlinedTextFieldColors(),
                            textStyle = AppInputTextStyle,
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
                            colors = appOutlinedTextFieldColors(),
                            textStyle = AppInputTextStyle,
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
                        colors = appOutlinedTextFieldColors(),
                        textStyle = AppInputTextStyle,
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
                            colors = appOutlinedTextFieldColors(),
                            textStyle = AppInputTextStyle,
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
                        text = "Tap to browse and upload PDF or Image documents from your device.",
                        fontSize = 11.sp,
                        color = TextSecondary,
                        modifier = Modifier.padding(bottom = 12.dp, top = 2.dp)
                    )

                    DocumentUploadCard(
                        title = "RC Book (Registration Certificate) *",
                        subtitle = "Vehicle Registration document copy",
                        fileName = rcFileName,
                        fileSize = rcFileSize,
                        testTag = "upload_rc_button",
                        onUploadClick = { activeUploadDialogDocType = "rc" },
                        onPreviewClick = {
                            previewDocTitle = "RC Book (Registration Certificate)"
                            previewDocFileName = rcFileName
                            previewDocVehicleNo = vehicleNo
                        },
                        onRemoveClick = {
                            rcFileName = null
                            rcFileUri = null
                            rcFileSize = null
                        }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    DocumentUploadCard(
                        title = "Driving License *",
                        subtitle = "Valid 2-wheeler Driving License or LLR",
                        fileName = licenseFileName,
                        fileSize = licenseFileSize,
                        testTag = "upload_license_button",
                        onUploadClick = { activeUploadDialogDocType = "dl" },
                        onPreviewClick = {
                            previewDocTitle = "Driving License"
                            previewDocFileName = licenseFileName
                            previewDocVehicleNo = vehicleNo
                        },
                        onRemoveClick = {
                            licenseFileName = null
                            licenseFileUri = null
                            licenseFileSize = null
                        }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    DocumentUploadCard(
                        title = "Vehicle Insurance Copy *",
                        subtitle = "Active vehicle insurance certificate",
                        fileName = insuranceFileName,
                        fileSize = insuranceFileSize,
                        testTag = "upload_insurance_button",
                        onUploadClick = { activeUploadDialogDocType = "insurance" },
                        onPreviewClick = {
                            previewDocTitle = "Vehicle Insurance Copy"
                            previewDocFileName = insuranceFileName
                            previewDocVehicleNo = vehicleNo
                        },
                        onRemoveClick = {
                            insuranceFileName = null
                            insuranceFileUri = null
                            insuranceFileSize = null
                        }
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
                            if (rcFileName == null || licenseFileName == null || insuranceFileName == null) {
                                validationError = "Please upload all 3 required documents (RC Book, Driving License, Insurance)"
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
                                rcBook = rcFileName,
                                license = licenseFileName,
                                insurance = insuranceFileName
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .shadow(8.dp, RoundedCornerShape(14.dp), ambientColor = DeepNavy.copy(alpha = 0.25f))
                            .testTag("submit_application_button"),
                        enabled = submitState !is SubmitUiState.Submitting,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DeepNavy,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        if (submitState is SubmitUiState.Submitting) {
                            CircularProgressIndicator(
                                color = ElectricCyan,
                                modifier = Modifier.size(22.dp),
                                strokeWidth = 2.5.dp
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("Submitting Application...", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        } else {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Send, contentDescription = null, tint = ElectricCyan, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "Submit Pass Application",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
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
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SkyBlue,
                        contentColor = Color.White
                    )
                ) {
                    Text("Track Status", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        viewModel.resetSubmitState()
                        onNavigateBack()
                    }
                ) {
                    Text("Done", color = DeepNavy, fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    // Document Upload Method Chooser Dialog
    val currentUploadDocType = activeUploadDialogDocType
    if (currentUploadDocType != null) {
        UploadDocumentModalDialog(
            docType = currentUploadDocType,
            vehicleNo = vehicleNo,
            rollNo = rollNo,
            onDismiss = { activeUploadDialogDocType = null },
            onBrowseFiles = {
                try {
                    browseFileLauncher.launch("*/*")
                } catch (e: Exception) {
                    Toast.makeText(context, "Device file picker not available on this emulator. Please pick from Gallery or choose a preset.", Toast.LENGTH_LONG).show()
                }
            },
            onPickGallery = {
                try {
                    galleryLauncher.launch("image/*")
                } catch (e: Exception) {
                    Toast.makeText(context, "Photo gallery not available on this emulator. Please select a preset below.", Toast.LENGTH_LONG).show()
                }
            },
            onAttachPreset = { fileName, fileSize ->
                when (currentUploadDocType) {
                    "rc" -> { rcFileName = fileName; rcFileSize = fileSize }
                    "dl" -> { licenseFileName = fileName; licenseFileSize = fileSize }
                    else -> { insuranceFileName = fileName; insuranceFileSize = fileSize }
                }
                activeUploadDialogDocType = null
                validationError = null
                Toast.makeText(context, "Attached: $fileName", Toast.LENGTH_SHORT).show()
            }
        )
    }

    // Document Preview Dialog
    val currentPreviewTitle = previewDocTitle
    val currentPreviewFileName = previewDocFileName
    if (currentPreviewTitle != null && currentPreviewFileName != null) {
        DocumentPreviewDialog(
            title = currentPreviewTitle,
            fileName = currentPreviewFileName,
            vehicleNo = previewDocVehicleNo ?: vehicleNo,
            rollNo = rollNo,
            onDismiss = {
                previewDocTitle = null
                previewDocFileName = null
                previewDocVehicleNo = null
            }
        )
    }
}

private fun getFileNameAndSize(context: android.content.Context, uri: Uri): Pair<String, String> {
    var name = "document_${System.currentTimeMillis().toString().takeLast(4)}.pdf"
    var size = ""
    try {
        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
            if (cursor.moveToFirst()) {
                if (nameIndex != -1) {
                    val resolved = cursor.getString(nameIndex)
                    if (!resolved.isNullOrBlank()) {
                        name = resolved
                    }
                }
                if (sizeIndex != -1) {
                    val bytes = cursor.getLong(sizeIndex)
                    size = when {
                        bytes >= 1024 * 1024 -> String.format(java.util.Locale.US, "%.1f MB", bytes / (1024f * 1024f))
                        bytes > 0 -> String.format(java.util.Locale.US, "%d KB", bytes / 1024)
                        else -> ""
                    }
                }
            }
        }
    } catch (e: Exception) {
        name = uri.lastPathSegment ?: "document.pdf"
    }
    return Pair(name, size)
}

@Composable
fun DocumentUploadCard(
    title: String,
    subtitle: String,
    fileName: String?,
    fileSize: String?,
    testTag: String,
    onUploadClick: () -> Unit,
    onPreviewClick: (() -> Unit)? = null,
    onRemoveClick: () -> Unit
) {
    val isAttached = fileName != null

    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (isAttached) EmeraldLight.copy(alpha = 0.22f) else Color(0xFFF8FAFC)
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isAttached) EmeraldGreen.copy(alpha = 0.6f) else BorderLight
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isAttached) EmeraldLight else Color(0xFFE2E8F0)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            if (isAttached) Icons.Default.CheckCircle else Icons.Default.Description,
                            contentDescription = null,
                            tint = if (isAttached) EmeraldGreen else TextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = title,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = DeepNavy
                        )
                        Text(
                            text = subtitle,
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (isAttached) EmeraldLight else Color(0xFFEFF6FF))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = if (isAttached) "✓ Attached" else "Required",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isAttached) EmeraldGreen else SkyBlue
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (isAttached) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White)
                        .border(1.dp, Color(0xFFCBD5E1), RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            Icons.Default.PictureAsPdf,
                            contentDescription = null,
                            tint = Color(0xFFE11D48),
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = fileName ?: "document.pdf",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = DeepNavy,
                                maxLines = 1
                            )
                            if (!fileSize.isNullOrBlank()) {
                                Text(
                                    text = fileSize,
                                    fontSize = 10.sp,
                                    color = TextSecondary
                                )
                            }
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (onPreviewClick != null) {
                            OutlinedButton(
                                onClick = onPreviewClick,
                                shape = RoundedCornerShape(6.dp),
                                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                modifier = Modifier.height(32.dp)
                            ) {
                                Icon(Icons.Default.Visibility, contentDescription = "View preview", modifier = Modifier.size(12.dp))
                                Spacer(modifier = Modifier.width(3.dp))
                                Text("View", fontSize = 11.sp)
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                        }

                        OutlinedButton(
                            onClick = onUploadClick,
                            shape = RoundedCornerShape(6.dp),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                            modifier = Modifier.height(32.dp)
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(3.dp))
                            Text("Change", fontSize = 11.sp)
                        }

                        Spacer(modifier = Modifier.width(4.dp))

                        IconButton(
                            onClick = onRemoveClick,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                Icons.Default.DeleteOutline,
                                contentDescription = "Remove file",
                                tint = Color(0xFFDC2626),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            } else {
                OutlinedButton(
                    onClick = onUploadClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag(testTag),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.White,
                        contentColor = SkyBlue
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SkyBlue)
                ) {
                    Icon(
                        Icons.Default.FileUpload,
                        contentDescription = null,
                        tint = SkyBlue,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Upload Document (PDF / Image)",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = SkyBlue
                    )
                }
            }
        }
    }
}

@Composable
fun UploadDocumentModalDialog(
    docType: String,
    vehicleNo: String,
    rollNo: String,
    onDismiss: () -> Unit,
    onBrowseFiles: () -> Unit,
    onPickGallery: () -> Unit,
    onAttachPreset: (fileName: String, fileSize: String) -> Unit
) {
    val (docTitle, presets) = when (docType) {
        "rc" -> Pair(
            "RC Book (Registration Certificate)",
            listOf(
                "RC_SmartCard_${vehicleNo.ifBlank { "Vehicle" }.replace(" ", "_")}.pdf",
                "Vahan_RC_Certificate.pdf",
                "RC_Original_Scan.jpg"
            )
        )
        "dl" -> Pair(
            "Driving License",
            listOf(
                "Driving_License_${rollNo.ifBlank { "Student" }}.pdf",
                "DigiLocker_Driving_License.pdf",
                "DL_SmartCard_Scan.jpg"
            )
        )
        else -> Pair(
            "Vehicle Insurance Policy",
            listOf(
                "Insurance_Policy_${vehicleNo.ifBlank { "Vehicle" }.replace(" ", "_")}.pdf",
                "Comprehensive_Insurance_Cover.pdf",
                "Policy_Schedule_2026.pdf"
            )
        )
    }

    var selectedFile by remember { mutableStateOf(presets.first()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.CloudUpload, contentDescription = null, tint = SkyBlue, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Upload $docTitle",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = DeepNavy
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Choose your preferred document upload method:",
                    fontSize = 12.sp,
                    color = TextSecondary,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                // Option 1: File Browser
                OutlinedButton(
                    onClick = onBrowseFiles,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = Color(0xFFF8FAFC)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFCBD5E1))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.FolderOpen, contentDescription = null, tint = SkyBlue, modifier = Modifier.size(22.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Browse Device Files (PDF / Doc)", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DeepNavy)
                            Text("Storage, Downloads, or Drive", fontSize = 10.sp, color = TextSecondary)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Option 2: Gallery
                OutlinedButton(
                    onClick = onPickGallery,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = Color(0xFFF8FAFC)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFCBD5E1))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.PhotoLibrary, contentDescription = null, tint = EmeraldGreen, modifier = Modifier.size(22.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Choose Photo from Gallery", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DeepNavy)
                            Text("Select clear picture or photo card", fontSize = 10.sp, color = TextSecondary)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Option 3: Camera Scan
                OutlinedButton(
                    onClick = {
                        val camName = when (docType) {
                            "rc" -> "RC_CamScan_${vehicleNo.ifBlank { "Vehicle" }.replace(" ", "_")}.jpg"
                            "dl" -> "DL_CamScan_${rollNo.ifBlank { "Student" }}.jpg"
                            else -> "Insurance_CamScan_${vehicleNo.ifBlank { "Vehicle" }.replace(" ", "_")}.jpg"
                        }
                        onAttachPreset(camName, "2.4 MB")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = Color(0xFFF8FAFC)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFCBD5E1))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.CameraAlt, contentDescription = null, tint = Color(0xFFF59E0B), modifier = Modifier.size(22.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Scan Document with Camera", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DeepNavy)
                            Text("Capture instant high-clarity snapshot", fontSize = 10.sp, color = TextSecondary)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                HorizontalDivider(color = BorderLight)
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Or Attach by File Name / Digital Copy:",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = DeepNavy
                )
                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = selectedFile,
                    onValueChange = { selectedFile = it },
                    label = { Text("Selected File Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = appOutlinedTextFieldColors(),
                    textStyle = AppInputTextStyle,
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(6.dp))
                Text("Suggested copies:", fontSize = 10.sp, color = TextSecondary)
                Spacer(modifier = Modifier.height(4.dp))

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    presets.forEach { preset ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (selectedFile == preset) SkyBlue.copy(alpha = 0.12f) else Color(0xFFF1F5F9))
                                .border(1.dp, if (selectedFile == preset) SkyBlue else Color(0xFFCBD5E1), RoundedCornerShape(6.dp))
                                .clickable { selectedFile = preset }
                                .padding(horizontal = 8.dp, vertical = 6.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.PictureAsPdf,
                                    contentDescription = null,
                                    tint = if (selectedFile == preset) SkyBlue else TextSecondary,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = preset,
                                    fontSize = 11.sp,
                                    color = if (selectedFile == preset) SkyBlue else TextPrimary
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (selectedFile.isNotBlank()) {
                        val size = if (selectedFile.endsWith(".pdf", ignoreCase = true)) "1.6 MB" else "2.2 MB"
                        onAttachPreset(selectedFile.trim(), size)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = SkyBlue, contentColor = Color.White),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Attach This File", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = TextSecondary)
            }
        }
    )
}

@Composable
fun DocumentPreviewDialog(
    title: String,
    fileName: String,
    vehicleNo: String,
    rollNo: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Description, contentDescription = null, tint = EmeraldGreen)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Document Preview", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DeepNavy)
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DeepNavy)
                Text("File: $fileName", fontSize = 11.sp, color = TextSecondary)
                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFF8FAFC))
                        .border(1.dp, EmeraldGreen.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                        .padding(14.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("GOVT / COLLEGE VERIFIED COPY", fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = EmeraldGreen)
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldGreen, modifier = Modifier.size(16.dp))
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Associated Vehicle: ${vehicleNo.ifBlank { "TN-REGISTERED" }}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
                        Text("Roll Number: ${rollNo.ifBlank { "STUDENT" }}", fontSize = 11.sp, color = TextPrimary)
                        Text("Status: Authenticated for Campus Pass Review", fontSize = 10.sp, color = TextSecondary)
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFE2E8F0))
                                .padding(6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("DIGITAL SIGNATURE & QR SEAL EMBEDDED", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = DeepNavy, contentColor = Color.White),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Close Preview", fontWeight = FontWeight.Bold)
            }
        }
    )
}
