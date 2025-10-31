// feature-offers/src/main/java/com/rc/feature/offers/ui/auth/HomeScreen.kt
// Removed @file:OptIn(ExperimentalMaterial3Api::class) as it was redundant.

package com.rc.feature.offers.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Sailing // New Icon
import androidx.compose.material.icons.filled.Anchor // New Icon
import androidx.compose.material.icons.filled.AccountBalanceWallet // New Icon
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector // Needed for UtilityIcon
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

// --- Data Structures for Dashboard (Needed for HomeScreen signature) ---
data class User(val firstName: String)
data class UpcomingVoyage(
    val shipName: String,
    val sailDate: String,
    val remainingDays: Int,
    val currentPort: String,
    val imagePlaceholder: String // Using a string URL placeholder
)

// --- COLOR DEFINITIONS (Unified RC Branding) ---
val RCNavy = Color(0xFF061556)    // Dark Blue
val RCBlue = Color(0xFF0073BB)    // Primary Blue
val RCGold = Color(0xFFFEBD11)    // Gold accent
val RCLightGray = Color(0xFFF8FAFB) // Light Off-White
val RCLilac = Color(0xFFE8EAF6)    // Soft accent for loyalty card
// --------------------------------------------------------

@Composable
fun HomeScreen(
    user: User? = null,
    upcomingVoyage: UpcomingVoyage? = null,
    onExploreOffers: () -> Unit,
    onSignIn: () -> Unit,
    onViewDestinations: () -> Unit,
    onLearnMoreLoyalty: () -> Unit,
    onViewSchedule: () -> Unit = {},
    onViewCheckin: () -> Unit = {},
    onNavigateToShipUtility: () -> Unit = {},
    onNavigateToPortPlanner: () -> Unit = {},
    onNavigateToWallet: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(RCLightGray)
            .padding(horizontal = 16.dp, vertical = 20.dp)
    ) {
        // === CORE LOGIC: SWITCH BETWEEN DASHBOARD AND LOGGED OUT VIEW ===
        if (user != null && upcomingVoyage != null) {
            VoyageDashboardContent(
                user = user,
                voyage = upcomingVoyage,
                onViewSchedule = onViewSchedule,
                onViewCheckin = onViewCheckin,
                onNavigateToWallet = onNavigateToWallet
            )
        } else {
            LoggedOutHomeContent(
                onExploreOffers = onExploreOffers, // Passed to content
                onSignIn = onSignIn
            )
        }

        // --- QUICK ACCESS TOOLS (ALWAYS VISIBLE) ---
        Spacer(Modifier.height(32.dp))

        Text(
            "Quick Access Tools",
            style = MaterialTheme.typography.titleLarge.copy(color = RCNavy, fontWeight = FontWeight.SemiBold)
        )
        Spacer(Modifier.height(12.dp))

        // New Row integrating the three utility screens
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            UtilityIcon(
                label = "Ship Map",
                icon = Icons.Default.Sailing,
                onClick = onNavigateToShipUtility,
                modifier = Modifier.weight(1f)
            )
            UtilityIcon(
                label = "Port Day",
                icon = Icons.Default.Anchor,
                onClick = onNavigateToPortPlanner,
                modifier = Modifier.weight(1f)
            )
            UtilityIcon(
                label = "My Wallet",
                icon = Icons.Default.AccountBalanceWallet,
                onClick = onNavigateToWallet,
                modifier = Modifier.weight(1f)
            )
        }
        // --------------------------------------------

        // --- LOGGED OUT EXPLORE SECTION (Visible when no cruise is booked) ---
        if (user == null || upcomingVoyage == null) {
            Spacer(Modifier.height(32.dp))
            LoggedOutExploreSection(onExploreOffers, onViewDestinations, onLearnMoreLoyalty)
        }
    }
}


// --------------------------------------------------------------------
// === VOYAGE DASHBOARD CONTENT ===
// --------------------------------------------------------------------

