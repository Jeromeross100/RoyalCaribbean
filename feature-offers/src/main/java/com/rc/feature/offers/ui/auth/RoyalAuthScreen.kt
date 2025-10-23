// feature-offers/src/main/java/com/rc/feature/offers/ui/auth/RoyalAuthScreen.kt
package com.rc.feature.offers.ui.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.collectLatest

private object RoyalPalette {
    val Navy = Color(0xFF003A70)
    val Gold = Color(0xFFFFC72C)
    val Sky  = Color(0xFF1F7DD4)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoyalAuthScreen(
    onSuccess: () -> Unit,
    onBack: () -> Unit = {},
    vm: AuthViewModel = hiltViewModel()
) {
    val state by vm.state.collectAsState()

    // Robust navigation: reacts to one-shot events (won’t miss due to recomposition)
    LaunchedEffect(Unit) {
        vm.events.collectLatest { event ->
            when (event) {
                is AuthEvent.SignedIn, is AuthEvent.SignedUp -> onSuccess()
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Royal Caribbean") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = RoyalPalette.Navy,
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(RoyalPalette.Navy, RoyalPalette.Sky.copy(alpha = 0.2f), Color.White)
                    )
                )
                .padding(padding)
        ) {
            // Header chip
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 18.dp, bottom = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Surface(color = RoyalPalette.Gold, shape = RoundedCornerShape(24.dp)) {
                    Text(
                        if (state.mode == AuthMode.SignIn) "Welcome back" else "Create your account",
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFF2C2C2C)
                    )
                }
            }

            // Form card
            Surface(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                tonalElevation = 6.dp
            ) {
                Column(
                    Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    SegmentedButton(
                        options = listOf("Sign In", "Create Account"),
                        selectedIndex = if (state.mode == AuthMode.SignIn) 0 else 1,
                        onSelected = { vm.toggleMode() }
                    )

                    AnimatedVisibility(
                        visible = state.mode == AuthMode.CreateAccount,
                        enter = androidx.compose.animation.fadeIn(tween(250)),
                        exit = androidx.compose.animation.fadeOut(tween(200))
                    ) {
                        OutlinedTextField(
                            value = state.fullName,
                            onValueChange = vm::updateFullName,
                            label = { Text("Full name") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    OutlinedTextField(
                        value = state.email,
                        onValueChange = vm::updateEmail,
                        label = { Text("Email") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = state.password,
                        onValueChange = vm::updatePassword,
                        label = { Text("Password") },
                        singleLine = true,
                        visualTransformation = if (state.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = vm::togglePasswordVisibility) {
                                Icon(
                                    if (state.isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = null
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (state.error != null) {
                        AssistChip(
                            onClick = {},
                            label = { Text(state.error!!) },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer
                            )
                        )
                    }

                    Button(
                        onClick = vm::submit,
                        enabled = !state.isLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .clip(RoundedCornerShape(14.dp))
                    ) {
                        if (state.isLoading) {
                            CircularProgressIndicator(
                                strokeWidth = 2.dp,
                                modifier = Modifier
                                    .size(20.dp)
                                    .padding(end = 8.dp),
                                color = Color.White
                            )
                        }
                        Text(if (state.mode == AuthMode.SignIn) "Sign In" else "Create Account")
                    }

                    TextButton(
                        onClick = vm::toggleMode,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            if (state.mode == AuthMode.SignIn) "Need an account? Create one"
                            else "Have an account? Sign in"
                        )
                    }
                }
            }

            Spacer(Modifier.weight(1f))

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Surface(color = RoyalPalette.Navy, shape = CircleShape) {
                    Text(
                        "Crown & Anchor® Society",
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}

@Composable
private fun SegmentedButton(
    options: List<String>,
    selectedIndex: Int,
    onSelected: (Int) -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        options.forEachIndexed { idx, label ->
            val selected = idx == selectedIndex
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp),
                shape = RoundedCornerShape(10.dp),
                color = if (selected) RoyalPalette.Navy else Color.Transparent,
                tonalElevation = if (selected) 1.dp else 0.dp,
                onClick = { onSelected(idx) }
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        label,
                        color = if (selected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}
