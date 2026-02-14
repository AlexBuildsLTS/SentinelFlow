package com.sentinel.mobile.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sentinel.mobile.models.UserRole
import com.sentinel.mobile.models.UserRole.*

@Composable
fun RoleBadge(role: UserRole) {
    val (color, label) = when (role) {
        ADMIN -> Color(0xFFF59E0B) to "SYSTEM ADMIN"
        MODERATOR -> Color(0xFF8B5CF6) to "MODERATOR"
        PREMIUM -> Color(0xFF10B981) to "PREMIUM"
        MEMBER -> Color(0xFF64748B) to "MEMBER"
        USER -> TODO()
        GUEST -> TODO()
    }

    Surface(
        color = color.copy(alpha = 0.1f),
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(0.5.dp, color.copy(alpha = 0.5f))
    ) {
        Text(
            text = label,
            color = color,
            fontSize = 9.sp,
            fontWeight = FontWeight.Black,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}