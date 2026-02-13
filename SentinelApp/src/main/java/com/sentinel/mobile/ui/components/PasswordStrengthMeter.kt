package com.sentinel.mobile.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PasswordStrengthMeter(strength: Float) {
    val (label, color) = when {
        strength < 0.3f -> "Weak" to Color.Red
        strength < 0.7f -> "Medium" to Color(0xFFFFA500)
        else -> "Strong" to Color.Green
    }

    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        LinearProgressIndicator(
            progress = { strength },
            modifier = Modifier.fillMaxWidth().height(8.dp),
            color = color,
            trackColor = Color.LightGray,
            strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
        )
        Text(
            text = "Strength: $label",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = color,
            modifier = Modifier.align(Alignment.End).padding(top = 4.dp)
        )
    }
}