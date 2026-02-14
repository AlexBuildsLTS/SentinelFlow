package com.sentinel.mobile.ui

import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset // ✅ FIXED: Missing import for Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign // ✅ FIXED: Missing import for TextAlign
import androidx.compose.ui.unit.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sentinel.mobile.R
import com.sentinel.mobile.viewmodel.LoginEvent
import com.sentinel.mobile.viewmodel.LoginViewModel
import com.sentinel.mobile.viewmodel.LoginUiState

// This is the stateful screen that connects to the ViewModel
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    loginViewModel: LoginViewModel = viewModel() // ✅ Get instance of the ViewModel
) {
    val uiState by loginViewModel.uiState.collectAsStateWithLifecycle()
    val loginEvent by loginViewModel.loginEvent.collectAsStateWithLifecycle()

    // Handle the one-time navigation event
    LaunchedEffect(loginEvent) {
        if (loginEvent is LoginEvent.LoginSuccess) {
            onLoginSuccess()
            loginViewModel.onLoginEventHandled() // Reset the event
        }
    }

    // Pass state down and hoist events up to the ViewModel
    LoginScreenContent(
        uiState = uiState,
        onEmailChanged = loginViewModel::onEmailChanged,
        onPasswordChanged = loginViewModel::onPasswordChanged,
        onLoginClicked = loginViewModel::onLoginClicked,
        onForgotPasswordClicked = loginViewModel::onForgotPasswordClicked,
        onTogglePasswordVisibility = loginViewModel::onTogglePasswordVisibility,
        onNavigateToRegister = onNavigateToRegister
    )
}

// This is the stateless UI component. It is now easy to preview and test.
@Composable
fun LoginScreenContent(
    uiState: LoginUiState,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onLoginClicked: () -> Unit,
    onForgotPasswordClicked: () -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF0F172A), Color(0xFF020617)))),
        contentAlignment = Alignment.Center
    ) {
        // Ambient Background Glow
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFF0EA5E9).copy(alpha = 0.1f), Color.Transparent),
                    center = Offset(size.width * 0.8f, size.height * 0.2f),
                    radius = 500f
                )
            )
        }

        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(28.dp)),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B).copy(alpha = 0.7f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 24.dp)
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Sentinel Logo",
                    modifier = Modifier.size(80.dp).padding(bottom = 16.dp),
                    contentScale = ContentScale.Fit
                )

                Text("SentinelFlow", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 1.sp)
                Text("System Access Control", color = Color(0xFF94A3B8), fontSize = 13.sp, fontWeight = FontWeight.Medium)

                if (uiState.errorMessage != null) {
                    FeedbackMessage(text = uiState.errorMessage, isError = true)
                }

                if (uiState.successMessage != null) {
                    FeedbackMessage(text = uiState.successMessage, isError = false)
                }

                Spacer(Modifier.height(32.dp))

                OutlinedTextField(
                    value = uiState.email,
                    onValueChange = onEmailChanged,
                    label = { Text("Email Identity") },
                    leadingIcon = { Icon(Icons.Default.Email, null, tint = Color(0xFF64748B)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = sentinelTextFieldColors(),
                    singleLine = true
                )

                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = uiState.password,
                    onValueChange = onPasswordChanged,
                    label = { Text("Password") },
                    leadingIcon = { Icon(Icons.Default.Lock, null, tint = Color(0xFF64748B)) },
                    trailingIcon = {
                        IconButton(onClick = onTogglePasswordVisibility) {
                            Icon(
                                if (uiState.passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                "Toggle password visibility",
                                tint = Color(0xFF64748B)
                            )
                        }
                    },
                    visualTransformation = if (uiState.passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = sentinelTextFieldColors(),
                    singleLine = true
                )

                TextButton(
                    onClick = onForgotPasswordClicked,
                    modifier = Modifier.align(Alignment.End).padding(top = 4.dp)
                ) {
                    if (uiState.isResetting) {
                        CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp, color = Color(0xFF0EA5E9))
                    } else {
                        Text("Recover Identity?", color = Color(0xFF0EA5E9), fontSize = 12.sp)
                    }
                }

                Button(
                    onClick = onLoginClicked,
                    enabled = !uiState.isLoading,
                    modifier = Modifier.fillMaxWidth().padding(top = 24.dp).height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0EA5E9),
                        disabledContainerColor = Color(0xFF0EA5E9).copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                    } else {
                        Text("Authenticate", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }

                Spacer(Modifier.height(24.dp))
                Divider(color = Color.White.copy(alpha = 0.1f))
                Spacer(Modifier.height(16.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Don't have an identity?", color = Color(0xFF94A3B8), fontSize = 13.sp)
                    TextButton(onClick = onNavigateToRegister) {
                        Text("Create Account", color = Color(0xFF0EA5E9), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}

// ✅ NEW FEATURE: Extracted composables for cleaner code
@Composable
private fun FeedbackMessage(text: String, isError: Boolean) {
    val backgroundColor = if (isError) Color(0xFFEF4444).copy(alpha = 0.1f) else Color(0xFF10B981).copy(alpha = 0.1f)
    val textColor = if (isError) Color(0xFFEF4444) else Color(0xFF10B981)

    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.padding(top = 16.dp).fillMaxWidth()
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(12.dp),
            textAlign = TextAlign.Center
        )
    }
}

// ✅ NEW FEATURE: Centralized TextField colors to reduce duplication
@Composable
private fun sentinelTextFieldColors(): TextFieldColors = OutlinedTextFieldDefaults.colors(
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White,
    focusedContainerColor = Color(0xFF0F172A).copy(alpha = 0.5f),
    unfocusedContainerColor = Color(0xFF0F172A).copy(alpha = 0.5f),
    focusedBorderColor = Color(0xFF0EA5E9),
    unfocusedBorderColor = Color.White.copy(alpha = 0.1f),
    cursorColor = Color(0xFF0EA5E9)
)