@Composable
private fun VoyageDashboardContent(
    user: User,
    voyage: UpcomingVoyage,
    onViewSchedule: () -> Unit,
    onViewCheckin: () -> Unit,
    onNavigateToWallet: () -> Unit
) {
    // 1. Welcome & Title
    Text(
        "Welcome Back, ${user.firstName}!",
        style = MaterialTheme.typography.headlineMedium.copy(color = RCNavy, fontWeight = FontWeight.Bold),
        modifier = Modifier.padding(top = 4.dp)
    )
    Spacer(Modifier.height(16.dp))

    // 2. Main Voyage Banner
    VoyageBanner(voyage)

    Spacer(Modifier.height(24.dp))

    // 3. Quick Actions (Schedule & Check-in)
    Text(
        "Voyage Quick Links",
        style = MaterialTheme.typography.titleLarge.copy(color = RCNavy, fontWeight = FontWeight.SemiBold)
    )
    Spacer(Modifier.height(12.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        DashboardActionButton(
            label = "View Schedule",
            onClick = onViewSchedule,
            modifier = Modifier.weight(1f)
        )
        DashboardActionButton(
            label = "Check-in Status",
            onClick = onViewCheckin,
            modifier = Modifier.weight(1f)
        )
    }

    Spacer(Modifier.height(24.dp))

    // 4. Wallet/Account Summary Card
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth().clickable(onClick = onNavigateToWallet)
    ) {
        Column(Modifier.padding(20.dp)) {
            Text("Your Wallet", style = MaterialTheme.typography.titleLarge.copy(color = RCNavy, fontWeight = FontWeight.Bold))
            Spacer(Modifier.height(8.dp))
            Text(
                "Account Balance: \$150.00", // Hardcoded placeholder
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold, color = RCBlue)
            )
            HorizontalDivider(Modifier.padding(vertical = 12.dp))
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text("Crown & Anchor Tier:", style = MaterialTheme.typography.bodyMedium)
                Text("Diamond", style = MaterialTheme.typography.bodyMedium.copy(color = RCGold, fontWeight = FontWeight.SemiBold))
            }
        }
    }

    Spacer(Modifier.height(20.dp))
}

// --------------------------------------------------------------------
// === LOGGED OUT CONTENT (FIXED: onExploreOffers is now used below) ===
// --------------------------------------------------------------------

@Composable
private fun LoggedOutHomeContent(
    onExploreOffers: () -> Unit,
    onSignIn: () -> Unit
) {
    // Welcome Section
    Text(
        "Welcome aboard! 👋",
        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold),
        color = RCNavy
    )
    Text(
        "Your adventure starts here. Sign in to see your booked cruises.",
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
    Spacer(Modifier.height(24.dp))

    // Hero card - Dynamic, branded, and high-impact
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .shadow(10.dp, RoundedCornerShape(24.dp))
            .clip(RoundedCornerShape(24.dp))
    ) {
        AsyncImage(
            model = "https://images.unsplash.com/photo-1549468940-0ce96adcb41d?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1770&q=80",
            contentDescription = "Royal Caribbean Cruise",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, RCNavy.copy(alpha = 0.8f)),
                        startY = 0.3f, endY = Float.POSITIVE_INFINITY
                    )
                )
        )
        Column(
            Modifier
                .align(Alignment.BottomStart)
                .padding(20.dp)
        ) {
            Text(
                "Embark on a Journey of Discovery",
                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Black),
                color = RCGold
            )
            Text(
                "Unparalleled experiences await you.",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.9f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }

    Spacer(Modifier.height(24.dp))

    // Primary Call to Action Buttons
    Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()) {
        Button(
            onClick = onExploreOffers, // FIX: The onExploreOffers lambda is now used here
            colors = ButtonDefaults.buttonColors(containerColor = RCBlue),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.weight(1f).height(56.dp)
        ) {
            Text("Explore Offers", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
        }
        OutlinedButton(
            onClick = onSignIn,
            colors = ButtonDefaults.outlinedButtonColors(contentColor = RCNavy),
            border = ButtonDefaults.outlinedButtonBorder(enabled = true).copy(brush = Brush.horizontalGradient(listOf(RCBlue, RCNavy))),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.weight(1f).height(56.dp)
        ) {
            Text("Sign In", style = MaterialTheme.typography.bodyLarge)
        }
    }
}

// --------------------------------------------------------------------
// === NEW HELPER: EXPLORE SECTION ===
// --------------------------------------------------------------------

