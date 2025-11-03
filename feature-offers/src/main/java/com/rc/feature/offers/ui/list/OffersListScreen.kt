package com.rc.feature.offers.ui.list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rc.feature.offers.domain.OfferSummary
import com.rc.feature.offers.util.UIState
import com.rc.feature.offers.util.formatPrice

private object RoyalPalette {
    val Navy = Color(0xFF061556)
    val Blue = Color(0xFF0073BB)
    val Background = Color(0xFFF8FAFB)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OffersListScreen(
    onOfferClick: (String) -> Unit,
    viewModel: OffersListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) { viewModel.loadOffers() }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Exclusive Offers") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = RoyalPalette.Navy,
                    titleContentColor = Color.White
                )
            )
        },
        containerColor = RoyalPalette.Background
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            when (val s = state) {
                is UIState.Loading -> {
                    LoadingUi()
                }

                is UIState.Error -> {
                    ErrorUi(onRetry = { viewModel.loadOffers() })
                }

                is UIState.Success -> {
                    OffersListContent(
                        offers = s.data,
                        onOfferClick = onOfferClick
                    )
                }

                else -> Unit // Ensures 'when' is exhaustive
            }
        }
    }
}

@Composable
private fun LoadingUi() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorUi(onRetry: () -> Unit) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Failed to load offers.")
            Spacer(Modifier.height(16.dp))
            Button(onClick = onRetry) {
                Text("Retry")
            }
        }
    }
}

@Composable
private fun OffersListContent(
    offers: List<OfferSummary>,
    onOfferClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(offers, key = { it.id }) { offer ->
            OfferCard(offer = offer, onClick = { onOfferClick(offer.id) })
        }
    }
}

@Composable
private fun OfferCard(offer: OfferSummary, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        tonalElevation = 6.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            AsyncImage(
                model = offer.image,
                contentDescription = offer.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )
            Column(Modifier.padding(16.dp)) {
                Text(
                    offer.title,
                    style = MaterialTheme.typography.titleLarge.copy(color = RoyalPalette.Navy),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    offer.shortDescription,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    formatPrice(offer.price),
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = RoyalPalette.Blue,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}
