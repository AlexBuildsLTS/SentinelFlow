package com.sentinel.mobile.ui

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import kotlinx.coroutines.launch
import com.sentinel.mobile.auth.SessionManager
import com.sentinel.mobile.viewmodel.LiveMonitorViewModel
import com.sentinel.mobile.ui.components.SentinelSidebar

@Composable
fun SentinelAppRoot() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }

    val startDestination = if (sessionManager.getToken() != null) "dashboard" else "login"
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
                    sessionManager.clearSession()
                    navController.navigate("login") { popUpTo(0) { inclusive = true } }
                }
            )
        }
    ) {
        NavHost(navController = navController, startDestination = startDestination) {
            composable("login") {
                LoginScreen(
                    onLoginSuccess = { navController.navigate("dashboard") { popUpTo("login") { inclusive = true } } },
                    onNavigateToRegister = { navController.navigate("register") }
                )
            }
            composable("register") {
                RegisterScreen(
                    onRegisterSuccess = { navController.navigate("dashboard") { popUpTo(0) } },
                    onNavigateToLogin = { navController.popBackStack() }
                )
            }
            composable("dashboard") {
                DashboardScreen(
                    viewModel = liveMonitorViewModel,
                    onOpenMenu = { scope.launch { drawerState.open() } },
                    onNavigateToAI = { navController.navigate("ai_assistant") },
                    onNavigateToSupport = { navController.navigate("support") },
                    onNavigateToSettings = { navController.navigate("settings") },
                    onLogout = { sessionManager.clearSession(); navController.navigate("login") { popUpTo(0) } }
                )
            }
            composable("ai_assistant") { SentinelAIScreen(onBack = { navController.popBackStack() }) }
            composable("settings") {
                SettingsScreen(
                    uiState = SettingsUiState(userEmail = sessionManager.getUserId() ?: "User"),
                    onBack = { navController.popBackStack() },
                    onApiKeyChanged = {}, onUpdateApiKeyClicked = {},
                    onBiometricToggled = {}, on2faToggled = {}, onViewLogsClicked = {},
                    onLogoutClicked = { sessionManager.clearSession(); navController.navigate("login") { popUpTo(0) } }
                )
            }
            composable("support") {
                // ✅ tickets parameter now uses a standard List to match SupportTicketScreen
                SupportTicketScreen(
                    tickets = emptyList(),
                    currentUserRole = com.sentinel.mobile.models.UserRole.ADMIN,
                    onBack = { navController.popBackStack() }
                )
            }
            composable(
                route = "chat/{partnerId}",
                arguments = listOf(navArgument("partnerId") { type = NavType.StringType })
            ) { backStackEntry ->
                val partnerId = backStackEntry.arguments?.getString("partnerId") ?: ""
                PrivateMessagesScreen(recipientName = "Agent", partnerId = partnerId, onBack = { navController.popBackStack() })
            }
        }
    }
}