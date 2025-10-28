// feature-offers/src/main/java/com/rc/feature/offers/bookings/BookingsScreen.kt
package com.rc.feature.offers.bookings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rc.feature.offers.data.graphql.BookingDto
import com.rc.feature.offers.util.UIState

// 🎨 ROYAL CARIBBEAN PALETTE DEFINITION 🎨
// NOTE: Ideally, this should be defined in a shared theme file.
private object RoyalPalette {
    val Navy = Color(0xFF061556) // Dark Blue / Oxford Blue (for headers, strong text)
    val Blue = Color(0xFF0073BB) // Primary Blue / French Blue (for CTAs, active states)
    val Background = Color(0xFFF8FAFB) // Light Off-White
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingsScreen(
    vm: BookingsViewModel = hiltViewModel()
) {
    val state by vm.state.collectAsState()

    LaunchedEffect(Unit) { vm.load() }

    Scaffold(
        // 🚢 UPDATED: Branded Top Bar
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("My Bookings") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = RoyalPalette.Navy, // Dark Blue background
                    titleContentColor = Color.White    // White text
                )
            )
        },
        containerColor = RoyalPalette.Background // Set branded background color
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            when (val s = state) {
                is UIState.Loading ->
                    CircularProgressIndicator(Modifier.align(Alignment.Center))

                is UIState.Error ->
                    Column(
                        Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Unable to load bookings")
                        Spacer(Modifier.height(12.dp))
                        Button(onClick = { vm.load() }) { Text("Retry") }
                    }

                is UIState.Success ->
                    BookingsList(
                        bookings = s.data,
                        // NOTE: This placeholder lambda is kept for compile safety
                        onExploreOffers = { /* vm.navigateToOffers() */ },
                        onCancel = { id -> vm.cancel(id) { /* could show snackbar */ } }
                    )
            }
        }
    }
}

// 🛳️ ENHANCED BookingsList with Branded UI
@Composable
private fun BookingsList(
    bookings: List<BookingDto>,
    onExploreOffers: () -> Unit,
    onCancel: (String) -> Unit
) {
    if (bookings.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 32.dp)
            ) {
                // Branded Title (Already done by user)
                Text(
                    text = "No Upcoming Voyages",
                    style = MaterialTheme.typography.titleLarge.copy(color = RoyalPalette.Navy)
                )
                Spacer(Modifier.height(8.dp))
                // Encouraging Body Text (Already done by user)
                Text(
                    text = "Ready to set sail? Explore our best cruise offers and secure your next Royal Caribbean adventure!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(24.dp))
                // Primary CTA (Already done by user)
                Button(
                    onClick = onExploreOffers,
                    colors = ButtonDefaults.buttonColors(containerColor = RoyalPalette.Blue),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Explore Cruise Deals")
                }
            }
        }
        return
    }

    // 📜 UPDATED: LazyColumn for actual bookings
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp) // Increased spacing
    ) {
        items(bookings, key = { it.id }) { b ->
            // ⚓ UPDATED: Branded Booking Card
            Surface(
                shape = RoundedCornerShape(12.dp),
                tonalElevation = 6.dp, // Higher elevation for a premium card look
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(16.dp)) {
                    // Highlight Confirmation ID in Navy Blue and Bold
                    Text(
                        "Confirmation: ${b.confirmationId}",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = RoyalPalette.Navy
                        )
                    )
                    Spacer(Modifier.height(8.dp))

                    // Main guest details are slightly larger
                    Text("Guest: ${b.guestName}", style = MaterialTheme.typography.bodyLarge)
                    Text("Email: ${b.email}", style = MaterialTheme.typography.bodyMedium)

                    // Secondary details (Offer ID, Created At) are small and gray
                    Spacer(Modifier.height(8.dp))
                    Text("Offer ID: ${b.offerId}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    Text("Created: ${b.createdAt}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)

                    Spacer(Modifier.height(12.dp))

                    // Outlined Cancel Button (Error Color)
                    Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                        OutlinedButton(
                            onClick = { onCancel(b.id) },
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.error // Red for caution/error
                            ),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.error)
                        ) { Text("Cancel") }
                    }
                }
            }
        }
    }
}