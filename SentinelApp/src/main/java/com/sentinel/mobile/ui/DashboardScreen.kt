package com.sentinel.mobile.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Logout
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
    var showNewTransactionDialog by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Color(0xFF020617),
        topBar = {
            MainHeader(
                userEmail = "admin@sentinel.com",
                avatarUrl = null,
                onOpenMenu = onOpenMenu,
                onNavigate = { route ->
                    when(route) {
                        "settings" -> onNavigateToSettings()
                        "support" -> onNavigateToSupport()
                        "ai_assistant" -> onNavigateToAI()
                        "logout" -> onLogout() // ✅ USED: Logout logic integrated
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showNewTransactionDialog = true },
                containerColor = Color(0xFF0284C7),
                contentColor = Color.White,
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(Icons.Default.Add, "New")
                Spacer(Modifier.width(8.dp))
                Text("New Transaction", fontWeight = FontWeight.ExtraBold)
            }
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
                "Financial Overview",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier.padding(top = 16.dp)
            )
            Text(
                "Real-time monitoring of corporate expenditure.",
                color = Color.Gray,
                fontSize = 14.sp
            )

            Spacer(Modifier.height(24.dp))

            StatCard(
                label = "Total Volume",
                value = "$38,304.97",
                trend = "12.5%",
                chartData = listOf(10f, 40f, 25f, 50f, 45f, 70f),
                isPositive = true
            )

            Spacer(Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(modifier = Modifier.weight(1f)) {
                    StatCard(
                        label = "Transactions",
                        value = "${transactions.size}", // ✅ DYNAMIC: Reads from state
                        trend = "8.2%",
                        chartData = listOf(20f, 30f, 25f, 40f),
                        isPositive = true,
                        compact = true
                    )
                }
                Box(modifier = Modifier.weight(1f)) {
                    StatCard(
                        label = "Avg Risk",
                        value = "0.04",
                        trend = "2.4%",
                        chartData = listOf(50f, 40f, 45f, 30f),
                        isPositive = false,
                        compact = true
                    )
                }
            }

            Spacer(Modifier.height(32.dp))

            Text("Recent Activity", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text("Live audit stream active.", color = Color.Gray, fontSize = 12.sp)
            Spacer(Modifier.height(16.dp))

            transactions.forEach { tx ->
                TransactionListItem(tx)
            }

            if (transactions.isEmpty()) {
                Box(modifier = Modifier.fillMaxWidth().padding(40.dp), contentAlignment = Alignment.Center) {
                    Text("No transactions found in current session.", color = Color.Gray)
                }
            }

            Spacer(Modifier.height(100.dp))
        }
    }

    if (showNewTransactionDialog) {
        NewTransactionDialog(onDismiss = { showNewTransactionDialog = false })
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
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(20.dp)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B).copy(alpha = 0.4f)),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(label, color = Color.Gray, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                Surface(
                    color = (if (isPositive) Color(0xFF10B981) else Color(0xFFEF4444)).copy(alpha = 0.1f),
                    shape = RoundedCornerShape(50)
                ) {
                    Text(
                        text = "${if (isPositive) "↗" else "↘"} $trend",
                        color = if (isPositive) Color(0xFF10B981) else Color(0xFFEF4444),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
            Text(value, color = Color.White, fontSize = if (compact) 24.sp else 32.sp, fontWeight = FontWeight.Black)
            Spacer(Modifier.height(12.dp))
            SentinelTrendChart(
                dataPoints = chartData,
                lineColor = if (isPositive) Color(0xFF10B981) else Color(0xFFEF4444),
                modifier = Modifier.fillMaxWidth().height(if (compact) 40.dp else 60.dp)
            )
        }
    }
}