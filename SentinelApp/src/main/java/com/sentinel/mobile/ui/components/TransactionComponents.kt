package com.sentinel.mobile.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sentinel.mobile.models.Transaction
import java.util.*

@Composable
fun TransactionListItem(transaction: Transaction) {
    Surface(
        color = Color(0xFF1E293B).copy(alpha = 0.4f),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Risk Indicator Dot
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(
                            color = if ((transaction.riskScore ?: 0.0) > 0.6) Color(0xFFEF4444) else Color(0xFF10B981),
                            shape = androidx.compose.foundation.shape.CircleShape
                        )
                )

                Spacer(Modifier.width(16.dp))

                Column {
                    Text(
                        text = transaction.merchantName ?: "Vault Deposit",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Text(
                        text = transaction.transactionDate.take(10), // Shows YYYY-MM-DD
                        color = Color.Gray,
                        fontSize = 11.sp
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = String.format(Locale.US, "$%.2f", transaction.amount),
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp
                )
                Text(
                    text = "Risk: ${String.format("%.2f", transaction.riskScore ?: 0.0)}",
                    color = if ((transaction.riskScore ?: 0.0) > 0.6) Color(0xFFEF4444) else Color.Gray,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}