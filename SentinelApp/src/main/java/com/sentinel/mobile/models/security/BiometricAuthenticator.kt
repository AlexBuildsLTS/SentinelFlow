package com.sentinel.mobile.models.security

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat

// ✅ CRITICAL FIX: Wrap the logic in a class so it can be instantiated and passed to the ViewModel.
class BiometricAuthenticator(private val context: Context) {

    /**
     * Checks if strong biometric authentication is available and enrolled on the device.
     * @return True if biometrics are ready to use, false otherwise.
     */
    fun isBiometricAvailable(): Boolean {
        val biometricManager = BiometricManager.from(context)
        // Checks if the device has strong biometric hardware and if the user has enrolled.
        val canAuthenticate = biometricManager.canAuthenticate(BIOMETRIC_STRONG)
        return canAuthenticate == BiometricManager.BIOMETRIC_SUCCESS
    }

    /**
     * Shows the OS-provided biometric prompt to the user.
     *
     * @param activity The activity that will host the prompt dialog.
     * @param onSuccess Callback triggered when the user successfully authenticates.
     * @param onError Callback triggered when an unrecoverable error occurs or the user cancels.
     */
    fun promptBiometric(
        activity: AppCompatActivity,
        onSuccess: () -> Unit,
        onError: (errorCode: Int, errString: CharSequence) -> Unit,
        onFailed: () -> Unit
    ) {
        val executor = ContextCompat.getMainExecutor(context)

        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                onError(errorCode, errString)
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                onSuccess()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                onFailed()
            }
        }

        val biometricPrompt = BiometricPrompt(activity, executor, callback)

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("SentinelApp Authentication")
            .setSubtitle("Confirm your identity to proceed")
            .setNegativeButtonText("Cancel")
            .setConfirmationRequired(false)
            .build()

        biometricPrompt.authenticate(promptInfo)
    }
}
