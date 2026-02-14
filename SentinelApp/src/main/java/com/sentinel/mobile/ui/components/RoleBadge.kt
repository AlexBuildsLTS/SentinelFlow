package com.sentinel.mobile.ui.components

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
import com.sentinel.mobile.models.UserRole // ✅ Corrected Import

@Composable
fun RoleBadge(role: UserRole) {
    val (backgroundColor, textColor) = when (role) {
        UserRole.ADMIN -> Color(0xFF7F1D1D) to Color(0xFFF87171)
        UserRole.MODERATOR -> Color(0xFF1E3A8A) to Color(0xFF93C5FD)
        UserRole.PREMIUM -> Color(0xFF3F6212) to Color(0xFFBEF264)
        UserRole.MEMBER, UserRole.USER -> Color(0xFF1E293B) to Color(0xFF94A3B8)
        UserRole.GUEST -> Color(0xFF334155) to Color(0xFF64748B)
        else -> Color(0xFF1E293B) to Color(0xFF94A3B8) // ✅ Exhaustive Check
    }

    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier.padding(horizontal = 4.dp)
    ) {
        Text(
            text = role.name,
            color = textColor,
            fontSize = 9.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}