@Composable
private fun LoggedOutExploreSection(
    onExploreOffers: () -> Unit,
    onViewDestinations: () -> Unit,
    onLearnMoreLoyalty: () -> Unit
) {
    // Featured Destinations Section
    Text("Top Destinations", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold), color = RCNavy)
    Spacer(Modifier.height(16.dp))
    Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()) {
        DestinationCard(
            imageUrl = "https://images.unsplash.com/photo-1516089228551-71fb355c7a36?w=800",
            title = "The Caribbean",
            modifier = Modifier.weight(1f)
        )
        DestinationCard(
            imageUrl = "https://images.unsplash.com/photo-1549468940-0ce96adcb41d?w=800",
            title = "The Bahamas",
            modifier = Modifier.weight(1f)
        )
    }
    Spacer(Modifier.height(16.dp))
    Button(
        onClick = onViewDestinations,
        colors = ButtonDefaults.buttonColors(containerColor = RCNavy),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("View All Destinations", color = Color.White)
    }

    Spacer(Modifier.height(32.dp))

    // Loyalty Program / Insider Info Card (Branded)
    Card(
        colors = CardDefaults.cardColors(containerColor = RCLilac.copy(alpha = 0.8f)),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Filled.Star, contentDescription = "Star", tint = RCGold, modifier = Modifier.size(28.dp))
                Spacer(Modifier.width(10.dp))
                Text(
                    "Crown & Anchor Society",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                    color = RCNavy
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(
                "Unlock a world of benefits, from priority boarding to onboard discounts. Your loyalty truly pays off!",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(12.dp))
            TextButton(onClick = onLearnMoreLoyalty) {
                Text("Learn More & Join", color = RCBlue, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold))
            }
        }
    }

    Spacer(Modifier.height(20.dp))
}

// --------------------------------------------------------------------
// === UNCHANGED HELPER COMPOSABLES ===
// --------------------------------------------------------------------

@Composable
private fun VoyageBanner(voyage: UpcomingVoyage) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(modifier = Modifier.height(200.dp)) {
            // Ship Image (using placeholder URL)
            AsyncImage(
                model = "https://images.unsplash.com/photo-1579735414169-d4190c1f6d3f?q=80&w=2670&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                contentDescription = voyage.shipName,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Gradient overlay for text readability
            Box(
                modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.4f))
            )

            Column(
                modifier = Modifier.align(Alignment.BottomStart).padding(16.dp)
            ) {
                Text(
                    voyage.shipName,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold
                    )
                )
                Text(
                    "Sailing in ${voyage.remainingDays} Days • ${voyage.currentPort} Itinerary",
                    style = MaterialTheme.typography.titleMedium.copy(color = RCGold)
                )
            }
        }
    }
}

@Composable
private fun DashboardActionButton(label: String, onClick: () -> Unit, modifier: Modifier) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = RCBlue),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier.height(56.dp)
    ) {
        Text(label, color = Color.White, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun UtilityIcon(label: String, icon: ImageVector, onClick: () -> Unit, modifier: Modifier) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier.height(100.dp).clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = RCGold,
                modifier = Modifier.size(36.dp).background(RCNavy, RoundedCornerShape(50))
            )
            Spacer(Modifier.height(8.dp))
            Text(label, style = MaterialTheme.typography.labelMedium, color = RCNavy, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun DestinationCard(imageUrl: String, title: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
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
                    .height(110.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            )
            Text(
                title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = RCNavy,
                modifier = Modifier.padding(10.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoggedInHomeScreenPreview() {
    HomeScreen(
        user = User("Alex"),
        upcomingVoyage = UpcomingVoyage("Wonder of the Seas", "Nov 15", 30, "Miami", "url"),
        onExploreOffers = {},
        onSignIn = {},
        onViewDestinations = {},
        onLearnMoreLoyalty = {}
    )
}

@Preview(showBackground = true)
@Composable
fun LoggedOutHomeScreenPreview() {
    HomeScreen(
        onExploreOffers = {},
        onSignIn = {},
        onViewDestinations = {},
        onLearnMoreLoyalty = {}
    )
}