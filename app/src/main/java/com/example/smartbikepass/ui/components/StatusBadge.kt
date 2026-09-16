package com.example.smartbikepass.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartbikepass.ui.theme.AmberLight
import com.example.smartbikepass.ui.theme.AmberWarning
import com.example.smartbikepass.ui.theme.CrimsonLight
import com.example.smartbikepass.ui.theme.CrimsonRed
import com.example.smartbikepass.ui.theme.EmeraldGreen
import com.example.smartbikepass.ui.theme.EmeraldLight
import com.example.smartbikepass.ui.theme.SkyBlue
import com.example.smartbikepass.ui.theme.SkyBlueLight

data class StatusVisual(
    val label: String,
    val shortLabel: String,
    val backgroundColor: Color,
    val textColor: Color,
    val borderColor: Color
)

fun getStatusVisual(status: String): StatusVisual {
    return when (status.lowercase()) {
        "pending" -> StatusVisual(
            label = "Pending - Awaiting Transport Review",
            shortLabel = "PENDING",
            backgroundColor = AmberLight,
            textColor = AmberWarning,
            borderColor = AmberWarning.copy(alpha = 0.5f)
        )
        "transport_verified" -> StatusVisual(
            label = "Transport Verified - Awaiting Principal Approval",
            shortLabel = "TRANSPORT VERIFIED",
            backgroundColor = SkyBlueLight,
            textColor = SkyBlue,
            borderColor = SkyBlue.copy(alpha = 0.5f)
        )
        "transport_rejected" -> StatusVisual(
            label = "Rejected by Transport In-Charge",
            shortLabel = "TRANSPORT REJECTED",
            backgroundColor = CrimsonLight,
            textColor = CrimsonRed,
            borderColor = CrimsonRed.copy(alpha = 0.5f)
        )
        "approved" -> StatusVisual(
            label = "Approved - Pass Issued",
            shortLabel = "APPROVED",
            backgroundColor = EmeraldLight,
            textColor = EmeraldGreen,
            borderColor = EmeraldGreen.copy(alpha = 0.5f)
        )
        "principal_rejected" -> StatusVisual(
            label = "Rejected by Principal",
            shortLabel = "PRINCIPAL REJECTED",
            backgroundColor = CrimsonLight,
            textColor = CrimsonRed,
            borderColor = CrimsonRed.copy(alpha = 0.5f)
        )
        else -> StatusVisual(
            label = status,
            shortLabel = status.uppercase(),
            backgroundColor = Color.LightGray.copy(alpha = 0.3f),
            textColor = Color.DarkGray,
            borderColor = Color.Gray
        )
    }
}

@Composable
fun StatusBadge(
    status: String,
    modifier: Modifier = Modifier,
    useFullLabel: Boolean = false
) {
    val visual = getStatusVisual(status)
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(visual.backgroundColor)
            .border(1.dp, visual.borderColor, RoundedCornerShape(20.dp))
            .padding(horizontal = 10.dp, vertical = 5.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            val icon = when (status.lowercase()) {
                "pending" -> Icons.Default.HourglassEmpty
                "transport_verified" -> Icons.Default.VerifiedUser
                "approved" -> Icons.Default.CheckCircle
                else -> Icons.Default.Close
            }
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = visual.textColor,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = if (useFullLabel) visual.label else visual.shortLabel,
                color = visual.textColor,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
