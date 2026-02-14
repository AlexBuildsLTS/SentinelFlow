package com.sentinel.mobile.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.sentinel.mobile.models.Transaction // ✅ Corrected Import
import java.util.Locale

@Composable
fun TransactionListItem(transaction: Transaction) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(Color(0xFF1E293B).copy(alpha = 0.2f), RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            color = Color(0xFF0F172A),
            shape = CircleShape,
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                Icons.Default.AccountBalanceWallet,
                null,
                tint = Color(0xFF0EA5E9),
                modifier = Modifier.padding(10.dp)
            )
        }
        Column(modifier = Modifier.padding(start = 12.dp).weight(1f)) {
            Text(
                text = transaction.merchantName ?: "Unknown Merchant",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
            Text(text = "Sentinel Pulse", color = Color.Gray, fontSize = 12.sp)
        }
        Text(
            text = "$${String.format(Locale.US, "%.2f", transaction.amount)}", // ✅ Locale Fixed
            color = Color.White,
            fontWeight = FontWeight.Black,
            fontSize = 16.sp
        )
    }
}