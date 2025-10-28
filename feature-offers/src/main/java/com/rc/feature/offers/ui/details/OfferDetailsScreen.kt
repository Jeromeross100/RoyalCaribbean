// feature-offers/src/main/java/com/rc/feature/offers/ui/details/OfferDetailsScreen.kt
@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.rc.feature.offers.ui.details

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rc.feature.offers.domain.OfferDetails
import com.rc.feature.offers.util.UIState
import com.rc.feature.offers.util.formatPrice

// ROYAL CARIBBEAN PALETTE DEFINITION
// NOTE: Ideally, this should be defined in a shared theme file.
private object RoyalPalette {
    val Navy = Color(0xFF061556) // Dark Blue / Oxford Blue (for headers, strong text)
    val Blue = Color(0xFF0073BB) // Primary Blue / French Blue (for CTAs, active states)
    val Background = Color(0xFFF8FAFB) // Light Off-White
}

/**
 * ROUTE — owns VM/state and exposes a safe default for onBack.
 */
@Composable
fun OfferDetailsRoute(
    offerId: String,
    onBack: () -> Unit = {},
    viewModel: OfferDetailsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(offerId) { viewModel.load(offerId) }

    Scaffold(
        // 🚢 UPDATED: Branded Top Bar
        topBar = {
            TopAppBar(
                title = { Text("Offer Details") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = RoyalPalette.Navy,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        // ⚓ UPDATED: Branded Bottom Bar (Price and Book Now CTA)
        bottomBar = {
            val s = state
            if (s is UIState.Success<OfferDetails>) {
                Surface(shadowElevation = 8.dp) { // Increased shadow for floating bar look
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            formatPrice(s.data.price),
                            style = MaterialTheme.typography.headlineSmall.copy(
                                color = RoyalPalette.Blue, // Highlight price in brand blue
                                fontWeight = FontWeight.ExtraBold
                            )
                        )
                        Button(
                            onClick = { /* placeholder */ },
                            // Use primary brand blue for the CTA
                            colors = ButtonDefaults.buttonColors(containerColor = RoyalPalette.Blue),
                            modifier = Modifier.height(52.dp)
                        ) { Text("Book Now", color = Color.White) }
                    }
                }
            }
        },
        containerColor = RoyalPalette.Background
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            when (val s = state) {
                is UIState.Loading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
                is UIState.Error -> ErrorUi(onRetry = { viewModel.load(offerId) })
                is UIState.Success<OfferDetails> -> DetailsContent(details = s.data)
            }
        }
    }
}

// NOTE: Removing the redundant OfferDetailsScreen composable with the full Scaffold logic
// and keeping only the content function, as the Route handles the Scaffold.

/**
 * UI — stateless, reusable content-only screen (no VM here).
 */
@Composable
private fun DetailsContent(details: OfferDetails) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp), // Only horizontal padding here
        verticalArrangement = Arrangement.spacedBy(16.dp) // Increased spacing
    ) {
        // Use LazyColumn or Scrollable Column if the content exceeds screen height
        // For simplicity, sticking to Column but adding Scroll state awareness is recommended.

        AsyncImage(
            model = details.image,
            contentDescription = details.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp), // Taller image
            contentScale = ContentScale.Crop
        )

        // Branded Title
        Text(
            details.title,
            style = MaterialTheme.typography.headlineMedium.copy(color = RoyalPalette.Navy)
        )

        // Price is now only in the bottom bar, removing it here to avoid duplication

        Text(details.description, style = MaterialTheme.typography.bodyLarge)

        // Branded Itinerary Header
        Text(
            "Itinerary",
            style = MaterialTheme.typography.titleLarge.copy(color = RoyalPalette.Navy, fontWeight = FontWeight.SemiBold)
        )
        Text(details.itinerary, style = MaterialTheme.typography.bodyMedium)

        Spacer(Modifier.height(80.dp)) // Spacer to push content above the bottom bar
    }
}

@Composable
private fun ErrorUi(onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Unable to load offer")
        Spacer(Modifier.height(12.dp))
        Button(onClick = onRetry) { Text("Retry") }
    }
}

/**
 * Backward-compat overload so any old call sites still compile even if they used the old signature.
 * Internally fetches via VM and uses a no-op back action.
 */
@Composable
fun OfferDetailsScreen(
    offerId: String,
    viewModel: OfferDetailsViewModel = hiltViewModel()
) {
    // Delegate to the route with a default onBack
    OfferDetailsRoute(offerId = offerId, onBack = {}, viewModel = viewModel)
}