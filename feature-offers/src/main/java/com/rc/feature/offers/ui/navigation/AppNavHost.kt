package com.rc.feature.offers.ui.navigation

// app/src/main/java/<your_app_package>/NavHostSetup.kt
// Example usage: call the NEW function so it doesn't conflict with old ones.

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController


@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = OffersRoutes.LIST
    ) {
        offersGraphFeature(navController)
    }
}
