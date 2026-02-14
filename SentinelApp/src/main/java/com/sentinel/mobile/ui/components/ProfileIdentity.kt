/**
 * SentinelFlow Profile Identity Module
 * Implements the top-bar dropdown and avatar fetching from Supabase Storage.
 * Matches design specs from image_d05ed7.png.
 */

package com.sentinel.mobile.ui.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import coil.compose.AsyncImage

@Composable
fun ProfileIdentityModule(
    email: String,
    avatarUrl: String?,
    onNavigate: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(contentAlignment = Alignment.TopEnd) {
        // High-Tier Avatar Button
        Surface(
            onClick = { expanded = true },
            shape = CircleShape,
            color = Color(0xFF6366F1), // Custom Indigo from image_d05ed7.png
            modifier = Modifier.size(38.dp)
        ) {
            if (avatarUrl != null) {
                AsyncImage(
                    model = avatarUrl,
                    contentDescription = "Avatar",
                    modifier = Modifier.fillMaxSize().clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Text(
                    text = email.take(1).uppercase(),
                    color = Color.White,
                    modifier = Modifier.wrapContentSize(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }

        // The Dropdown matching image_d05ed7.png
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(Color(0xFF0F172A))
                .border(1.dp, Color.White.copy(alpha = 0.1f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(email, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text("Authenticated Session", color = Color.Gray, fontSize = 10.sp)
            }
            HorizontalDivider(color = Color.White.copy(alpha = 0.1f))

            IdentityMenuItem("My Profile", Icons.Default.Person) { onNavigate("profile"); expanded = false }
            IdentityMenuItem("Settings", Icons.Default.Settings) { onNavigate("settings"); expanded = false }
            IdentityMenuItem("Billing", Icons.Default.CreditCard) { /* Navigate */ ; expanded = false }
        }
    }
}

@Composable
private fun IdentityMenuItem(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    DropdownMenuItem(
        text = { Text(label, color = Color.White, fontSize = 14.sp) },
        leadingIcon = { Icon(icon, null, tint = Color.Gray, modifier = Modifier.size(18.dp)) },
        onClick = onClick
    )
}