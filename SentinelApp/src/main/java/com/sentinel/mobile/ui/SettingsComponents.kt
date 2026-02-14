package com.sentinel.mobile.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ✅ Reusable header, no longer private
@Composable
fun SettingsSectionHeader(title: String) {
    Text(
        text = title.uppercase(),
        color = Color(0xFF0EA5E9),
        fontSize = 11.sp,
        letterSpacing = 1.sp,
        fontWeight = FontWeight.Black,
        modifier = Modifier.padding(start = 4.dp, bottom = 12.dp)
    )
}

// ✅ Base layout for all setting rows to reduce duplication
@Composable
fun SettingsItem(
    title: String,
    sub: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    trailingContent: @Composable (() -> Unit)? = null
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B).copy(alpha = 0.4f)),
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
    ) {
        Row(modifier = Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(color = Color(0xFF0F172A), shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)) {
                Icon(icon, null, tint = Color(0xFF0EA5E9), modifier = Modifier
                    .padding(12.dp)
                    .size(22.dp))
            }
            Column(modifier = Modifier
                .padding(start = 16.dp)
                .weight(1f)) {
                Text(title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text(sub, color = Color.Gray, fontSize = 12.sp)
            }
            if (trailingContent != null) {
                trailingContent()
            }
        }
    }
}

// ✅ Built using SettingsItem
@Composable
fun SettingsCard(
    title: String,
    sub: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    SettingsItem(
        title = title,
        sub = sub,
        icon = icon,
        modifier = Modifier.clickable { onClick() },
        trailingContent = {
            Icon(Icons.Default.ChevronRight, null, tint = Color.DarkGray)
        }
    )
}

// ✅ Built using SettingsItem, now stateless
@Composable
fun SettingsToggleCard(
    title: String,
    sub: String,
    icon: ImageVector,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    SettingsItem(
        title = title,
        sub = sub,
        icon = icon,
        trailingContent = {
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color(0xFF0EA5E9),
                    checkedTrackColor = Color(0xFF0EA5E9).copy(alpha = 0.3f),
                    uncheckedBorderColor = Color.Transparent
                )
            )
        }
    )
}
