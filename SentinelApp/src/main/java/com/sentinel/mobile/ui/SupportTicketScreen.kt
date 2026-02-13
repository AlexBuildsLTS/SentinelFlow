package com.sentinel.mobile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack // ✅ CHANGED from AutoMirrored
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sentinel.mobile.models.SupportTicket
import com.sentinel.mobile.models.UserRole

// Theme Colors
val SentinelDarkTheme = Color(0xFF0F172A)
val SentinelCardTheme = Color(0xFF1E293B)
val SentinelAccentTheme = Color(0xFF0EA5E9)

@Composable
fun SupportTicketScreen(
    tickets: List<SupportTicket>,
    currentUserRole: UserRole,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SentinelDarkTheme)
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                // ✅ FIX: Using standard icon to prevent "Unresolved reference"
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Text(
                text = "Support Tickets",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(tickets) { ticket ->
                TicketItem(ticket, currentUserRole)
            }
        }
    }
}

@Composable
fun TicketItem(ticket: SupportTicket, role: UserRole) {
    val priorityColor = when (ticket.priority) {
        "URGENT" -> Color(0xFFEF4444)
        "HIGH" -> Color(0xFFF59E0B)
        else -> SentinelAccentTheme
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = SentinelCardTheme),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.DateRange, null, tint = priorityColor, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("TICKET #${ticket.id.take(8)}", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                Surface(
                    color = priorityColor.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = ticket.status.name,
                        color = priorityColor,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(Modifier.height(12.dp))
            Text(text = ticket.subject, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(text = ticket.description, color = Color.LightGray, fontSize = 14.sp, maxLines = 2)

            Spacer(Modifier.height(16.dp))

            if (role == UserRole.ADMIN || role == UserRole.MODERATOR) {
                Button(
                    onClick = { },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = priorityColor.copy(alpha = 0.2f)),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("Claim & Respond", color = priorityColor, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}