package com.sentinel.mobile.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sentinel.mobile.models.SupportTicket
import com.sentinel.mobile.models.UserRole
import com.sentinel.mobile.ui.components.TicketItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupportTicketScreen(
    tickets: List<SupportTicket>, // Standard List, zero extra dependencies
    currentUserRole: UserRole,
    onBack: () -> Unit
) {
    Scaffold(
        containerColor = Color(0xFF020617),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Encrypted Support", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color(0xFF020617))
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { /* TODO */ },
                containerColor = Color(0xFF0EA5E9),
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, null)
                Spacer(Modifier.width(8.dp))
                Text("New Ticket", fontWeight = FontWeight.Bold)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFF020617))
        ) {
            if (tickets.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("No active transmissions", color = Color.Gray, fontSize = 14.sp)
                        Text("Secure channel is idle.", color = Color.DarkGray, fontSize = 12.sp)
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(tickets) { ticket ->
                        // TicketItem is called with the 'subject' property which is now resolved
                        TicketItem(
                            ticket = ticket,
                            isAdmin = currentUserRole == UserRole.ADMIN
                        )
                    }
                }
            }
        }
    }
}