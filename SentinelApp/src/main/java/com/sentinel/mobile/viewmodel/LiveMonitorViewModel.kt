package com.sentinel.mobile.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sentinel.mobile.api.ApiClient
import com.sentinel.mobile.auth.SessionManager
import com.sentinel.mobile.models.Transaction
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LiveMonitorViewModel(application: Application) : AndroidViewModel(application) {

    private val sessionManager = SessionManager(application.applicationContext)

    // The Source of Truth for the UI
    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
    val transactions: StateFlow<List<Transaction>> = _transactions.asStateFlow()

    // Loading State for Glassmorphic Spinners
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        // Start the engine immediately on launch
        startLiveMonitor()
    }

    private fun startLiveMonitor() {
        viewModelScope.launch {
            _isLoading.value = true
            while (true) {
                try {
                    fetchLatestData()
                } catch (e: Exception) {
                    Log.e("SentinelLive", "Sync Failure: ${e.message}")
                }
                // Wait 5 seconds before next pulse (Heartbeat)
                delay(5000)
            }
        }
    }

    private suspend fun fetchLatestData() {
        val token = sessionManager.getToken()

        // If no user is logged in, we can't fetch secure data
        if (token.isNullOrEmpty()) {
            _isLoading.value = false
            return
        }

        // Execute Network Request
        val response = ApiClient.service.getTransactions("Bearer $token")

        if (response.isSuccessful && response.body() != null) {
            val newData = response.body()!!

            // Only update StateFlow if data has changed (prevents UI flicker)
            if (_transactions.value != newData) {
                _transactions.value = newData
                Log.d("SentinelLive", "Pulse: Updated ${newData.size} transactions")
            }
        } else {
            Log.w("SentinelLive", "API Error: ${response.code()} - ${response.errorBody()?.string()}")
        }

        _isLoading.value = false
    }

    // Helper to calculate Risk Score for the Dashboard Gauge
    fun getAverageRiskScore(): Double {
        val list = _transactions.value
        if (list.isEmpty()) return 0.0
        return list.mapNotNull { it.riskScore }.average()
    }
}