package com.sentinel.mobile.ui.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.sentinel.mobile.models.UserRole

@Composable
fun SentinelSidebar(
    onNavigate: (String) -> Unit,
    onLogout: () -> Unit,
    userEmail: String = "admin@sentinel.com",
    userRole: UserRole = UserRole.ADMIN // ✅ Fixed: Uses correct enum
) {
    ModalDrawerSheet(
        drawerContainerColor = Color(0xFF020617),
        drawerShape = RoundedCornerShape(topEnd = 24.dp, bottomEnd = 24.dp),
        modifier = Modifier.width(300.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            // --- HEADER: USER IDENTITY ---
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(contentAlignment = Alignment.Center) {
                    Surface(
                        modifier = Modifier.size(54.dp),
                        shape = CircleShape,
                        color = Color(0xFF6366F1)
                    ) {
                        Text(
                            text = userEmail.take(1).uppercase(),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.wrapContentSize()
                        )
                    }
                }
                Column(modifier = Modifier.padding(start = 12.dp)) {
                    Text(
                        text = userEmail.split("@")[0],
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    // ✅ FIXED: Using custom RoleBadge to kill unresolved errors
                    RoleBadge(role = userRole)
                }
            }

            Spacer(Modifier.height(40.dp))
            HorizontalDivider(color = Color.White.copy(alpha = 0.05f))
            Spacer(Modifier.height(24.dp))

            // --- NAVIGATION ITEMS ---
            Text("COMMAND CENTER", color = Color.Gray, fontSize = 11.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
            Spacer(Modifier.height(16.dp))

            NavItem(Icons.Default.Dashboard, "Executive Dashboard", "dashboard", onNavigate)
            NavItem(Icons.Default.SmartToy, "SentinelAI Engine", "ai_assistant", onNavigate)
            NavItem(Icons.Default.Chat, "Secure Comms", "chat/support-agent-001", onNavigate)
            NavItem(Icons.Default.ConfirmationNumber, "Audit Tickets", "support", onNavigate)

            Spacer(Modifier.weight(1f))

            // --- FOOTER ---
            NavItem(Icons.Default.Settings, "System Settings", "settings", onNavigate)

            Button(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth().height(50.dp).padding(top = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444).copy(alpha = 0.1f)),
                border = BorderStroke(1.dp, Color(0xFFEF4444).copy(alpha = 0.2f)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Logout, null, tint = Color(0xFFEF4444), modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(12.dp))
                Text("Terminals Offline", color = Color(0xFFEF4444), fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun NavItem(
    icon: ImageVector,
    label: String,
    route: String,
    onNavigate: (String) -> Unit
) {
    Surface(
        onClick = { onNavigate(route) },
        color = Color.Transparent,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, null, tint = Color(0xFF94A3B8), modifier = Modifier.size(22.dp))
            Spacer(Modifier.width(16.dp))
            Text(label, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }
    }
}