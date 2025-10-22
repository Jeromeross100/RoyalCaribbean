// feature-offers/src/main/java/com/rc/feature/offers/ui/navigation/OffersNavigation.kt
// This avoids ALL name collisions by using new names.
// Use OffersRoutes and offersGraphFeature(...) in your NavHost.

package com.rc.feature.offers.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.rc.feature.offers.ui.details.OfferDetailsScreen
import com.rc.feature.offers.ui.list.OffersListScreen

object OffersRoutes {
    const val LIST: String = "offers"
    const val DETAILS: String = "offer/{id}"
}

fun NavGraphBuilder.offersGraphFeature(navController: NavHostController) {
    composable(OffersRoutes.LIST) {
        OffersListScreen(
            onOfferClick = { id -> navController.navigate("offer/$id") }
        )
    }
    composable(OffersRoutes.DETAILS) { backStackEntry ->
        val id = backStackEntry.arguments?.getString("id").orEmpty()
        OfferDetailsScreen(
            offerId = id,
            onBack = { navController.popBackStack() }
        )
    }
}
