package com.sentinel.mobile.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.sentinel.mobile.models.ChatMessage
import com.sentinel.mobile.models.UserRole
import com.sentinel.mobile.ui.components.UnifiedChatBubble

@Composable
fun SentinelAIScreen(onBack: () -> Unit) {
    var userInput by remember { mutableStateOf("") }

    val history = remember {
        mutableStateListOf(
            ChatMessage(
                id = "1",
                content = "Hello! I noticed a 15% increase in \"Server Costs\" this month. Would you like a forecast?",
                timestamp = "10:02 AM",
                isFromMe = false,
                senderName = "SentinelAI",
                senderRole = UserRole.ADMIN
            ),
            ChatMessage(
                id = "2",
                content = "Yes, please analyze the trend.",
                timestamp = "10:03 AM",
                isFromMe = true,
                senderName = "Admin User",
                senderRole = UserRole.ADMIN
            )
        )
    }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFF020617))) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White)
            }
            Surface(color = Color(0xFF1E293B), shape = RoundedCornerShape(12.dp), modifier = Modifier.size(44.dp)) {
                Icon(Icons.Default.SmartToy, null, tint = Color(0xFF8B5CF6), modifier = Modifier.padding(10.dp))
            }
            Column(modifier = Modifier.padding(start = 12.dp).weight(1f)) {
                Text("SentinelAI Assistant", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("Neural Analysis Active", color = Color(0xFF8B5CF6), fontSize = 11.sp, fontWeight = FontWeight.ExtraBold)
            }
        }

        HorizontalDivider(color = Color.White.copy(alpha = 0.05f))

        LazyColumn(
            modifier = Modifier.weight(1f).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 20.dp)
        ) {
            items(history) { msg ->
                UnifiedChatBubble(message = msg)
            }
        }

        Box(modifier = Modifier.padding(16.dp).navigationBarsPadding()) {
            OutlinedTextField(
                value = userInput,
                onValueChange = { userInput = it },
                placeholder = { Text("Ask SentinelAI...", color = Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFF0F172A),
                    focusedContainerColor = Color(0xFF0F172A),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                trailingIcon = {
                    IconButton(
                        onClick = { userInput = "" },
                        modifier = Modifier.background(Color(0xFF8B5CF6), CircleShape).size(36.dp)
                    ) {
                        Icon(Icons.Default.ArrowUpward, null, tint = Color.White)
                    }
                }
            )
        }
    }
}