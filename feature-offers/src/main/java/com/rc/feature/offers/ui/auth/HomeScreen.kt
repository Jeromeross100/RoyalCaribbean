package com.rc.feature.offers.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rc.feature.offers.theme.RCIndigo
import com.rc.feature.offers.theme.RCLilac

@Composable
fun HomeScreen(
    onExploreOffers: () -> Unit,
    onSignIn: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text("Welcome aboard 👋", style = MaterialTheme.typography.headlineMedium, color = RCIndigo)
        Spacer(Modifier.height(16.dp))

        // Hero card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(190.dp)
                .shadow(4.dp, RoundedCornerShape(24.dp))
                .clip(RoundedCornerShape(24.dp))
        ) {
            AsyncImage(
                model = "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=1200",
                contentDescription = "Paradise",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color(0x55000000), Color(0xC0000000)),
                            startY = 0f, endY = Float.POSITIVE_INFINITY
                        )
                    )
            )
            Column(
                Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Text(
                    "Sail into Paradise",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White
                )
                Text(
                    "Exclusive offers & curated itineraries",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White.copy(alpha = 0.9f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Spacer(Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            ElevatedButton(onClick = onExploreOffers) { Text("Explore Offers") }
            OutlinedButton(onClick = onSignIn) { Text("Sign In") }
        }

        Spacer(Modifier.height(16.dp))
        Surface(
            color = RCLilac,
            tonalElevation = 2.dp,
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(16.dp)) {
                Text("Tip", style = MaterialTheme.typography.titleMedium, color = RCIndigo)
                Spacer(Modifier.height(4.dp))
                Text(
                    "Tap an offer to see full details, itinerary timeline, and book with one tap.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
