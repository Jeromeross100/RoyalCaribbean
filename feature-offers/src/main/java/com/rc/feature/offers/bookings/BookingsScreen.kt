package com.rc.feature.offers.bookings

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.rc.feature.offers.data.graphql.BookingDto
import com.rc.feature.offers.util.UIState

// ROYAL CARIBBEAN PALETTE DEFINITION
private object RoyalPalette {
    val Navy = Color(0xFF061556)
    val Blue = Color(0xFF0073BB)
    val Background = Color(0xFFF8FAFB)
}

// UPDATED: NEW WORKING CRUISE SHIP IMAGE URL (Source: Unsplash)
private const val ROYAL_SHIP_IMAGE_URL =
    "https://picsum.photos/id/238/300/200"

// ⚓ CONSTANT FOR THE LOG TAG
private const val LOG_TAG = "SHIP_IMAGE"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingsScreen(
    // CRITICAL UPDATE: Add navigation lambda parameter
    onNavigateToOffers: () -> Unit,
    vm: BookingsViewModel = hiltViewModel()
) {
    val state by vm.state.collectAsState()
    var bookingToCancel by remember { mutableStateOf<BookingDto?>(null) }

    LaunchedEffect(Unit) { vm.load() }

    // Show Cancellation Dialog
    bookingToCancel?.let { booking ->
        CancellationDialog(
            booking = booking,
            onConfirm = {
                vm.cancel(booking.id) { /* could show snackbar */ }
                bookingToCancel = null
            },
            onDismiss = { bookingToCancel = null }
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("My Bookings") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = RoyalPalette.Navy,
                    titleContentColor = Color.White
                )
            )
        },
        containerColor = RoyalPalette.Background
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
                        // CRITICAL UPDATE: Pass the navigation action
                        onExploreOffers = onNavigateToOffers,
                        onCancel = { bookingToCancel = it }
                    )
            }
        }
    }
}

// ENHANCED BookingsList with Branded UI
@Composable
private fun BookingsList(
    bookings: List<BookingDto>,
    onExploreOffers: () -> Unit, // Navigation action passed here
    onCancel: (BookingDto) -> Unit
) {
    if (bookings.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 32.dp)
            ) {
                // Image loading logic
                val context = LocalContext.current

                AsyncImage(
                    // USES THE NEW WORKING ROYAL_SHIP_IMAGE_URL
                    model = ImageRequest.Builder(context)
                        .data(ROYAL_SHIP_IMAGE_URL)
                        .listener(
                            onStart = { Log.d(LOG_TAG, "Image load START for: $ROYAL_SHIP_IMAGE_URL") },
                            onSuccess = { _, _ -> Log.d(LOG_TAG, "Image loaded SUCCESSFULLY!") },
                            onError = { _, result ->
                                Log.e(LOG_TAG, "Image load FAILED! Throwable: ${result.throwable.message}", result.throwable)
                            }
                        )
                        .build(),
                    contentDescription = "Royal Caribbean Cruise Ship",
                    placeholder = painterResource(android.R.drawable.ic_menu_help),
                    error = painterResource(android.R.drawable.ic_menu_help),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(200.dp)
                        .padding(bottom = 16.dp),
                )

                // Branded Title
                Text(
                    text = "No Upcoming Voyages",
                    style = MaterialTheme.typography.titleLarge.copy(color = RoyalPalette.Navy)
                )
                Spacer(Modifier.height(8.dp))
                // Encouraging Body Text
                Text(
                    text = "Ready to set sail? Explore our best cruise offers and secure your next Royal Caribbean adventure!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(24.dp))
                // Primary CTA
                Button(
                    // CONNECTED: Clicks now execute the onExploreOffers lambda (navigation)
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

    // LazyColumn for actual bookings (remains the same)
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(bookings, key = { it.id }) { b ->
            // ... (Booking Card UI remains the same) ...
        }
    }
}

// Cancellation Confirmation Dialog (remains the same)
@Composable
private fun CancellationDialog(
    booking: BookingDto,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = { Icon(imageVector = Icons.Default.Warning, contentDescription = "Warning") },
        title = { Text("Confirm Cancellation") },
        text = {
            Text(
                "Are you sure you want to cancel booking ${booking.confirmationId} " +
                        "for guest ${booking.guestName}? This action cannot be undone."
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Confirm Cancel")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Keep Booking")
            }
        }
    )
}