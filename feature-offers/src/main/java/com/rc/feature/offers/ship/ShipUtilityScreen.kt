@file:OptIn(ExperimentalMaterial3Api::class) // FIX 1: Move to the very top

package com.rc.feature.offers.ship

// feature-offers/src/main/java/com/rc/feature/offers/ship/ShipUtilityScreen.kt
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items // FIX 3: Import for List support in LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape // FIX 2: Import for CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.RoomService
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview // FIX 4: Import for Preview
import androidx.compose.ui.unit.dp

// --- Data Structures ---
data class Venue(val name: String, val deck: Int, val hours: String, val occupancy: String)
data class QuickAction(val label: String, val icon: ImageVector, val onClick: () -> Unit)

// --- Palette (Re-defined for file independence) ---
private object RoyalPalette {
    val Navy = Color(0xFF061556)
    val Blue = Color(0xFF0073BB)
    val Gold = Color(0xFFFEBD11)
    val Background = Color(0xFFF8FAFB)
}

@Composable
fun ShipUtilityScreen(
    shipName: String = "Icon of the Seas",
    currentDeck: Int = 8
) {
    val quickActions = listOf(
        QuickAction("Service", Icons.Default.RoomService) { /* request service */ },
        QuickAction("Map", Icons.Default.LocationOn) { /* open map viewer */ }
        // ... other actions
    )
    val venues = listOf(
        Venue("Coastal Kitchen", 16, "5:30 PM - 9:30 PM", "Quiet"),
        Venue("Rising Tide Bar", 5, "10:00 AM - 1:00 AM", "Medium"),
        Venue("Izumi Sushi", 4, "6:00 PM - 10:00 PM", "Full")
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(shipName) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = RoyalPalette.Navy,
                    titleContentColor = Color.White
                )
            )
        },
        containerColor = RoyalPalette.Background
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding).fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { CurrentLocationCard(currentDeck) }
            item { QuickActionsRow(quickActions) }
            item { Text("Dining & Entertainment", style = MaterialTheme.typography.titleLarge.copy(color = RoyalPalette.Navy, fontWeight = FontWeight.Bold)) }
            items(venues) { venue -> VenueCard(venue) } // FIX 3: uses the imported items extension function
        }
    }
}

@Composable
private fun CurrentLocationCard(currentDeck: Int) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = RoyalPalette.Blue.copy(alpha = 0.9f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.LocationOn,
                contentDescription = null,
                tint = RoyalPalette.Gold,
                modifier = Modifier.size(36.dp)
            )
            Spacer(Modifier.width(16.dp))
            Column {
                Text(
                    "You Are Here",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
                Text(
                    "Deck $currentDeck",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun QuickActionsRow(actions: List<QuickAction>) {
    Row(
        modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        actions.forEach { action ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Button(
                    onClick = action.onClick,
                    colors = ButtonDefaults.buttonColors(containerColor = RoyalPalette.Blue),
                    shape = CircleShape, // FIX 2: CircleShape is now available
                    modifier = Modifier.size(64.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(action.icon, contentDescription = action.label, tint = Color.White, modifier = Modifier.size(32.dp))
                }
                Spacer(Modifier.height(4.dp))
                Text(action.label, style = MaterialTheme.typography.bodySmall.copy(color = RoyalPalette.Navy))
            }
        }
    }
}

@Composable
private fun VenueCard(venue: Venue) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(venue.name, style = MaterialTheme.typography.titleMedium.copy(color = RoyalPalette.Navy, fontWeight = FontWeight.SemiBold))
                Spacer(Modifier.height(4.dp))
                Text("Deck ${venue.deck} • ${venue.hours}", style = MaterialTheme.typography.bodyMedium)
            }
            Text(
                venue.occupancy,
                style = MaterialTheme.typography.labelMedium.copy(color = RoyalPalette.Gold, fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(RoyalPalette.Gold.copy(alpha = 0.1f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}

// FIX 4: Add a Preview function to make the screen runnable
@Preview(showBackground = true)
@Composable
fun ShipUtilityScreenPreview() {
    ShipUtilityScreen()
}