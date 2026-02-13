package com.sentinel.mobile.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sentinel.mobile.models.UserRole

@Composable
fun RoleBadge(role: UserRole) {
    val color = when (role) {
        UserRole.ADMIN -> Color.Red
        UserRole.USER -> Color.Blue
        UserRole.GUEST -> Color.Gray
        UserRole.MODERATOR -> Color(0xFF9C27B0) // Purple for Moderator
    }
    Surface(
        color = color.copy(alpha = 0.1f),
        border = BorderStroke(1.dp, color),
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = role.name,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
            style = MaterialTheme.typography.labelSmall,
            color = color
        )
    }
}