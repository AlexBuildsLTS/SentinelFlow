package com.sentinel.mobile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sentinel.mobile.ui.components.UnifiedChatBubble
import com.sentinel.mobile.viewmodel.PrivateChatViewModel

@Composable
fun SentinelAIScreen(onBack: () -> Unit) {
    // ⚡ Logic: AI Chat is effectively a chat with a specific System ID
    val chatViewModel: PrivateChatViewModel = viewModel()
    val messages by chatViewModel.messages.collectAsState()
    var userInput by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        chatViewModel.initChat("sentinel-ai-system-id")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF020617))
            .statusBarsPadding()
    ) {
        // --- Header ---
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White)
            }
            Surface(
                color = Color(0xFF8B5CF6).copy(alpha = 0.1f),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.size(44.dp)
            ) {
                Icon(Icons.Default.SmartToy, null, tint = Color(0xFF8B5CF6), modifier = Modifier.padding(10.dp))
            }
            Column(modifier = Modifier.padding(start = 12.dp).weight(1f)) {
                Text("SentinelAI Assistant", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("Neural Analysis Active", color = Color(0xFF8B5CF6), fontSize = 11.sp, fontWeight = FontWeight.ExtraBold)
            }
        }

        HorizontalDivider(color = Color.White.copy(alpha = 0.05f))

        // --- Chat Feed ---
        LazyColumn(
            modifier = Modifier.weight(1f).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 20.dp)
        ) {
            items(messages) { msg ->
                UnifiedChatBubble(message = msg)
            }
        }

        // --- Input ---
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
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color(0xFF8B5CF6)
                ),
                trailingIcon = {
                    IconButton(
                        onClick = {
                            if (userInput.isNotBlank()) {
                                chatViewModel.sendMessage(userInput)
                                userInput = ""
                            }
                        },
                        modifier = Modifier.background(Color(0xFF8B5CF6), RoundedCornerShape(50)).size(36.dp)
                    ) {
                        Icon(Icons.Default.ArrowUpward, null, tint = Color.White)
                    }
                }
            )
        }
    }
}