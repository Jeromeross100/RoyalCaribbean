// feature-offers/src/main/java/com/rc/feature/offers/ui/auth/RoyalAuthScreen.kt
package com.rc.feature.offers.ui.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade // <-- New Import
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
    val Navy = Color(0xFF061556) // Dark Blue / Oxford Blue
    val Blue = Color(0xFF0073BB) // Primary Blue / French Blue
    val Gold = Color(0xFFFEBD11) // Accent Gold / Mikado Yellow
    val Background = Color(0xFFF8FAFB) // Light Off-White
    val Error = Color(0xFFB3261E) // Standard Material Error
    val Sky = Color(0xFFB3D9FF) // Light Blue added to fix the Unresolved reference: Sky error
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
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = RoyalPalette.Navy,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                // FIX: Used the new RoyalPalette.Sky color to resolve the reference.
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
                        // Using a darker color for text on Gold for better contrast
                        color = Color(0xFF2C2C2C)
                    )
                }
            }

            // Form card
            Surface(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                // Using MaterialTheme primary color for tonal elevation to better fit the M3 standard
                tonalElevation = 6.dp,
                color = MaterialTheme.colorScheme.surface
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

                    // Animated field for full name
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
                                    contentDescription = "Toggle password visibility"
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Error chip - kept for error display
                    if (state.error != null) {
                        AssistChip(
                            onClick = { /* Consider a dismiss action or no-op */ },
                            label = { Text(state.error!!) },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer,
                                labelColor = MaterialTheme.colorScheme.onErrorContainer
                            )
                        )
                    }

                    // Primary Action Button
                    Button(
                        onClick = vm::submit,
                        enabled = !state.isLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .clip(RoundedCornerShape(14.dp)),
                        colors = ButtonDefaults.buttonColors(containerColor = RoyalPalette.Blue)
                    ) {
                        // FIX: Use Crossfade for state transition, eliminating the RowScope issue
                        Crossfade(
                            targetState = state.isLoading,
                            label = "AuthButtonCrossfade",
                            animationSpec = tween(300)
                        ) { isLoading ->
                            if (isLoading) {
                                CircularProgressIndicator(
                                    strokeWidth = 2.dp,
                                    modifier = Modifier.size(24.dp),
                                    color = Color.White
                                )
                            } else {
                                Text(if (state.mode == AuthMode.SignIn) "Sign In" else "Create Account")
                            }
                        }
                    }

                    // Mode Toggle TextButton
                    TextButton(
                        onClick = vm::toggleMode,
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = RoyalPalette.Navy
                        )
                    ) {
                        Text(
                            if (state.mode == AuthMode.SignIn) "Need an account? Create one"
                            else "Have an account? Sign in"
                        )
                    }
                }
            }

            Spacer(Modifier.weight(1f))

            // Footer Badge
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
                // Using Navy for the selected state
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