// feature-offers/src/main/java/com/rc/feature/offers/account/LoyaltyModels.kt

package com.rc.feature.offers.account

import androidx.compose.ui.graphics.Color

// --- 1. Data Structures ---
data class LoyaltyTier(
    val name: String,
    val description: String,
    val pointsRequired: Int,
    val benefits: List<String>,
    val color: Color
)

data class LoyaltyStatus(
    val guestName: String,
    val currentTier: LoyaltyTier,
    val totalPoints: Int,
    val pointsToNextTier: Int
)

// --- 2. Dummy Data (Fixes all 'Not enough information' errors) ---

// REMOVED 'private' so it can be accessed by LoyaltyProgramDetailsScreen
val Tiers = listOf(
    LoyaltyTier(
        name = "Gold",
        description = "Exclusive offers and onboard coupons.",
        pointsRequired = 3,
        benefits = listOf("CAS Welcome Back Offers", "Exclusive savings coupons"),
        color = Color(0xFFFDD835) // Yellow/Gold
    ),
    LoyaltyTier(
        name = "Platinum",
        description = "Added convenience and special onboard treats.",
        pointsRequired = 30,
        benefits = listOf("Personalized communications", "Lapel Pin"),
        color = Color(0xFFB0BEC5) // Silver/Platinum
    ),
    LoyaltyTier(
        name = "Emerald",
        description = "Special onboard privileges and more value.",
        pointsRequired = 55,
        benefits = listOf("Waters, snacks, and soda service", "Private departure lounge"),
        color = Color(0xFF4CAF50) // Emerald Green
    ),
    LoyaltyTier(
        name = "Diamond",
        description = "The best of what our fleet has to offer.",
        pointsRequired = 80,
        benefits = listOf("Exclusive Diamond Lounge access", "Priority boarding & departure"),
        color = Color(0xFF0091EA) // Bright Blue/Diamond
    )
    // You can add Diamond Plus and Pinnacle Club tiers here
)

// Fixes all 'No value passed for parameter' errors by using Tiers[2] (Emerald)
val SampleLoyaltyStatus = LoyaltyStatus(
    guestName = "Jane Doe",
    currentTier = Tiers[2], // Emerald Tier
    totalPoints = 48,
    pointsToNextTier = 7 // 55 (Emerald) - 48 = 7
)