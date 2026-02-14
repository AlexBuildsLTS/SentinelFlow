package com.sentinel.mobile.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sentinel.mobile.api.ApiClient
import com.sentinel.mobile.api.LoginRequest
import com.sentinel.mobile.api.ResetRequest
import com.sentinel.mobile.auth.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// This data class holds all the state for the Login Screen UI.
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val isResetting: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val passwordVisible: Boolean = false
)

// This sealed class represents one-time events from the ViewModel to the UI.
sealed class LoginEvent {
    object LoginSuccess : LoginEvent()
}

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    // Use a separate event flow for navigation to avoid re-triggering on config change.
    private val _loginEvent = MutableStateFlow<LoginEvent?>(null)
    val loginEvent = _loginEvent.asStateFlow()

    private val sessionManager = SessionManager(application)
    private val apiService = ApiClient.service

    fun onEmailChanged(email: String) {
        _uiState.update { it.copy(email = email, errorMessage = null) }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update { it.copy(password = password, errorMessage = null) }
    }

    fun onTogglePasswordVisibility() {
        _uiState.update { it.copy(passwordVisible = !it.passwordVisible) }
    }

    fun onLoginClicked() {
        if (_uiState.value.email.isBlank() || _uiState.value.password.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Credentials required.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val request = LoginRequest(_uiState.value.email, _uiState.value.password)
                val response = apiService.login(request)

                if (response.isSuccessful && response.body() != null) {
                    val authData = response.body()!!
                    sessionManager.saveSession(
                        token = authData.accessToken,
                        userId = authData.user.id
                    )
                    _loginEvent.value = LoginEvent.LoginSuccess
                } else {
                    val errorMsg = when (response.code()) {
                        400, 401 -> "Access Denied: Invalid credentials."
                        else -> "Login failed: ${response.message()}"
                    }
                    _uiState.update { it.copy(errorMessage = errorMsg) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "Connection failed. Check network.") }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onForgotPasswordClicked() {
        if (_uiState.value.email.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Please enter your email first to reset.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isResetting = true, errorMessage = null, successMessage = null) }
            try {
                val response = apiService.resetPassword(request = ResetRequest(_uiState.value.email))
                if (response.isSuccessful) {
                    _uiState.update { it.copy(successMessage = "Reset link sent to ${_uiState.value.email}") }
                } else {
                    _uiState.update { it.copy(errorMessage = "Reset failed. Check email address.") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "Network error: ${e.localizedMessage}") }
            } finally {
                _uiState.update { it.copy(isResetting = false) }
            }
        }
    }

    // Call this to signal that the navigation event has been handled.
    fun onLoginEventHandled() {
        _loginEvent.value = null
    }
}
