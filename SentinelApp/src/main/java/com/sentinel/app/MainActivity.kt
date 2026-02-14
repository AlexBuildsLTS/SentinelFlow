package com.sentinel.mobile // ✅ CRITICAL: Matches Manifest package="com.sentinel.mobile"

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
// Imports for your specific components
import com.sentinel.mobile.api.ApiClient
import com.sentinel.mobile.models.security.BiometricAuthenticator
import com.sentinel.mobile.ui.SentinelAppRoot

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Global API State
        try {
            ApiClient.init(applicationContext)
        } catch (e: Exception) {
            e.printStackTrace() // Prevent crash if API fails init
        }

        val authenticator = BiometricAuthenticator(this)

        setContent {
            // Default to unlocked for dev testing if biometric hardware is missing
            var isUnlocked by remember { mutableStateOf(true) }

            // Uncomment this block to re-enable strict biometric locking
            /*
            LaunchedEffect(Unit) {
                if (authenticator.isBiometricAvailable()) {
                    isUnlocked = false
                    authenticator.promptBiometric(
                        activity = this@MainActivity,
                        onSuccess = { isUnlocked = true },
                        onError = {  }
                    )
                }
            }
            */

            if (isUnlocked) {
                SentinelAppRoot() // Loads the full navigation graph above
            } else {
                // Locked State Splash
                Box(modifier = Modifier.fillMaxSize().background(Color(0xFF020617)))
            }
        }
    }
}