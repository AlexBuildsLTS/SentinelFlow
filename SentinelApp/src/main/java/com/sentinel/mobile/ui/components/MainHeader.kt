/**
 * Sentinel Master Header Module
 * Orchestrates real-time connectivity status and user session identity.
 * Integrates Supabase avatar loading and notification indicators.
 */

package com.sentinel.mobile.ui.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight // ✅ Fixed: Resolved Weight Reference
import androidx.compose.ui.unit.*
import coil.compose.AsyncImage

@Composable
fun MainHeader(
    userEmail: String,
    avatarUrl: String?,
    onOpenMenu: () -> Unit,
    onNavigate: (String) -> Unit
) {
    var showDropdown by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF020617))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onOpenMenu) {
            Icon(Icons.Default.Menu, "Menu", tint = Color.White)
        }

        // Live Feed Status Badge (Visual Parity: sentintenlivefeedon.png)
        Surface(
            color = Color(0xFF064E3B),
            shape = RoundedCornerShape(50),
            border = BorderStroke(1.dp, Color(0xFF10B981).copy(alpha = 0.4f))
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Bolt, null, tint = Color(0xFF10B981), modifier = Modifier.size(14.dp))
                Spacer(Modifier.width(6.dp))
                Text("LIVE FEED ON", color = Color(0xFF10B981), fontSize = 10.sp, fontWeight = FontWeight.ExtraBold)
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            // High-Tier Notification Bell with Red dot indicator (cite: headericon.png)
            Box(modifier = Modifier.padding(end = 12.dp)) {
                Icon(Icons.Default.Notifications, null, tint = Color.White)
                Surface(
                    color = Color.Red,
                    shape = CircleShape,
                    modifier = Modifier
                        .size(8.dp)
                        .align(Alignment.TopEnd)
                        .border(1.dp, Color(0xFF020617), CircleShape)
                ) {}
            }

            // User Identity Module with Supabase Storage Support
            Box {
                Surface(
                    onClick = { showDropdown = true },
                    shape = CircleShape,
                    color = Color(0xFF6366F1),
                    modifier = Modifier.size(36.dp)
                ) {
                    if (avatarUrl != null) {
                        AsyncImage(
                            model = avatarUrl,
                            contentDescription = "Profile",
                            modifier = Modifier.fillMaxSize().clip(CircleShape)
                        )
                    } else {
                        Text(
                            text = userEmail.take(1).uppercase(),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.wrapContentSize()
                        )
                    }
                }

                // Identity Context Menu (Visual Parity: image_d05ed7.png)
                DropdownMenu(
                    expanded = showDropdown,
                    onDismissRequest = { showDropdown = false },
                    modifier = Modifier.background(Color(0xFF0F172A)).border(1.dp, Color.White.copy(alpha = 0.1f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(userEmail, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("Authenticated Session", color = Color.Gray, fontSize = 11.sp)
                    }
                    HorizontalDivider(color = Color.White.copy(alpha = 0.1f))

                    DropdownMenuItem(
                        text = { Text("My Profile", color = Color.White) },
                        leadingIcon = { Icon(Icons.Default.AccountCircle, null, tint = Color.Gray) },
                        onClick = { onNavigate("profile"); showDropdown = false }
                    )
                    DropdownMenuItem(
                        text = { Text("Settings", color = Color.White) },
                        leadingIcon = { Icon(Icons.Default.Settings, null, tint = Color.Gray) },
                        onClick = { onNavigate("settings"); showDropdown = false }
                    )
                }
            }
        }
    }
}