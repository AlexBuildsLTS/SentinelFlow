package com.sentinel.mobile.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ✅ CRITICAL IMPORTS - These resolve the 'Unresolved reference' errors
import com.sentinel.mobile.models.SupportTicket
import com.sentinel.mobile.models.TicketStatus

@Composable
fun TicketItem(ticket: SupportTicket, onClaim: (String) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B).copy(alpha = 0.6f)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CalendarMonth, null, tint = Color(0xFFEF4444), modifier = Modifier.size(14.dp))
                    Text(
                        text = " TICKET #${ticket.id.take(4).uppercase()}",
                        color = Color.Gray,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Surface(
                    color = if (ticket.status == TicketStatus.OPEN) Color(0xFF7F1D1D) else Color(0xFF1E3A8A),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = ticket.status.name,
                        color = if (ticket.status == TicketStatus.OPEN) Color(0xFFF87171) else Color(0xFF93C5FD),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Text(
                text = ticket.subject,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 12.dp)
            )

            Text(
                text = ticket.description,
                color = Color.Gray,
                fontSize = 13.sp,
                modifier = Modifier.padding(top = 4.dp)
            )

            Button(
                onClick = { onClaim(ticket.id) },
                modifier = Modifier.fillMaxWidth().padding(top = 20.dp).height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F172A)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Process Ticket", color = Color(0xFF38BDF8), fontWeight = FontWeight.Bold)
            }
        }
    }
}