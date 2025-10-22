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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rc.feature.offers.domain.OfferDetails
import com.rc.feature.offers.util.UIState
import com.rc.feature.offers.util.formatPrice

@Composable
fun OfferDetailsScreen(
    offerId: String,
    onBack: () -> Unit,
    viewModel: OfferDetailsViewModel = hiltViewModel<OfferDetailsViewModel>()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(offerId) {
        viewModel.load(offerId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Offer Details") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        bottomBar = {
            if (state is UIState.Success<OfferDetails>) {
                val details = (state as UIState.Success<OfferDetails>).data
                Surface(shadowElevation = 2.dp) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = formatPrice(details.price),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Button(onClick = { /* non-functional placeholder */ }) {
                            Text("Book Now")
                        }
                    }
                }
            }
        }
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
            }
        }
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
private fun DetailsContent(details: OfferDetails) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        AsyncImage(
            model = details.image,
            contentDescription = details.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp),
            contentScale = ContentScale.Crop
        )
        Text(details.title, style = MaterialTheme.typography.headlineSmall)
        Text(formatPrice(details.price), style = MaterialTheme.typography.titleMedium)
        Text(details.description, style = MaterialTheme.typography.bodyLarge)
        Text("Itinerary", style = MaterialTheme.typography.titleMedium)
        Text(details.itinerary, style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(80.dp))
    }
}
