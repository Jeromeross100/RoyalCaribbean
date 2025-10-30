// app/src/main/java/.../AppNavHost.kt
package com.rc.feature.offers.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.rc.feature.offers.auth.AuthViewModel
import com.rc.feature.offers.auth.AuthUiState // NOTE: Assuming this class exists for ProfileScreen logic
import com.rc.feature.offers.profile.ProfileScreen
import com.rc.feature.offers.bookings.BookingsScreen
import com.rc.feature.offers.schedule.ScheduleScreen
import com.rc.feature.offers.ui.details.OfferDetailsRoute
import com.rc.feature.offers.ui.list.OffersListScreen
import com.rc.feature.offers.ui.auth.HomeScreen
import com.rc.feature.offers.ui.auth.RoyalAuthScreen // <-- NEW IMPORT

object RootRoutes {
    const val HOME = "home"
    const val OFFERS = "offers"
    const val BOOKINGS = "bookings"
    const val SCHEDULE = "schedule"
    const val OFFER_DETAILS_PATTERN = "offer/{id}"
    fun offerDetails(id: String) = "offer/$id"
    // RENAMED from LOGIN/SIGNUP to a single AUTH route
    const val AUTH = "auth"
    const val PROFILE = "profile"
    // New destination for Destinations Screen, assuming it exists
    const val DESTINATIONS = "destinations"
    const val LOYALTY = "loyalty" // New destination for Loyalty Screen
}

@Composable
fun AppNavHost() {
    val nav = rememberNavController()
    val items = listOf(
        RootRoutes.HOME to Icons.Filled.Home,
        RootRoutes.OFFERS to Icons.Filled.LocalOffer,
        RootRoutes.BOOKINGS to Icons.AutoMirrored.Filled.ReceiptLong,
        RootRoutes.SCHEDULE to Icons.Filled.CalendarMonth
    )
    val backstack by nav.currentBackStackEntryAsState()
    val route = backstack?.destination?.route

    // Hide bottom bar on auth & details screens
    val showBottomBar = route !in setOf(
        RootRoutes.AUTH, // <-- Updated from LOGIN/SIGNUP
        RootRoutes.OFFER_DETAILS_PATTERN
    )

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    items.forEach { (r, icon) ->
                        NavigationBarItem(
                            selected = route == r,
                            onClick = {
                                nav.navigate(r) {
                                    launchSingleTop = true
                                    popUpTo(RootRoutes.HOME) { saveState = true }
                                    restoreState = true
                                }
                            },
                            icon = { Icon(icon, null) },
                            label = { Text(r.replaceFirstChar { it.uppercase() }) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = nav,
            startDestination = RootRoutes.HOME,
            modifier = Modifier.padding(innerPadding).fillMaxSize(),
        ) {
            // HOME
            composable(RootRoutes.HOME) {
                HomeScreen(
                    onExploreOffers = { nav.navigate(RootRoutes.OFFERS) },
                    onSignIn = { nav.navigate(RootRoutes.AUTH) }, // <-- Updated to AUTH
                    // --- FIX: Pass the new required parameters ---
                    onViewDestinations = { nav.navigate(RootRoutes.DESTINATIONS) },
                    onLearnMoreLoyalty = { nav.navigate(RootRoutes.LOYALTY) }
                    // ---------------------------------------------
                )
            }

            // OFFERS LIST
            composable(RootRoutes.OFFERS) {
                OffersListScreen(onOfferClick = { id -> nav.navigate(RootRoutes.offerDetails(id)) })
            }

            // BOOKINGS
            composable(RootRoutes.BOOKINGS) {
                // 🛑 FIX: Pass the onNavigateToOffers parameter here
                BookingsScreen(
                    onNavigateToOffers = { nav.navigate(RootRoutes.OFFERS) }
                )
            }

            // SCHEDULE
            composable(RootRoutes.SCHEDULE) { ScheduleScreen() }

            // DESTINATIONS (Placeholder for the new route)
            composable(RootRoutes.DESTINATIONS) {
                Text(text = "Destinations Screen Content")
            }

            // LOYALTY (Placeholder for the new route)
            composable(RootRoutes.LOYALTY) {
                Text(text = "Loyalty Program Screen Content")
            }

            // OFFER DETAILS
            composable(
                route = RootRoutes.OFFER_DETAILS_PATTERN,
                arguments = listOf(navArgument("id") { type = NavType.StringType })
            ) { entry ->
                val id = entry.arguments?.getString("id").orEmpty()
                OfferDetailsRoute(offerId = id, onBack = { nav.popBackStack() })
            }

            // AUTHENTICATION (Replaces both LOGIN and SIGNUP)
            composable(RootRoutes.AUTH) {
                RoyalAuthScreen(
                    onSuccess = {
                        // Navigate to PROFILE upon successful Sign In or Sign Up
                        nav.navigate(RootRoutes.PROFILE) {
                            popUpTo(RootRoutes.HOME) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onBack = { nav.popBackStack() }
                )
            }

            // PROFILE (with Logout)
            composable(RootRoutes.PROFILE) {
                val authVm = hiltViewModel<AuthViewModel>()
                ProfileScreen(
                    // NOTE: The AuthUiState reference must be correct for this line to compile.
                    userProvider = { (authVm.state.value as? AuthUiState.Success)?.user },
                    onLogout = {
                        authVm.signOut()
                        nav.navigate(RootRoutes.HOME) {
                            popUpTo(RootRoutes.HOME) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    }
}