package com.sentinel.mobile.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage // ✅ Now resolves
import com.sentinel.mobile.models.ChatMessage

@Composable
fun ChatBubble(message: ChatMessage) {
    val isFromMe = message.isFromMe

    Row(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        horizontalArrangement = if (isFromMe) Arrangement.End else Arrangement.Start
    ) {
        if (!isFromMe) {
            AsyncImage(
                model = message.senderAvatar,
                contentDescription = null,
                modifier = Modifier.size(40.dp).clip(CircleShape)
            )
            Spacer(Modifier.width(8.dp))
        }

        Column(horizontalAlignment = if (isFromMe) Alignment.End else Alignment.Start) {
            Text(text = message.senderName, fontSize = 12.sp, color = Color.Gray)
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = if (isFromMe) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer
            ) {
                Text(
                    text = message.content,
                    modifier = Modifier.padding(12.dp),
                    color = if (isFromMe) Color.White else Color.Black
                )
            }
            Text(text = message.timestamp, fontSize = 10.sp, color = Color.LightGray)
        }
    }
}