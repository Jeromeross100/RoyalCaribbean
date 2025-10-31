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

// --- NEW SCREEN IMPORTS ---
import com.rc.feature.offers.ship.ShipUtilityScreen
import com.rc.feature.offers.ports.PortPlannerScreen
import com.rc.feature.offers.account.WalletScreen
// --------------------------

import com.rc.feature.offers.auth.AuthViewModel
import com.rc.feature.offers.auth.AuthUiState
import com.rc.feature.offers.profile.ProfileScreen
import com.rc.feature.offers.bookings.BookingsScreen
import com.rc.feature.offers.schedule.ScheduleScreen
import com.rc.feature.offers.ui.details.OfferDetailsRoute
import com.rc.feature.offers.ui.list.OffersListScreen
import com.rc.feature.offers.ui.auth.HomeScreen
import com.rc.feature.offers.ui.auth.RoyalAuthScreen

object RootRoutes {
    const val HOME = "home"
    const val OFFERS = "offers"
    const val BOOKINGS = "bookings"
    const val SCHEDULE = "schedule"
    const val OFFER_DETAILS_PATTERN = "offer/{id}"
    fun offerDetails(id: String) = "offer/$id"
    const val AUTH = "auth"
    const val PROFILE = "profile"
    const val DESTINATIONS = "destinations"
    const val LOYALTY = "loyalty"

    // --- NEW SCREEN ROUTES ---
    const val SHIP_UTILITY = "ship_utility"
    const val PORT_PLANNER = "port_planner"
    const val WALLET = "wallet"
    // -------------------------
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
        RootRoutes.AUTH,
        RootRoutes.OFFER_DETAILS_PATTERN,
        RootRoutes.SHIP_UTILITY,
        RootRoutes.PORT_PLANNER,
        RootRoutes.WALLET
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
            // HOME (Updated to include navigation for utility screens)
            composable(RootRoutes.HOME) {
                HomeScreen(
                    onExploreOffers = { nav.navigate(RootRoutes.OFFERS) },
                    onSignIn = { nav.navigate(RootRoutes.AUTH) },
                    onViewDestinations = { nav.navigate(RootRoutes.DESTINATIONS) },
                    onLearnMoreLoyalty = { nav.navigate(RootRoutes.LOYALTY) },
                    // *** FIX: Connect the utility lambdas to the correct routes ***
                    onNavigateToShipUtility = { nav.navigate(RootRoutes.SHIP_UTILITY) },
                    onNavigateToPortPlanner = { nav.navigate(RootRoutes.PORT_PLANNER) },
                    onNavigateToWallet = { nav.navigate(RootRoutes.WALLET) }
                    // *** End FIX ***
                )
            }

            // OFFERS LIST
            composable(RootRoutes.OFFERS) {
                OffersListScreen(onOfferClick = { id -> nav.navigate(RootRoutes.offerDetails(id)) })
            }

            // BOOKINGS
            composable(RootRoutes.BOOKINGS) {
                BookingsScreen(
                    onNavigateToOffers = { nav.navigate(RootRoutes.OFFERS) }
                )
            }

            // SCHEDULE
            composable(RootRoutes.SCHEDULE) { ScheduleScreen() }

            // --- NEW SCREEN DEFINITIONS (These are correct) ---
            composable(RootRoutes.SHIP_UTILITY) {
                ShipUtilityScreen()
            }

            composable(RootRoutes.PORT_PLANNER) {
                PortPlannerScreen()
            }

            composable(RootRoutes.WALLET) {
                WalletScreen()
            }
            // ------------------------------

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

            // AUTHENTICATION
            composable(RootRoutes.AUTH) {
                RoyalAuthScreen(
                    onSuccess = {
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