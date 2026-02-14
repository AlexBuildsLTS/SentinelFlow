package com.sentinel.mobile.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sentinel.mobile.models.SupportTicket

@Composable
fun TicketItem(
    ticket: SupportTicket,
    isAdmin: Boolean, // ✅ Fixed: Added missing parameter
    onClaim: (String) -> Unit = {} // ✅ Fixed: Added missing parameter
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B).copy(alpha = 0.4f)),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = Color(0xFF0EA5E9).copy(alpha = 0.1f),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    Icons.Default.ConfirmationNumber,
                    null,
                    tint = Color(0xFF0EA5E9),
                    modifier = Modifier.padding(12.dp)
                )
            }

            Column(modifier = Modifier.padding(start = 16.dp).weight(1f)) {
                Text(
                    text = ticket.subject, // ✅ Fixed: Matches DataModels.kt
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = "ID: ${ticket.id.take(8)} • ${ticket.priority}",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }

            // Status Badge
            Surface(
                color = if (ticket.status == "OPEN") Color(0xFF10B981).copy(alpha = 0.1f) else Color.DarkGray,
                shape = RoundedCornerShape(50),
                border = BorderStroke(1.dp, if (ticket.status == "OPEN") Color(0xFF10B981) else Color.Gray)
            ) {
                Text(
                    text = ticket.status,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    color = if (ticket.status == "OPEN") Color(0xFF10B981) else Color.LightGray,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            if (isAdmin && ticket.status == "OPEN") {
                IconButton(onClick = { onClaim(ticket.id) }) {
                    Text("CLAIM", color = Color(0xFF0EA5E9), fontSize = 10.sp, fontWeight = FontWeight.Black)
                }
            }
        }
    }
}