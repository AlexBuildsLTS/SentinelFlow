package com.sentinel.mobile.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sentinel.mobile.api.ApiClient
import com.sentinel.mobile.auth.SessionManager
import com.sentinel.mobile.models.Transaction
import com.sentinel.mobile.models.TransactionType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// ✅ Changed to AndroidViewModel to get 'application' context automatically
class LiveMonitorViewModel(application: Application) : AndroidViewModel(application) {

    private val sessionManager = SessionManager(application.applicationContext)

    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
    val transactions: StateFlow<List<Transaction>> = _transactions.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun startMonitoring() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val token = sessionManager.getToken()

                if (token != null) {
                    // Call API (Ensure your Token string has "Bearer " if your API needs it,
                    // otherwise just pass 'token' if you handle "Bearer" in the Interface)
                    val response = ApiClient.service.getTransactions("Bearer $token")

                    if (response.isSuccessful && response.body() != null) {
                        val rawList = response.body()!!

                        // Map Supabase JSON to UI Model
                        val uiList = rawList.map { apiTx ->
                            Transaction(
                                id = apiTx.id,
                                amount = apiTx.amount,
                                currency = apiTx.currency,
                                status = apiTx.status,
                                timestamp = apiTx.createdAt.take(10), // Simple date format
                                merchantName = apiTx.merchantName,
                                type = TransactionType.EXPENSE,
                                category = "General"
                            )
                        }
                        _transactions.value = uiList
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}