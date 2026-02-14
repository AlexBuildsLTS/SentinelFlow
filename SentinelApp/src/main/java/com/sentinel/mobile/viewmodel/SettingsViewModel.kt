package com.sentinel.mobile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sentinel.mobile.auth.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// This data class holds all the state for the Settings Screen UI.
data class SettingsUiState(
    val userEmail: String,
    val avatarUrl: String?,
    val userApiKey: String = "",
    val isSaving: Boolean = false,
    val isBiometricEnabled: Boolean = true,
    val is2faEnabled: Boolean = false
)

// One-time events from the ViewModel to the UI, like navigation.
sealed class SettingsEvent {
    object LogoutSuccess : SettingsEvent()
}

class SettingsViewModel(private val sessionManager: SessionManager) : ViewModel() {

    private val _uiState = MutableStateFlow(
        SettingsUiState(
            userEmail = sessionManager.getToken() ?: "user@example.com",
            avatarUrl = sessionManager.getUserId()
        )
    )
    val uiState = _uiState.asStateFlow()

    private val _settingsEvent = MutableStateFlow<SettingsEvent?>(null)
    val settingsEvent = _settingsEvent.asStateFlow()

    // --- Event Handlers ---
    fun onApiKeyChanged(apiKey: String) {
        _uiState.update { it.copy(userApiKey = apiKey) }
    }

    fun onUpdateApiKeyClicked() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            // TODO: Implement API call to save the key
            // For now, simulate a network call
            kotlinx.coroutines.delay(1500)
            _uiState.update { it.copy(isSaving = false) }
        }
    }

    fun onBiometricToggled(isEnabled: Boolean) {
        _uiState.update { it.copy(isBiometricEnabled = isEnabled) }
        // TODO: Save this preference to SharedPreferences
    }

    fun on2faToggled(isEnabled: Boolean) {
        _uiState.update { it.copy(is2faEnabled = isEnabled) }
        // TODO: Save this preference to SharedPreferences
    }

    fun onLogoutClicked() {
        sessionManager.clearSession()
        _settingsEvent.value = SettingsEvent.LogoutSuccess
    }

    fun onEventHandled() {
        _settingsEvent.value = null
    }
}
