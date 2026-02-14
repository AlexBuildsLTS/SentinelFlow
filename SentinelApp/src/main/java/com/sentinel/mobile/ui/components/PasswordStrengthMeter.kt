package com.sentinel.mobile.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PasswordStrengthMeter(password: String) {
    val strength = calculateStrength(password)

    val color by animateColorAsState(
        targetValue = when (strength) {
            0 -> Color.Gray
            1, 2 -> Color(0xFFEF4444) // Red
            3 -> Color(0xFFF59E0B)    // Yellow
            4, 5 -> Color(0xFF10B981) // Green
            else -> Color.Gray
        },
        label = "StrengthColor"
    )

    val label = when (strength) {
        0 -> "Enter Password"
        1, 2 -> "Weak"
        3 -> "Medium"
        4, 5 -> "Strong (Sentinel Approved)"
        else -> ""
    }

    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
        ) {
            Text("Security Level", color = Color.Gray, fontSize = 11.sp)
            Text(label, color = color, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(6.dp))

        // Animated Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .background(Color(0xFF0F172A), RoundedCornerShape(4.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction = (strength / 5f).coerceIn(0.1f, 1f))
                    .height(6.dp)
                    .background(color, RoundedCornerShape(4.dp))
            )
        }
    }
}

private fun calculateStrength(password: String): Int {
    if (password.isEmpty()) return 0
    var score = 0
    if (password.length >= 8) score++
    if (password.any { it.isUpperCase() }) score++
    if (password.any { it.isLowerCase() }) score++
    if (password.any { it.isDigit() }) score++
    if (password.any { !it.isLetterOrDigit() }) score++
    return score
}