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

private object RoyalPalette {
    val Navy = Color(0xFF061556)
    val Blue = Color(0xFF0073BB)
    val Background = Color(0xFFF8FAFB)
}

@Composable
fun OfferDetailsRoute(
    offerId: String,
    onBack: () -> Unit = {},
    viewModel: OfferDetailsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(offerId) { viewModel.load(offerId) }

    Scaffold(
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
        bottomBar = {
            val s = state
            if (s is UIState.Success<OfferDetails>) {
                Surface(shadowElevation = 8.dp) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            formatPrice(s.data.price),
                            style = MaterialTheme.typography.headlineSmall.copy(
                                color = RoyalPalette.Blue,
                                fontWeight = FontWeight.ExtraBold
                            )
                        )
                        Button(
                            onClick = { /* TODO: handle booking */ },
                            colors = ButtonDefaults.buttonColors(containerColor = RoyalPalette.Blue),
                            modifier = Modifier.height(52.dp)
                        ) {
                            Text("Book Now", color = Color.White)
                        }
                    }
                }
            }
        },
        containerColor = RoyalPalette.Background
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (val s = state) {
                is UIState.Loading -> {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }

                is UIState.Error -> {
                    ErrorUi(onRetry = { viewModel.load(offerId) })
                }

                is UIState.Success<OfferDetails> -> {
                    DetailsContent(details = s.data)
                }

                else -> Unit // ✅ makes 'when' exhaustive
            }
        }
    }
}

@Composable
private fun DetailsContent(details: OfferDetails) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AsyncImage(
            model = details.image,
            contentDescription = details.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            contentScale = ContentScale.Crop
        )

        Text(
            details.title,
            style = MaterialTheme.typography.headlineMedium.copy(color = RoyalPalette.Navy)
        )

        Text(details.description, style = MaterialTheme.typography.bodyLarge)

        Text(
            "Itinerary",
            style = MaterialTheme.typography.titleLarge.copy(
                color = RoyalPalette.Navy,
                fontWeight = FontWeight.SemiBold
            )
        )
        Text(details.itinerary, style = MaterialTheme.typography.bodyMedium)

        Spacer(Modifier.height(80.dp))
    }
}

@Composable
private fun ErrorUi(onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Unable to load offer")
        Spacer(Modifier.height(12.dp))
        Button(onClick = onRetry) { Text("Retry") }
    }
}

@Composable
fun OfferDetailsScreen(
    offerId: String,
    viewModel: OfferDetailsViewModel = hiltViewModel()
) {
    OfferDetailsRoute(offerId = offerId, onBack = {}, viewModel = viewModel)
}
