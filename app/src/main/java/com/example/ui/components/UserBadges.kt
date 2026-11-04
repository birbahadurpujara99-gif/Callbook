package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Security
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
import com.example.localization.AppLanguage
import com.example.localization.CallbookStrings

val BlueBadgeColor = Color(0xFF2563EB)
val GreenAdminColor = Color(0xFF10B981)

@Composable
fun VerifiedBadge(
    modifier: Modifier = Modifier,
    size: androidx.compose.ui.unit.Dp = 16.dp
) {
    Icon(
        imageVector = Icons.Default.CheckCircle,
        contentDescription = "Verified User",
        tint = BlueBadgeColor,
        modifier = modifier.size(size)
    )
}

@Composable
fun AdminBadge(
    language: AppLanguage,
    modifier: Modifier = Modifier,
    showLabel: Boolean = false
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(GreenAdminColor.copy(alpha = 0.15f))
            .padding(horizontal = 6.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Security,
            contentDescription = "Admin Badge",
            tint = GreenAdminColor,
            modifier = Modifier.size(14.dp)
        )
        if (showLabel) {
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = CallbookStrings.adminBadge(language),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = GreenAdminColor
            )
        }
    }
}

@Composable
fun UserNameWithBadges(
    name: String,
    isVerified: Boolean,
    role: String,
    language: AppLanguage,
    modifier: Modifier = Modifier,
    style: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.titleMedium,
    color: Color = MaterialTheme.colorScheme.onSurface
) {
    val isAdmin = role == "admin" || name.equals("Bir Pujara", ignoreCase = true)

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = name.ifBlank { "User" },
            style = style,
            fontWeight = FontWeight.Bold,
            color = color
        )

        // Green Admin Badge for Bir Pujara
        if (isAdmin) {
            Spacer(modifier = Modifier.width(6.dp))
            AdminBadge(language = language, showLabel = true)
        }

        // Blue Verification Badge for Verified Users
        if (isVerified) {
            Spacer(modifier = Modifier.width(6.dp))
            VerifiedBadge(size = 18.dp)
        }
    }
}
