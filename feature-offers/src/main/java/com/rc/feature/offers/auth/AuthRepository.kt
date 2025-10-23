package com.rc.feature.offers.auth

// feature-offers/src/main/java/com/rc/feature/offers/auth/AuthRepository.kt

// feature-offers/src/main/java/com/rc/feature/offers/auth/AuthRepository.kt

interface AuthRepository {
    suspend fun signIn(email: String, password: String): Result<User>
    suspend fun signUp(fullName: String, email: String, password: String): Result<User>
    fun currentUser(): User?
    fun signOut()
}

