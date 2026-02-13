package com.sentinel.mobile.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel // ✅ Instantiates VM
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sentinel.mobile.models.SupportTicket
import com.sentinel.mobile.models.TicketStatus
import com.sentinel.mobile.models.UserRole
import com.sentinel.mobile.viewmodel.LiveMonitorViewModel

@Composable
fun SentinelAppRoot() {
    val navController = rememberNavController()

    // ✅ Instantiate the ViewModel here (scoped to the nav host or app)
    // The "viewModel()" function handles the Factory logic for AndroidViewModel automatically
    val liveMonitorViewModel: LiveMonitorViewModel = viewModel()

    val mockTickets = remember {
        listOf(
            SupportTicket("1", "Login Issue", "Cannot access account", TicketStatus.OPEN, "URGENT", "10:00 AM"),
            SupportTicket("2", "Payment Failed", "Transaction declined", TicketStatus.IN_PROGRESS, "HIGH", "11:30 AM")
        )
    }

    NavHost(navController = navController, startDestination = "login") {

        composable("login") {
            LoginScreen(onLoginSuccess = {
                navController.navigate("dashboard") {
                    popUpTo("login") { inclusive = true }
                }
            })
        }

        composable("dashboard") {
            // ✅ Pass the ViewModel to the Dashboard
            DashboardScreen(
                viewModel = liveMonitorViewModel,
                onNavigateToSupport = { navController.navigate("support") },
                onNavigateToSettings = { navController.navigate("settings") },
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("dashboard") { inclusive = true }
                    }
                }
            )
        }

        composable("support") {
            SupportTicketScreen(
                tickets = mockTickets,
                currentUserRole = UserRole.ADMIN,
                onBack = { navController.popBackStack() }
            )
        }

        composable("settings") {
            SettingsScreen(onBack = { navController.popBackStack() })
        }
    }
}