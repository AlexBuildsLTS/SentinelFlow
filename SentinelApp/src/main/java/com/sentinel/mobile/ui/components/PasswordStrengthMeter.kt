/**
 * SentinelFlow Password Security Module
 * Provides real-time entropy analysis for high-tier registration.
 * Matches visual requirements for secure account initialization.
 */

package com.sentinel.mobile.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PasswordStrengthMeter(password: String) {
    // Logic to calculate 0.0 to 1.0 strength based on input complexity
    val strength = remember(password) {
        when {
            password.isEmpty() -> 0f
            password.length < 6 -> 0.2f
            password.any { it.isDigit() } && password.any { it.isUpperCase() } && password.length >= 8 -> 1f
            password.length >= 8 -> 0.7f
            else -> 0.4f
        }
    }

    val color by animateColorAsState(
        targetValue = when {
            strength <= 0.3f -> Color(0xFFEF4444) // Red for Weak
            strength <= 0.7f -> Color(0xFFF59E0B) // Amber for Fair
            else -> Color(0xFF10B981) // Green for Strong
        }, label = "StrengthColor"
    )

    val animatedProgress by animateFloatAsState(targetValue = strength, label = "Progress")

    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Identity Strength", color = Color.Gray, fontSize = 11.sp)
            Text(
                text = when {
                    strength <= 0.3f -> "Weak"
                    strength <= 0.7f -> "Fair"
                    else -> "Strong"
                },
                color = color,
                fontSize = 11.sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }
        LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp)),
            color = color,
            trackColor = Color.White.copy(alpha = 0.1f)
        )
    }
}