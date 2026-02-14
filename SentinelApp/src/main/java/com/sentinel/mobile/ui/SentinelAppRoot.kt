package com.sentinel.mobile.ui

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.compose.*
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import com.sentinel.mobile.viewmodel.LiveMonitorViewModel
import com.sentinel.mobile.models.*
import com.sentinel.mobile.ui.components.SentinelSidebar

@Composable
fun SentinelAppRoot() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Initialize the ViewModel for real-time data
    val liveMonitorViewModel: LiveMonitorViewModel = viewModel()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            SentinelSidebar(
                onNavigate = { route ->
                    navController.navigate(route) { launchSingleTop = true }
                    scope.launch { drawerState.close() }
                },
                onLogout = {
                    navController.navigate("login") { popUpTo(0) { inclusive = true } }
                    scope.launch { drawerState.close() }
                }
            )
        }
    ) {
        NavHost(navController = navController, startDestination = "login") {

            // 1. Authentication Layer
            composable("login") {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate("dashboard") { popUpTo("login") { inclusive = true } }
                    },
                    onNavigateToRegister = { navController.navigate("register") },
                    onForgotPassword = { /* Todo: Reset Logic */ }
                )
            }

            composable("register") {
                RegisterScreen(
                    onRegisterSuccess = {
                        navController.navigate("dashboard") { popUpTo("register") { inclusive = true } }
                    },
                    onNavigateToLogin = { navController.popBackStack() }
                )
            }

            // 2. Core Dashboard Layer
            composable("dashboard") {
                DashboardScreen(
                    viewModel = liveMonitorViewModel,
                    onOpenMenu = { scope.launch { drawerState.open() } },
                    onNavigateToAI = { navController.navigate("ai_assistant") },
                    onNavigateToSupport = { navController.navigate("support") },
                    onNavigateToSettings = { navController.navigate("settings") },
                    onLogout = { navController.navigate("login") { popUpTo(0) } }
                )
            }

            // 3. Feature Modules
            composable("ai_assistant") {
                SentinelAIScreen(onBack = { navController.popBackStack() })
            }

            composable("settings") {
                SettingsScreen(onBack = { navController.popBackStack() })
            }

            composable("support") {
                SupportTicketScreen(
                    currentUserRole = UserRole.ADMIN,
                    onBack = { navController.popBackStack() },
                    tickets = listOf(
                        SupportTicket("1", "Login Issue", "Session lock", TicketStatus.OPEN, "URGENT", "10:00 AM"),
                        SupportTicket("2", "Terminal Error", "Transaction failed", TicketStatus.IN_PROGRESS, "HIGH", "11:30 AM")
                    )
                )
            }
        }
    }
}