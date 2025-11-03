package com.rc.feature.offers.auth

// feature-offers/src/main/java/com/rc/feature/offers/auth/User.kt

@JvmInline
value class UserId(val value: String)

data class User(
    val id: String,
    val fullName: String,
    val email: String,
    val crownAnchorsTier: String = "Gold",
    val points: Int = 1200
)
