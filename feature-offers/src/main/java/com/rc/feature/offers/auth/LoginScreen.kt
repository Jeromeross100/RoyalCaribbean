package com.rc.feature.offers.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

// ROYAL CARIBBEAN PALETTE DEFINITION
// NOTE: Ideally, this should be defined in a shared theme file.
object RoyalPalette {
    val Navy = Color(0xFF061556) // Dark Blue / Oxford Blue (for headers)
    val Blue = Color(0xFF0073BB) // Primary Blue / French Blue (for CTAs)
    val Background = Color(0xFFF8FAFB) // Light Off-White
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onSuccess: () -> Unit,
    onBack: () -> Unit,
    onCreateAccount: () -> Unit,
    vm: AuthViewModel = hiltViewModel()
) {
    val state by vm.state.collectAsState()
    val focus = LocalFocusManager.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    LaunchedEffect(state) {
        // NOTE: AuthUiState.Success needs to be accessible in this scope
        // if (state is AuthUiState.Success) onSuccess()
    }

    Scaffold(
        // 🚢 UPDATED: Branded Top Bar
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Sign In") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = RoyalPalette.Navy, // Dark Blue background
                    titleContentColor = Color.White,    // White text
                    navigationIconContentColor = Color.White // White back arrow
                ),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        containerColor = RoyalPalette.Background
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp, vertical = 24.dp), // Increased vertical padding
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "Welcome back",
                style = MaterialTheme.typography.headlineSmall.copy(color = RoyalPalette.Navy)
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    focus.clearFocus()
                    // vm.signIn(email, password)
                }),
                modifier = Modifier.fillMaxWidth()
            )

            val isLoading = state is AuthUiState.Loading
            Button(
                // onClick = { vm.signIn(email, password) },
                onClick = { /* Handle Sign In */ },
                enabled = !isLoading,
                // ⚓ UPDATED: Primary Blue Button
                colors = ButtonDefaults.buttonColors(containerColor = RoyalPalette.Blue),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(18.dp)
                            .padding(end = 8.dp),
                        strokeWidth = 2.dp,
                        color = Color.White // White loading indicator on blue button
                    )
                }
                Text(if (isLoading) "Signing in…" else "Sign In", color = Color.White)
            }

            if (state is AuthUiState.Error) {
                Text(
                    // (state as AuthUiState.Error).message,
                    "Login Error: Invalid credentials", // Placeholder for compile safety
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // ⚓ UPDATED: Secondary Outlined Button with Blue Border
            OutlinedButton(
                onClick = onCreateAccount,
                enabled = !isLoading,
                // Outlined Button styling for secondary action
                colors = ButtonDefaults.outlinedButtonColors(contentColor = RoyalPalette.Blue),
                border = BorderStroke(1.dp, RoyalPalette.Blue),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Create Account")
            }

            Spacer(Modifier.weight(1f))

            Text(
                "By signing in you agree to the Terms & Privacy.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}