package com.sentinel.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.sentinel.mobile.models.security.BiometricAuthenticator
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// These classes define the state and events for the UI.
// They belong here with the ViewModel.
enum class LockState {
    LOCKED,
    UNLOCKED,
    ERROR
}

sealed class UiEvent {
    object ShowBiometricPrompt : UiEvent()
}

// The ViewModel now correctly accepts BiometricAuthenticator in its constructor.
class MainViewModel(
    private val authenticator: BiometricAuthenticator
) : ViewModel() {

    private val _lockState = MutableStateFlow(LockState.LOCKED)
    val lockState = _lockState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun onUnlockClicked() {
        viewModelScope.launch {
            if (authenticator.isBiometricAvailable()) {
                _uiEvent.emit(UiEvent.ShowBiometricPrompt)
            } else {
                // For emulators without biometrics, unlock immediately for testing.
                _lockState.value = LockState.UNLOCKED
            }
        }
    }

    fun onBiometricAuthenticationResult(successful: Boolean) {
        if (successful) {
            _lockState.value = LockState.UNLOCKED
        } else {
            _lockState.value = LockState.ERROR
        }
    }
}

// A factory is needed to create the ViewModel with its dependency.
@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(
    private val authenticator: BiometricAuthenticator
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(authenticator) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
