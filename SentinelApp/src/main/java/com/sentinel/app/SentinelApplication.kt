package com.sentinel.app

import android.app.Application
import com.sentinel.mobile.api.ApiClient

/**
 * The single, central entry point for the application process.
 * Initializes app-wide components like ApiClient.
 */
class SentinelApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // ✅ Initialize the API client once for the entire app lifecycle.
        ApiClient.init(this)
    }
}
