// app/src/main/java/com/rc/feature/offers/ui/navigation/RootNavHost.kt
package com.rc.feature.offers.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.compose.*
import coil.compose.AsyncImage
import com.rc.feature.offers.bookings.BookingsScreen
import com.rc.feature.offers.schedule.ScheduleScreen
import com.rc.feature.offers.profile.ProfileScreen
import com.rc.feature.offers.ui.auth.AuthViewModel
import com.rc.feature.offers.ui.auth.RoyalAuthScreen

// ----- Top-level routes -----
private object RootRoutes {
    const val HOME = "home"
    const val OFFERS = OffersRoutes.LIST
    const val BOOKINGS = "bookings"
    const val SCHEDULE = "schedule"
    const val LOGIN = "login"
    const val PROFILE = "profile"
}

// ----- Bottom bar items -----
private data class BottomItem(
    val route: String,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

private val bottomItems = listOf(
    BottomItem(RootRoutes.HOME, "Home", Icons.Default.Home),
    BottomItem(RootRoutes.OFFERS, "Offers", Icons.Default.LocalOffer),
    BottomItem(RootRoutes.BOOKINGS, "Bookings", Icons.Default.ReceiptLong),
    BottomItem(RootRoutes.SCHEDULE, "Schedule", Icons.Default.CalendarMonth)
)

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val backStack by navController.currentBackStackEntryAsState()
    val current = backStack?.destination

    Scaffold(
        bottomBar = {
            NavigationBar {
                bottomItems.forEach { item ->
                    val selected = current.isInHierarchy(item.route)
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = RootRoutes.HOME,
            modifier = Modifier.padding(innerPadding)
        ) {
            // Home
            composable(RootRoutes.HOME) {
                HomeScreen(
                    onExplore = { navController.navigate(RootRoutes.OFFERS) },
                    onLogin = { navController.navigate(RootRoutes.LOGIN) }
                )
            }

            // Offers feature subgraph (list + details)
            offersGraphFeature(navController)

            // Tabs
            composable(RootRoutes.BOOKINGS) { BookingsScreen() }
            composable(RootRoutes.SCHEDULE) { ScheduleScreen() }

            // LOGIN -> PROFILE on success (UPDATED)
            composable(RootRoutes.LOGIN) {
                RoyalAuthScreen(
                    onSuccess = {
                        navController.navigate(RootRoutes.PROFILE) {
                            popUpTo(RootRoutes.HOME) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }

            // PROFILE destination (UPDATED)
            composable(RootRoutes.PROFILE) {
                val authVm = androidx.hilt.navigation.compose.hiltViewModel<AuthViewModel>()
                ProfileScreen(userProvider = { authVm.state.value.user })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(
    onExplore: () -> Unit,
    onLogin: () -> Unit
) {
    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("Welcome aboard 👋") }) }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(20.dp))
            ) {
                AsyncImage(
                    model = "https://images.unsplash.com/photo-1507525428034-b723cf961d3e",
                    contentDescription = "Hero",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize()
                )
                Box(
                    Modifier
                        .matchParentSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color.Transparent, Color(0x99000000))
                            )
                        )
                )
                Column(
                    Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Text("Sail into Paradise", style = MaterialTheme.typography.headlineSmall, color = Color.White)
                    Text("Exclusive offers & curated itineraries", color = Color.White.copy(alpha = 0.9f))
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ElevatedButton(onClick = onExplore) { Text("Explore Offers") }
                OutlinedButton(onClick = onLogin) { Text("Sign In") }
            }

            Surface(tonalElevation = 1.dp, shape = RoundedCornerShape(16.dp)) {
                Column(
                    Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Tip", style = MaterialTheme.typography.titleMedium)
                    Text(
                        "Tap an offer to see full details, itinerary timeline, and book with one tap.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

private fun NavDestination?.isInHierarchy(route: String): Boolean {
    var d = this
    while (d != null) { if (d.route == route) return true; d = d.parent }
    return false
}
