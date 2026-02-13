package com.sentinel.mobile.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.sentinel.mobile.api.SupabaseConfig
import com.sentinel.mobile.models.Transaction
import com.sentinel.mobile.models.UserRole
import com.sentinel.mobile.ui.components.RoleBadge
import com.sentinel.mobile.ui.components.TransactionDialog
import com.sentinel.mobile.viewmodel.LiveMonitorViewModel
import kotlinx.coroutines.launch

@Composable
fun DashboardScreen(
    viewModel: LiveMonitorViewModel, // ✅ Receives the ViewModel
    onNavigateToSupport: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onLogout: () -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // ✅ Collect Real Data
    val transactions by viewModel.transactions.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // Trigger data fetch on load
    LaunchedEffect(Unit) {
        viewModel.startMonitoring()
    }

    val avatarUrl = "${SupabaseConfig.STORAGE_URL}user_avatar.png"
    var selectedTransaction by remember { mutableStateOf<Transaction?>(null) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Color(0xFF1E293B),
                drawerContentColor = Color.White
            ) {
                Spacer(Modifier.height(24.dp))
                Text("Sentinel Menu", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(16.dp))
                HorizontalDivider(color = Color.Gray.copy(alpha = 0.3f))

                NavigationDrawerItem(
                    label = { Text("Dashboard", color = Color.White) },
                    selected = true,
                    onClick = { scope.launch { drawerState.close() } },
                    icon = { Icon(Icons.Default.Home, null, tint = Color(0xFF0EA5E9)) },
                    colors = NavigationDrawerItemDefaults.colors(unselectedContainerColor = Color.Transparent)
                )
                NavigationDrawerItem(
                    label = { Text("Support Tickets", color = Color.White) },
                    selected = false,
                    onClick = { onNavigateToSupport() },
                    icon = { Icon(Icons.Default.Person, null, tint = Color.Gray) },
                    colors = NavigationDrawerItemDefaults.colors(unselectedContainerColor = Color.Transparent)
                )
                NavigationDrawerItem(
                    label = { Text("Settings", color = Color.White) },
                    selected = false,
                    onClick = { onNavigateToSettings() },
                    icon = { Icon(Icons.Default.Settings, null, tint = Color.Gray) },
                    colors = NavigationDrawerItemDefaults.colors(unselectedContainerColor = Color.Transparent)
                )

                Spacer(Modifier.weight(1f))
                Button(
                    onClick = onLogout,
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red.copy(alpha = 0.8f))
                ) {
                    Text("LOGOUT")
                }
            }
        }
    ) {
        Scaffold(
            containerColor = Color(0xFF0F172A),
            topBar = {
                Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { scope.launch { drawerState.open() } }) {
                        Icon(Icons.Default.Menu, "Menu", tint = Color.White)
                    }
                    Surface(color = Color(0xFF1E293B), shape = RoundedCornerShape(50), border = BorderStroke(1.dp, Color(0xFF0EA5E9).copy(alpha = 0.3f))) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)) {
                            // Spinning indicator if loading, else Bolt
                            if (isLoading) {
                                CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Color(0xFF0EA5E9), strokeWidth = 2.dp)
                            } else {
                                Icon(Icons.Default.Bolt, null, tint = Color(0xFF0EA5E9), modifier = Modifier.size(16.dp))
                            }
                            Spacer(Modifier.width(8.dp))
                            Text("LIVE FEED", color = Color(0xFF0EA5E9), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    AsyncImage(model = avatarUrl, contentDescription = "Profile", modifier = Modifier.size(40.dp).clip(CircleShape).background(Color.Gray), contentScale = ContentScale.Crop)
                }
            }
        ) { padding ->
            Column(modifier = Modifier.padding(padding).padding(16.dp).fillMaxSize()) {
                Text("Financial Overview", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text("Real-time monitoring.", color = Color.Gray, fontSize = 14.sp)
                Spacer(Modifier.height(16.dp))

                Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)), shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(24.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AccountBalanceWallet, null, tint = Color(0xFF0EA5E9))
                            Spacer(Modifier.width(8.dp))
                            Text("Total Volume", color = Color.Gray, fontSize = 14.sp)
                        }
                        Spacer(Modifier.height(8.dp))
                        Text("$38,304.97", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                Text("Recent Activity", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(12.dp))

                if (transactions.isEmpty() && !isLoading) {
                    Text("No transactions found.", color = Color.Gray, modifier = Modifier.padding(top = 20.dp))
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(transactions) { tx ->
                            TransactionRow(tx, onClick = { selectedTransaction = tx })
                        }
                    }
                }
            }
        }
    }

    selectedTransaction?.let { tx ->
        TransactionDialog(transaction = tx, onDismiss = { selectedTransaction = null })
    }
}

@Composable
fun TransactionRow(tx: Transaction, onClick: () -> Unit) {
    Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)), shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth().clickable { onClick() }) {
        Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Column {
                Text(tx.merchantName, color = Color.White, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(4.dp))
                Surface(color = Color.White.copy(alpha = 0.1f), shape = RoundedCornerShape(4.dp)) {
                    Text(text = tx.status, color = if (tx.status == "FAILED") Color(0xFFEF4444) else Color(0xFF10B981), fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("${tx.currency} ${tx.amount}", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(tx.timestamp, color = Color.Gray, fontSize = 12.sp)
            }
        }
    }
}