package com.sentinel.mobile.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.*
import coil.compose.AsyncImage

// ✅ This represents the state that a ViewModel would provide
data class SettingsUiState(
    val userEmail: String = "admin@sentinel.com",
    val avatarUrl: String? = null,
    val userApiKey: String = "",
    val isSaving: Boolean = false,
    val isBiometricEnabled: Boolean = true,
    val is2faEnabled: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    // ✅ Use a state holder object for cleaner parameters
    uiState: SettingsUiState,
    // ✅ Hoist all events out of the screen
    onApiKeyChanged: (String) -> Unit,
    onUpdateApiKeyClicked: () -> Unit,
    onBiometricToggled: (Boolean) -> Unit,
    on2faToggled: (Boolean) -> Unit,
    onViewLogsClicked: () -> Unit,
    onLogoutClicked: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        containerColor = Color(0xFF020617),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("System Configuration", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White)
                    }
                },
                // ✅ FIXED: Deprecated colors call
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF020617))
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // --- IDENTITY SNAPSHOT MODULE ---
            Spacer(Modifier.height(16.dp))
            IdentitySnapshot(email = uiState.userEmail, avatarUrl = uiState.avatarUrl)

            Spacer(Modifier.height(32.dp))

            // --- NEURAL CONFIGURATION MODULE ---
            SettingsSectionHeader("SentinelAI Neural Engine")
            Text(
                text = "Provide your personal API key. Requests are proxied through an encrypted tunnel for maximum privacy.",
                color = Color.Gray, fontSize = 12.sp, lineHeight = 18.sp,
                modifier = Modifier.padding(bottom = 16.dp, start = 4.dp)
            )

            OutlinedTextField(
                value = uiState.userApiKey,
                onValueChange = onApiKeyChanged, // ✅ Event hoisted
                label = { Text("OpenAI / Anthropic Key") },
                placeholder = { Text("sk-...", color = Color.DarkGray) },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFF0F172A),
                    focusedContainerColor = Color(0xFF0F172A),
                    unfocusedBorderColor = Color.White.copy(alpha = 0.1f),
                    focusedBorderColor = Color(0xFF8B5CF6),
                    focusedTextColor = Color.White,
                    unfocusedLabelColor = Color.Gray,
                    unfocusedTrailingIconColor = Color.Gray
                ),
                trailingIcon = {
                    if (uiState.isSaving) CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp, color = Color(0xFF8B5CF6))
                    else Icon(Icons.Default.VpnKey, null)
                }
            )

            Button(
                onClick = onUpdateApiKeyClicked, // ✅ Event hoisted
                modifier = Modifier.padding(top = 12.dp).align(Alignment.End),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B5CF6).copy(alpha = 0.2f), contentColor = Color(0xFF8B5CF6)),
                border = BorderStroke(1.dp, Color(0xFF8B5CF6).copy(alpha = 0.4f)),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Secure Update", fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(32.dp))

            // --- SECURITY HARDENING ---
            SettingsSectionHeader("Security & Biometrics")
            SettingsToggleCard(
                title = "Fingerprint Identity",
                sub = "Require biometric scan for session entry",
                icon = Icons.Default.Fingerprint,
                checked = uiState.isBiometricEnabled, // ✅ State comes from uiState
                onCheckedChange = onBiometricToggled   // ✅ Event is hoisted
            )
            SettingsToggleCard(
                title = "Hardware 2FA",
                sub = "Yubikey or authenticator verification",
                icon = Icons.Default.PhonelinkLock,
                checked = uiState.is2faEnabled,     // ✅ State comes from uiState
                onCheckedChange = on2faToggled       // ✅ Event is hoisted
            )
            SettingsCard(
                title = "Local Audit Logs",
                sub = "Review last 50 encrypted events",
                icon = Icons.Default.History,
                onClick = onViewLogsClicked // ✅ Event hoisted
            )

            Spacer(Modifier.height(48.dp))

            // --- SESSION TERMINATION ---
            Button(
                onClick = onLogoutClicked, // ✅ Event hoisted
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444).copy(alpha = 0.1f)),
                border = BorderStroke(1.dp, Color(0xFFEF4444).copy(alpha = 0.5f)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("End Authenticated Session", color = Color(0xFFEF4444), fontWeight = FontWeight.ExtraBold)
            }
            Spacer(Modifier.height(32.dp))
        }
    }
}

// Extracted for clarity
@Composable
private fun IdentitySnapshot(email: String, avatarUrl: String?) {
    Surface(
        color = Color(0xFF1E293B).copy(alpha = 0.4f),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(contentAlignment = Alignment.BottomEnd) {
                Surface(
                    shape = CircleShape,
                    color = Color(0xFF6366F1),
                    modifier = Modifier.size(64.dp)
                ) {
                    if (avatarUrl != null) {
                        AsyncImage(model = avatarUrl, contentDescription = null, modifier = Modifier.clip(CircleShape))
                    } else {
                        Icon(Icons.Default.Person, null, tint = Color.White, modifier = Modifier.padding(16.dp))
                    }
                }
                Surface(color = Color(0xFF10B981), shape = CircleShape, modifier = Modifier.size(16.dp).border(2.dp, Color(0xFF0F172A), CircleShape)) {}
            }
            Column(modifier = Modifier.padding(start = 16.dp)) {
                Text(email, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("Senior Audit Admin", color = Color.Gray, fontSize = 13.sp)
            }
        }
    }
}
