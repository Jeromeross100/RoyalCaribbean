package com.rc.feature.offers.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

// NOTE: Ensure RoyalPalette is accessible here, or define it again if necessary.

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    onSuccess: () -> Unit,
    onBack: () -> Unit,
    vm: AuthViewModel = hiltViewModel()
) {
    val state by vm.state.collectAsState()
    val focus = LocalFocusManager.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val passwordMatch = password == confirmPassword

    LaunchedEffect(state) {
        // if (state is AuthUiState.Success) onSuccess()
    }

    Scaffold(
        // UPDATED: Branded Top Bar
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Create Account") },
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
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Join Royal Caribbean",
                style = MaterialTheme.typography.headlineSmall.copy(color = RoyalPalette.Navy)
            )

            // Email Field
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                modifier = Modifier.fillMaxWidth()
            )

            // Password Field
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                modifier = Modifier.fillMaxWidth()
            )

            // Confirm Password Field
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                isError = !passwordMatch,
                supportingText = if (!passwordMatch && confirmPassword.isNotEmpty()) {
                    { Text("Passwords do not match") }
                } else null,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    focus.clearFocus()
                    // if (passwordMatch && email.isNotEmpty() && password.isNotEmpty()) {
                    //     vm.signUp(email, password)
                    // }
                }),
                modifier = Modifier.fillMaxWidth()
            )

            val isLoading = state is AuthUiState.Loading
            val fieldsValid = email.isNotEmpty() && password.isNotEmpty() && passwordMatch

            // ⚓ UPDATED: Primary Blue Button
            Button(
                // onClick = { vm.signUp(email, password) },
                onClick = { /* Handle Sign Up */ },
                enabled = !isLoading && fieldsValid,
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
                Text(if (isLoading) "Creating account…" else "Sign Up", color = Color.White)
            }

            // Error Display
            if (state is AuthUiState.Error) {
                Text(
                    // (state as AuthUiState.Error).message,
                    "Sign Up Error: Please try again.", // Placeholder for compile safety
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(Modifier.weight(1f))

            Text(
                "By creating an account you agree to the Terms & Privacy.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}