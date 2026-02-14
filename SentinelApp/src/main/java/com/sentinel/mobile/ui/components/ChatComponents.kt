package com.sentinel.mobile.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sentinel.mobile.models.ChatMessage
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * Standard UI component for displaying individual chat messages.
 * This version includes improved timestamp formatting.
 */
@Composable
fun UnifiedChatBubble(message: ChatMessage) {
    // Determine alignment and bubble color based on sender identity
    val alignment = if (message.isFromMe) Alignment.CenterEnd else Alignment.CenterStart
    val bubbleColor = if (message.isFromMe) Color(0xFF0284C7) else Color(0xFF1E293B)
    val bubbleShape = RoundedCornerShape(
        topStart = 16.dp,
        topEnd = 16.dp,
        bottomStart = if (message.isFromMe) 16.dp else 2.dp,
        bottomEnd = if (message.isFromMe) 2.dp else 16.dp
    )

    // ✅ NEW: Formatter for displaying a human-readable time
    val formatter = remember { DateTimeFormatter.ofPattern("hh:mm a").withZone(ZoneId.systemDefault()) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp),
        contentAlignment = alignment
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(0.8f), // Constrain max width
            color = bubbleColor,
            shape = bubbleShape
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
                // ✅ NEW FEATURE: Show sender's name for group chats or non-user messages
                if (!message.isFromMe) {
                    Text(
                        text = message.senderName,
                        color = Color(0xFF0EA5E9),
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }

                // Main text content of the message
                Text(
                    text = message.content,
                    color = Color.White,
                    fontSize = 15.sp,
                    lineHeight = 22.sp
                )

                // Timestamp aligned to the bottom-right of the bubble
                Text(
                    // ✅ FIXED: Parse and format the timestamp for display
                    text = try { formatter.format(Instant.parse(message.createdAt)) } catch (e: Exception) { "sending..." },
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 11.sp,
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(top = 6.dp)
                )
            }
        }
    }
}

/**
 * Legacy alias for UnifiedChatBubble to ensure backward compatibility.
 */
@Composable
fun ChatBubble(message: ChatMessage) {
    UnifiedChatBubble(message = message)
}
