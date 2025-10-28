// feature-offers/src/main/java/com/rc/feature/offers/ui/navigation/OffersNavigation.kt
// This avoids ALL name collisions by using new names.
// Use OffersRoutes and offersGraphFeature(...) in your NavHost.

package com.rc.feature.offers.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.rc.feature.offers.ui.details.OfferDetailsRoute // <-- use the VM-owning Route
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
        // Call the Route, which accepts onBack. This fixes: "Cannot find a parameter with this name: onBack"
        OfferDetailsRoute(
            offerId = id,
            onBack = { navController.popBackStack() }
        )
    }
}

/*
If you haven't added OfferDetailsRoute yet, either:
1) Add it (recommended), or
2) Temporarily call the screen without onBack (system back will still work):

    import com.rc.feature.offers.ui.details.OfferDetailsScreen
    ...
    OfferDetailsScreen(offerId = id)

*/
