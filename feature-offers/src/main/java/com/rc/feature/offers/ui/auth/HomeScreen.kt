package com.rc.feature.offers.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

// --- COLOR DEFINITIONS (Now definitive for this file) ---
// In a real project, these would be in your theme.kt file.
val RCSkyBlue = Color(0xFF0077FF) // Royal Caribbean's vibrant blue
val RCGold = Color(0xFFFFD700) // Gold accent
val RCPaleBlue = Color(0xFFE0F2F7) // Very light blue for backgrounds
val RCIndigo = Color(0xFF0A183C) // Darker blue for text
val RCLilac = Color(0xFFE8EAF6) // A soft, subtle background accent
// --------------------------------------------------------

@Composable
fun HomeScreen(
    onExploreOffers: () -> Unit,
    onSignIn: () -> Unit,
    onViewDestinations: () -> Unit, // New callback
    onLearnMoreLoyalty: () -> Unit // New callback
) {
    // REMOVED THE OUTER SCAFFOLD AND ITS BOTTOM BAR DEFINITION
    // The paddingValues will now come from the AppNavHost's Scaffold
    Column(
        modifier = Modifier
            .fillMaxSize()
            // The padding from the AppNavHost's Scaffold is passed implicitly here
            // via the parent NavHost's modifier, which already has paddingValues applied.
            // If you need direct access to paddingValues, the HomeScreen itself
            // should not have a Scaffold. We'll simulate it for now.
            // For a real app, you'd usually pass it as a parameter if HomeScreen needed it directly.
            // But since AppNavHost passes padding to NavHost, and NavHost content
            // uses fillMaxSize, the inner content will respect the padding.
            .verticalScroll(rememberScrollState()) // Make the content scrollable
            .background(RCPaleBlue) // Light background for the whole screen
            .padding(horizontal = 16.dp, vertical = 12.dp) // Maintain inner padding
    ) {
        // Welcome Section
        Text("Welcome aboard! 👋", style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold), color = RCIndigo)
        Text("Your adventure starts here.", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(20.dp))

        // Hero card - More dynamic image and content
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp) // Slightly taller hero card
                .shadow(8.dp, RoundedCornerShape(28.dp)) // More pronounced shadow
                .clip(RoundedCornerShape(28.dp))
        ) {
            AsyncImage(
                model = "https://images.unsplash.com/photo-1549468940-0ce96adcb41d?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1770&q=80", // A grand cruise ship or vibrant destination
                contentDescription = "Royal Caribbean Cruise",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color(0xC0000000)), // More subtle top gradient
                            startY = 0.4f, endY = Float.POSITIVE_INFINITY
                        )
                    )
            )
            Column(
                Modifier
                    .align(Alignment.BottomStart)
                    .padding(20.dp) // More padding for hero text
            ) {
                Text(
                    "Embark on a Journey of Discovery",
                    style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold), // Larger, bolder title
                    color = Color.White
                )
                Text(
                    "Explore breathtaking destinations and unparalleled experiences with Royal Caribbean.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White.copy(alpha = 0.9f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Spacer(Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) { // Increased spacing
            ElevatedButton(
                onClick = onExploreOffers,
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = RCSkyBlue, // Royal Caribbean blue button
                    contentColor = Color.White
                ),
                modifier = Modifier.weight(1f)
            ) {
                Text("Explore Offers", style = MaterialTheme.typography.bodyLarge)
            }
            OutlinedButton(
                onClick = onSignIn,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = RCIndigo), // Dark blue text for outline button
                border = ButtonDefaults.outlinedButtonBorder(enabled = true).copy(brush = Brush.horizontalGradient(listOf(RCIndigo, RCSkyBlue))),
                modifier = Modifier.weight(1f)
            ) {
                Text("Sign In", style = MaterialTheme.typography.bodyLarge)
            }
        }

        Spacer(Modifier.height(24.dp)) // More space for new sections

        // Featured Destinations Section
        Text("Featured Destinations", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold), color = RCIndigo)
        Spacer(Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
            DestinationCard(
                imageUrl = "https://images.unsplash.com/photo-1516089228551-71fb355c7a36?w=800",
                title = "Caribbean Escape",
                modifier = Modifier.weight(1f) // Apply weight here
            )
            DestinationCard(
                imageUrl = "https://images.unsplash.com/photo-1549468940-0ce96adcb41d?w=800",
                title = "Alaskan Wonders",
                modifier = Modifier.weight(1f) // Apply weight here
            )
        }
        Spacer(Modifier.height(12.dp))
        Button(
            onClick = onViewDestinations,
            colors = ButtonDefaults.buttonColors(containerColor = RCIndigo), // Dark blue button
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("View All Destinations", color = Color.White)
        }


        Spacer(Modifier.height(24.dp))

        // Loyalty Program / Insider Info
        Card(
            colors = CardDefaults.cardColors(containerColor = RCLilac.copy(alpha = 0.7f)),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Filled.Star, contentDescription = "Star", tint = RCGold)
                    Spacer(Modifier.width(8.dp))
                    Text("Crown & Anchor Society", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold), color = RCIndigo)
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    "Become a member of our exclusive loyalty program and unlock a world of benefits, from priority boarding to onboard discounts. Your loyalty truly pays off!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(12.dp))
                TextButton(onClick = onLearnMoreLoyalty) {
                    Text("Learn More", color = RCSkyBlue, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold))
                }
            }
        }

        Spacer(Modifier.height(24.dp)) // Final spacing
    }
}

@Composable
fun DestinationCard(imageUrl: String, title: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier, // Accept the modifier passed from the parent Row
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            AsyncImage(
                model = imageUrl,
                contentDescription = title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            )
            Text(
                title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(8.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}