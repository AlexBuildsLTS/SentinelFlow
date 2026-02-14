package com.sentinel.app

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.sentinel.app.viewmodel.*
import com.sentinel.mobile.models.security.BiometricAuthenticator
import com.sentinel.mobile.ui.SentinelAppRoot
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    // The BiometricAuthenticator is created here, in the Activity context.
    private val authenticator by lazy { BiometricAuthenticator(this) }

    // Use the factory to create the ViewModel instance with its dependency.
    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory(authenticator)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Observe one-time events from the ViewModel to show the biometric prompt.
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiEvent.collectLatest { event ->
                    when (event) {
                        is UiEvent.ShowBiometricPrompt -> {
                            authenticator.promptBiometric(
                                activity = this@MainActivity,
                                onSuccess = { viewModel.onBiometricAuthenticationResult(true) },
                                onError = { _, _ -> viewModel.onBiometricAuthenticationResult(false) },
                                onFailed = { /* Optional: Show a "try again" message */ }
                            )
                        }
                    }
                }
            }
        }

        setContent {
            // Observe the UI state from the ViewModel.
            val lockState by viewModel.lockState.collectAsStateWithLifecycle()

            // Trigger the unlock attempt only once when the UI is first composed.
            LaunchedEffect(Unit) {
                viewModel.onUnlockClicked()
            }

            // The UI now simply reacts to the state from the ViewModel.
            when (lockState) {
                LockState.UNLOCKED -> {
                    SentinelAppRoot() // Your main app UI
                }
                else -> {
                    // Show a loading/locked screen. You could differentiate between LOCKED and ERROR here.
                    LockedScreen() // Placeholder for your locked UI
                }
            }
        }
    }
}

/**
 * A placeholder composable for the locked screen UI.
 * This resolves the "Unresolved reference 'LockedScreen'" error.
 */
@Composable
fun LockedScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF020617)) // Sentinel Brand Dark
    ) {
        // You can add a loading spinner or a logo here.
    }
}
