package com.sentinel.mobile.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sentinel.mobile.viewmodel.LiveMonitorViewModel
import com.sentinel.mobile.ui.components.MainHeader
import com.sentinel.mobile.ui.components.TransactionListItem
import com.sentinel.mobile.ui.components.SentinelTrendChart
import com.sentinel.mobile.ui.components.NewTransactionDialog
import java.text.NumberFormat
import java.util.Locale

@Composable
fun DashboardScreen(
    viewModel: LiveMonitorViewModel,
    onOpenMenu: () -> Unit,
    onNavigateToAI: () -> Unit,
    onNavigateToSupport: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onLogout: () -> Unit
) {
    val transactions by viewModel.transactions.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // Dynamic Calculations
    val totalVolume = remember(transactions) { transactions.sumOf { it.amount } }
    val avgRisk = remember(transactions) { viewModel.getAverageRiskScore() }
    val formattedVolume = NumberFormat.getCurrencyInstance(Locale.US).format(totalVolume)

    Scaffold(
        containerColor = Color(0xFF020617),
        topBar = {
            MainHeader(
                userEmail = "admin@sentinel.com",
                avatarUrl = null,
                onOpenMenu = onOpenMenu,
                onNavigate = { route ->
                    if (route == "settings") onNavigateToSettings()
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                "Executive Overview",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier.padding(top = 24.dp)
            )

            Spacer(Modifier.height(24.dp))

            // Main Volume Card
            StatCard(
                label = "Total Vault Volume",
                value = formattedVolume,
                trend = if (isLoading) "SYNCING..." else "SECURE",
                chartData = listOf(20f, 50f, 30f, 80f, 60f, 90f),
                isPositive = true
            )

            Spacer(Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(modifier = Modifier.weight(1f)) {
                    StatCard(
                        label = "Audit Events",
                        value = "${transactions.size}",
                        trend = "LIVE",
                        chartData = listOf(10f, 20f, 15f, 40f),
                        isPositive = true,
                        compact = true
                    )
                }
                Box(modifier = Modifier.weight(1f)) {
                    StatCard(
                        label = "Risk Index",
                        value = String.format("%.2f", avgRisk),
                        trend = "INDEX",
                        chartData = listOf(80f, 70f, 75f, 60f),
                        isPositive = avgRisk < 0.4,
                        compact = true
                    )
                }
            }

            Spacer(Modifier.height(32.dp))

            Text("Encrypted Transaction Stream", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(16.dp))

            if (transactions.isEmpty() && !isLoading) {
                Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                    Text("No data found in Supabase", color = Color.DarkGray)
                }
            } else {
                transactions.forEach { tx ->
                    TransactionListItem(tx)
                    Spacer(Modifier.height(10.dp))
                }
            }

            Spacer(Modifier.height(100.dp))
        }
    }
}

@Composable
private fun StatCard(
    label: String,
    value: String,
    trend: String,
    chartData: List<Float>,
    isPositive: Boolean,
    compact: Boolean = false
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B).copy(alpha = 0.4f)),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(label, color = Color.Gray, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
            Text(value, color = Color.White, fontSize = if (compact) 24.sp else 34.sp, fontWeight = FontWeight.Black)
            Spacer(Modifier.height(12.dp))
            SentinelTrendChart(
                dataPoints = chartData,
                lineColor = if (isPositive) Color(0xFF10B981) else Color(0xFFEF4444),
                modifier = Modifier.fillMaxWidth().height(if (compact) 30.dp else 60.dp)
            )
        }
    }
}