package com.sentinel.mobile.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.*
import com.sentinel.mobile.api.ApiClient
import com.sentinel.mobile.api.SignUpRequest
import com.sentinel.mobile.auth.SessionManager
import com.sentinel.mobile.ui.components.PasswordStrengthMeter
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val sessionManager = remember { SessionManager(context) }

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var passwordVisible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF1E1B4B), Color(0xFF020617)))),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(28.dp)),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B).copy(alpha = 0.8f))
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header
                Surface(
                    color = Color(0xFF0EA5E9),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.size(56.dp)
                ) {
                    Icon(Icons.Default.Shield, "Logo", tint = Color.White, modifier = Modifier.padding(12.dp))
                }

                Text("SentinelFlow", color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.ExtraBold, modifier = Modifier.padding(top = 16.dp))
                Text("Secure Audit Environment", color = Color.Gray, fontSize = 13.sp)

                // Error Feedback
                if (errorMessage != null) {
                    Text(
                        text = errorMessage!!,
                        color = Color(0xFFEF4444),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }

                // Toggle Tabs
                Row(
                    modifier = Modifier
                        .padding(vertical = 24.dp)
                        .background(Color(0xFF0F172A), RoundedCornerShape(12.dp))
                        .padding(4.dp)
                ) {
                    TextButton(onClick = onNavigateToLogin, modifier = Modifier.weight(1f)) {
                        Text("Sign In", color = Color.Gray)
                    }
                    Button(
                        onClick = {},
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0EA5E9))
                    ) {
                        Text("Create Account", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }

                // Inputs
                RegisterField(value = name, onValueChange = { name = it }, label = "Full Name", icon = Icons.Default.Person)
                Spacer(Modifier.height(12.dp))
                RegisterField(value = email, onValueChange = { email = it }, label = "Email Identity", icon = Icons.Default.Email)
                Spacer(Modifier.height(12.dp))

                RegisterField(
                    value = password,
                    onValueChange = { password = it },
                    label = "Secure Password",
                    icon = Icons.Default.Lock,
                    isPassword = true,
                    isVisible = passwordVisible,
                    onToggleVisibility = { passwordVisible = !passwordVisible }
                )

                PasswordStrengthMeter(password = password)

                Spacer(Modifier.height(12.dp))
                RegisterField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = "Verify Password",
                    icon = Icons.Default.Key,
                    isPassword = true,
                    isVisible = passwordVisible,
                    onToggleVisibility = { passwordVisible = !passwordVisible }
                )

                // Register Action
                Button(
                    onClick = {
                        if (name.isBlank() || email.isBlank() || password.isBlank()) {
                            errorMessage = "All fields required."
                            return@Button
                        }
                        if (password != confirmPassword) {
                            errorMessage = "Passwords do not match."
                            return@Button
                        }

                        isLoading = true
                        scope.launch {
                            try {
                                val request = SignUpRequest(
                                    email = email,
                                    password = password,
                                    data = mapOf("full_name" to name)
                                )
                                val response = ApiClient.service.signUp(request = request)

                                if (response.isSuccessful && response.body() != null) {
                                    val authData = response.body()!!
                                    // Auto-Login / Save Session
                                    if (authData.accessToken != null) {
                                        sessionManager.saveSession(authData.accessToken, authData.user.id)
                                        onRegisterSuccess()
                                    } else {
                                        // Sometimes Supabase requires email verification first
                                        errorMessage = "Account created. Please verify your email."
                                    }
                                } else {
                                    errorMessage = "Registration Failed: ${response.message()}"
                                }
                            } catch (e: Exception) {
                                errorMessage = "Network Error: ${e.message}"
                            } finally {
                                isLoading = false
                            }
                        }
                    },
                    enabled = !isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 28.dp)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0EA5E9)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Text("Initialize Account", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun RegisterField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isPassword: Boolean = false,
    isVisible: Boolean = true,
    onToggleVisibility: () -> Unit = {}
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(label, color = Color.Gray, fontSize = 14.sp) },
        leadingIcon = { Icon(icon, null, tint = Color.Gray, modifier = Modifier.size(20.dp)) },
        trailingIcon = if (isPassword) {
            {
                IconButton(onClick = onToggleVisibility) {
                    Icon(
                        if (isVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        null,
                        tint = Color.Gray
                    )
                }
            }
        } else null,
        visualTransformation = if (isPassword && !isVisible) PasswordVisualTransformation() else VisualTransformation.None,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedBorderColor = Color(0xFF0EA5E9),
            unfocusedBorderColor = Color.White.copy(alpha = 0.1f),
            unfocusedContainerColor = Color(0xFF0F172A),
            focusedContainerColor = Color(0xFF0F172A)
        ),
        singleLine = true
    )
}