package com.sentinel.mobile.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sentinel.mobile.models.ChatMessage

/**
 * Standard UI component for displaying individual chat messages.
 * Synchronized with the UnifiedChatBubble logic in your main screens.
 */
@Composable
fun UnifiedChatBubble(message: ChatMessage) {
    // Determine alignment and bubble color based on sender identity
    val alignment = if (message.isFromMe) Alignment.CenterEnd else Alignment.CenterStart
    val bubbleColor = if (message.isFromMe) Color(0xFF0284C7) else Color(0xFF1E293B)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        contentAlignment = alignment
    ) {
        Surface(
            color = bubbleColor,
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (message.isFromMe) 16.dp else 2.dp,
                bottomEnd = if (message.isFromMe) 2.dp else 16.dp
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                // Main text content of the message
                Text(
                    text = message.content,
                    color = Color.White,
                    fontSize = 14.sp
                )

                // Timestamp aligned to the bottom-right of the bubble
                Text(
                    text = message.timestamp,
                    color = Color.White.copy(alpha = 0.5f),
                    fontSize = 10.sp,
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(top = 4.dp)
                )
            }
        }
    }
}

/**
 * Legacy alias for UnifiedChatBubble to ensure backward compatibility
 * with existing PrivateMessagesScreen code.
 */
@Composable
fun ChatBubble(message: ChatMessage) {
    UnifiedChatBubble(message = message)
}