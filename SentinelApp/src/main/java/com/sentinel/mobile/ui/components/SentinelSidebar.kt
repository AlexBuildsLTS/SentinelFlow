/**
 * SentinelFlow Navigation Sidebar
 * Implements high-tier branding and real-time status indicators.
 * Matches design specs from sentintelintro.png and sentintelgreenlivefeed.png.
 */

package com.sentinel.mobile.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SentinelSidebar(onNavigate: (String) -> Unit, onLogout: () -> Unit) {
    ModalDrawerSheet(
        // Updated to drawerContainerColor to fix Material3 compilation error
        drawerContainerColor = Color(0xFF020617),
        drawerContentColor = Color.White
    ) {
        Column(modifier = Modifier.fillMaxHeight().padding(24.dp)) {
            // Header with App Icon and Versioning
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    color = Color(0xFF3B82F6),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.size(42.dp)
                ) {
                    Icon(Icons.Default.Shield, "Icon", tint = Color.White, modifier = Modifier.padding(8.dp))
                }
                Column(modifier = Modifier.padding(start = 12.dp)) {
                    Text("SentinelFlow", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text("Audit Engine v1.0", color = Color.Gray, fontSize = 11.sp)
                }
            }

            Spacer(Modifier.height(40.dp))

            // Sidebar Links with Real-time Status Badges
            SidebarLink("Dashboard", Icons.Default.Dashboard, "dashboard", onNavigate)
            SidebarLink("Live Monitor", Icons.Default.Timeline, "dashboard", onNavigate, badge = "LIVE")
            SidebarLink("SentinelAI", Icons.Default.SmartToy, "ai_assistant", onNavigate, badge = "BETA")

            Spacer(Modifier.weight(1f))

            SidebarLink("Settings", Icons.Default.Settings, "settings", onNavigate)

            // Dynamic User Session Identity Snapshot
            Surface(
                color = Color(0xFF1E293B).copy(alpha = 0.5f),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
            ) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Surface(color = Color(0xFF6366F1), shape = RoundedCornerShape(8.dp), modifier = Modifier.size(32.dp)) {
                        Text("A", color = Color.White, modifier = Modifier.wrapContentSize(), fontWeight = FontWeight.Bold)
                    }
                    Column(modifier = Modifier.padding(start = 12.dp)) {
                        Text("Admin User", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Text("Root Access", color = Color.Gray, fontSize = 10.sp)
                    }
                }
            }

            // Session Termination Action
            TextButton(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFFEF4444))
            ) {
                Icon(Icons.Default.Logout, null, modifier = Modifier.size(18.dp))
                Text("End Session", modifier = Modifier.padding(start = 8.dp), fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun SidebarLink(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    route: String,
    onNavigate: (String) -> Unit,
    badge: String? = null
) {
    NavigationDrawerItem(
        label = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(label, fontSize = 15.sp)
                if (badge != null) {
                    Surface(
                        color = if (badge == "LIVE") Color(0xFF064E3B) else Color(0xFF1E1B4B),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Text(
                            text = badge,
                            color = if (badge == "LIVE") Color(0xFF10B981) else Color(0xFF8B5CF6),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        },
        selected = false,
        onClick = { onNavigate(route) },
        icon = { Icon(icon, null, modifier = Modifier.size(22.dp)) },
        colors = NavigationDrawerItemDefaults.colors(unselectedContainerColor = Color.Transparent)
    )
}