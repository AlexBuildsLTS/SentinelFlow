package com.sentinel.mobile.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info // ✅ Standard Icon
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.*
import androidx.compose.ui.unit.dp
import com.sentinel.mobile.models.Transaction

@Composable
fun TransactionIcon() {
    Icon(
        imageVector = Icons.Default.Info, // ✅ Fixed
        contentDescription = "Transaction",
        tint = Color(0xFFFFD700),
        modifier = Modifier.size(24.dp)
    )
}

@Composable
fun TransactionDialog(
    transaction: Transaction,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = { TransactionIcon() },
        title = { Text(text = "Transaction Details") },
        text = {
            Column {
                Text("Merchant: ${transaction.merchantName}")
                Text("Amount: ${transaction.currency} ${transaction.amount}")
                Text("Status: ${transaction.status}")
                Text("Date: ${transaction.timestamp}")
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}