package com.sentinel.mobile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sentinel.mobile.models.ChatMessage
import com.sentinel.mobile.ui.components.UnifiedChatBubble

@Composable
fun PrivateMessagesScreen(recipientName: String, onBack: () -> Unit) {
    var messageText by remember { mutableStateOf("") }

    // Mock Data Stream - Replaced by ViewModel in production
    val chatStream = listOf(
        ChatMessage("1", "Secure node initialized.", "09:41 AM", false, recipientName),
        ChatMessage("2", "Awaiting verification.", "09:42 AM", true, "You")
    )

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFF020617))) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White)
            }
            Text(
                text = recipientName,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }

        // Chat List
        LazyColumn(
            modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
            reverseLayout = false
        ) {
            items(chatStream) { msg ->
                UnifiedChatBubble(message = msg)
            }
        }

        // Input Area
        Surface(
            modifier = Modifier.padding(16.dp).navigationBarsPadding(),
            color = Color(0xFF0F172A),
            shape = RoundedCornerShape(28.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = messageText,
                    onValueChange = { messageText = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Encrypted Message", color = Color.Gray) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    )
                )
                IconButton(onClick = { /* Send Logic */ }) {
                    Icon(Icons.AutoMirrored.Filled.Send, null, tint = Color(0xFF0EA5E9))
                }
            }
        }
    }
}