package com.sentinel.mobile.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sentinel.mobile.api.ApiClient
import com.sentinel.mobile.api.ChatLogDto
import com.sentinel.mobile.api.SendMessageRequest
import com.sentinel.mobile.auth.SessionManager
import com.sentinel.mobile.models.ChatMessage
import com.sentinel.mobile.models.UserRole
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class PrivateChatViewModel(application: Application) : AndroidViewModel(application) {

    private val sessionManager = SessionManager(application.applicationContext)
    private val apiService = ApiClient.service

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private var currentPartnerId: String? = null

    fun initChat(partnerId: String) {
        if (currentPartnerId == partnerId) return
        currentPartnerId = partnerId
        _messages.value = emptyList()
        startPolling()
    }

    private fun startPolling() {
        viewModelScope.launch {
            _isLoading.value = true
            while (isActive && currentPartnerId != null) {
                try {
                    fetchMessages()
                } catch (e: CancellationException) {
                    Log.i("ChatVM", "Polling was cancelled.")
                    break
                } catch (e: Exception) {
                    Log.e("ChatVM", "Message sync error: ${e.message}")
                }
                delay(4000)
            }
        }
    }

    private suspend fun fetchMessages() {
        val token = sessionManager.getToken() ?: return
        val partnerId = currentPartnerId ?: return
        val myId = sessionManager.getUserId() ?: return

        val filter = "or(and(sender_id.eq.$myId,receiver_id.eq.$partnerId),and(sender_id.eq.$partnerId,receiver_id.eq.$myId))"

        try {
            // ✅ This now correctly calls the API with the required 'select' parameter.
            val response = apiService.getChatHistory(
                token = "Bearer $token",
                select = "*,sender_profile:sender_id(full_name,role)", // Fetch sender profile
                orFilter = filter
            )

            if (response.isSuccessful) {
                val dtos = response.body() ?: emptyList()
                // ✅ This mapping logic is now correct and will not produce errors.
                val uiMessages = dtos.map { dto ->
                    dto.toChatMessage(myId)
                }
                _messages.value = uiMessages.sortedBy { it.createdAt }
            }
        } finally {
            _isLoading.value = false
        }
    }

    fun sendMessage(content: String) {
        if (content.isBlank()) return
        val token = sessionManager.getToken() ?: return
        val partnerId = currentPartnerId ?: return
        val myId = sessionManager.getUserId() ?: return

        viewModelScope.launch {
            val request = SendMessageRequest(senderId = myId, receiverId = partnerId, content = content)
            try {
                val response = apiService.sendMessage(token = "Bearer $token", message = request)
                if (response.isSuccessful) {
                    fetchMessages() // Refresh immediately
                }
            } catch (e: Exception) {
                Log.e("ChatVM", "Failed to send message", e)
            }
        }
    }
}

/**
 * ✅ NEW & CORRECTED: This mapper function now correctly converts the corrected
 * ChatLogDto to your internal ChatMessage model.
 */
fun ChatLogDto.toChatMessage(currentUserId: String): ChatMessage {
    val senderName = this.senderProfile?.fullName ?: "User"
    // Safely convert the role string from the API to your UserRole enum
    val senderRole = when (this.senderProfile?.role?.uppercase()) {
        "ADMIN" -> UserRole.ADMIN
        "MODERATOR" -> UserRole.MODERATOR
        "PREMIUM" -> UserRole.PREMIUM
        else -> UserRole.MEMBER
    }

    return ChatMessage(
        id = this.id,
        senderId = this.senderId,
        receiverId = this.receiverId,
        content = this.content,
        createdAt = this.createdAt ?: "1970-01-01T00:00:00Z", // Provide a safe default
        isFromMe = this.senderId == currentUserId,
        senderName = senderName,
        senderRole = senderRole
    )
}
