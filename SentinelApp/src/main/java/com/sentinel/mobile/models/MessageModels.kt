@file:OptIn(kotlinx.serialization.InternalSerializationApi::class)

package com.sentinel.mobile.models

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@Serializable
data class ChatMessage(
    val id: String,
    val content: String,
    val timestamp: String,
    val isFromMe: Boolean,
    val senderName: String,
    val senderAvatar: String? = null,
    val senderRole: String? = "User"
)

@Serializable
@Suppress("unused") // ✅ Silences "ChatSession is never used"
data class ChatSession(
    val sessionId: String,
    val participants: List<String>,
    val lastMessage: ChatMessage?
)