@file:OptIn(kotlinx.serialization.InternalSerializationApi::class)

package com.sentinel.mobile.models

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@Serializable
data class SupportTicket(
    val id: String,
    val subject: String,
    val description: String,
    val status: TicketStatus,
    val priority: String,
    val createdAt: String
)

@Serializable
@Suppress("unused") // ✅ Silences "RESOLVED/CLOSED is never used"
enum class TicketStatus {
    OPEN, IN_PROGRESS, RESOLVED, CLOSED
}

@Serializable
data class CreateTicketRequest(
    val subject: String,
    val description: String,
    val priority: String = "NORMAL"
)