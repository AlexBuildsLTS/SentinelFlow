package com.sentinel.mobile.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sentinel.mobile.api.ApiClient
import com.sentinel.mobile.auth.SessionManager
import com.sentinel.mobile.models.Transaction
import com.sentinel.mobile.models.TransactionType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LiveMonitorViewModel(application: Application) : AndroidViewModel(application) {

    private val sessionManager = SessionManager(application.applicationContext)
    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
    val transactions: StateFlow<List<Transaction>> = _transactions.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        executeFullSync()
    }

    private fun executeFullSync() {
        viewModelScope.launch {
            _isLoading.value = true // ✅ USED: isLoading active
            try {
                val token = sessionManager.getToken() ?: run {
                    _isLoading.value = false
                    return@launch
                }

                val response = ApiClient.service.getTransactions("Bearer $token")
                if (response.isSuccessful && response.body() != null) {
                    val rawList = response.body()!!
                    _transactions.value = rawList.map { apiTx ->
                        Transaction(
                            id = apiTx.id,
                            userId = apiTx.userId, // ✅ RESOLVED: Matches ApiModels.kt property
                            amount = apiTx.amount,
                            currency = apiTx.currency,
                            status = apiTx.status,
                            transactionDate = apiTx.createdAt,
                            merchantName = apiTx.merchantName,
                            type = TransactionType.EXPENSE,
                            category = "Neural Stream Sync",
                            riskScore = apiTx.riskScore
                        )
                    }
                }
                startRealtimeLoop(token)
            } catch (e: Exception) {
                Log.e("SentinelLive", "Sync Error: ${e.localizedMessage}") // ✅ USED: Exception logged
            } finally {
                _isLoading.value = false // ✅ USED: isLoading state reset
            }
        }
    }

    private fun startRealtimeLoop(token: String) {
        viewModelScope.launch {
            while (true) {
                try {
                    val response = ApiClient.service.getTransactions("Bearer $token")
                    val latest = response.body()?.firstOrNull()
                    if (latest != null && _transactions.value.none { it.id == latest.id }) {
                        val newTx = Transaction(
                            id = latest.id,
                            userId = latest.userId, // ✅ RESOLVED: Matches ApiModels.kt property
                            amount = latest.amount,
                            currency = latest.currency,
                            status = latest.status,
                            transactionDate = latest.createdAt,
                            merchantName = latest.merchantName,
                            type = TransactionType.EXPENSE,
                            category = "Live Node Update",
                            riskScore = 0.01
                        )
                        _transactions.value = listOf(newTx) + _transactions.value
                    }
                } catch (e: Exception) { /* Automatic recovery for polling stream */ }
                delay(5000)
            }
        }
    }
}