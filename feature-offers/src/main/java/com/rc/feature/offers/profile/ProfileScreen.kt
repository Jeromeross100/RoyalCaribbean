package com.rc.feature.offers.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

// ROYAL CARIBBEAN PALETTE DEFINITION
// NOTE: Ideally, this should be defined in a shared theme file.
private object RoyalPalette {
    val Navy = Color(0xFF061556) // Dark Blue / Oxford Blue (for headers, strong text)
    val Blue = Color(0xFF0073BB) // Primary Blue / French Blue (for CTAs, active states)
    val Gold = Color(0xFFFEBD11) // Accent Gold (for loyalty)
    val Background = Color(0xFFF8FAFB) // Light Off-White
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    userProvider: () -> com.rc.feature.offers.auth.User?,
    onLogout: () -> Unit = {} // RENAMED from onSignOut to onLogout
) {
    val user = userProvider()
    var showConfirm by remember { mutableStateOf(false) }

    if (showConfirm) {
        AlertDialog(
            onDismissRequest = { showConfirm = false },
            title = { Text("Sign out?") },
            text = { Text("You’ll be logged out of your Royal Caribbean account on this device.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showConfirm = false
                        onLogout() // UPDATED call site
                    }
                ) { Text("Sign Out") }
            },
            dismissButton = {
                TextButton(onClick = { showConfirm = false }) { Text("Cancel") }
            }
        )
    }

    Scaffold(
        // 🚢 UPDATED: Branded Top Bar
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("My Profile") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = RoyalPalette.Navy, // Dark Blue background
                    titleContentColor = Color.White    // White text
                )
            )
        },
        containerColor = RoyalPalette.Background
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Profile card (Branded Name)
            Surface(
                shape = RoundedCornerShape(16.dp),
                tonalElevation = 6.dp, // Increased elevation
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        user?.fullName ?: "Guest",
                        style = MaterialTheme.typography.headlineSmall.copy(color = RoyalPalette.Navy), // Use Navy
                        fontWeight = FontWeight.Bold
                    )
                    if (!user?.email.isNullOrBlank()) {
                        Text(user?.email ?: "", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }

            // Loyalty card (Crown & Anchor) - Branded with Gold
            Surface(
                shape = RoundedCornerShape(16.dp),
                tonalElevation = 6.dp, // Increased elevation
                // ⚓ NEW: Use Gold accent for the loyalty card background/emphasis
                color = RoyalPalette.Gold.copy(alpha = 0.1f),
                border = BorderStroke(1.dp, RoyalPalette.Gold),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        // Highlight Title in Navy
                        Text("Crown & Anchor®", style = MaterialTheme.typography.titleMedium.copy(color = RoyalPalette.Navy))
                        // Highlight tier in Gold
                        Text(
                            "${user?.crownAnchorsTier ?: "Gold"} • ${user?.points ?: 0} pts",
                            fontWeight = FontWeight.Bold,
                            color = RoyalPalette.Gold // Gold for emphasis
                        )
                    }
                    // Primary Blue Button
                    Button(
                        onClick = { /* TODO: manage loyalty */ },
                        colors = ButtonDefaults.buttonColors(containerColor = RoyalPalette.Blue)
                    ) { Text("View Details") }
                }
            }

            // Upcoming cruises
            Surface(
                shape = RoundedCornerShape(16.dp),
                tonalElevation = 6.dp, // Increased elevation
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Upcoming Cruises", style = MaterialTheme.typography.titleMedium.copy(color = RoyalPalette.Navy))
                    Text("No cruises booked yet.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            Spacer(Modifier.weight(1f))

            // Sign out button (error styling is good, no change needed)
            Button(
                onClick = { showConfirm = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                )
            ) {
                Text("Sign Out")
            }
        }
    }
}