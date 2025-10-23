package com.rc.feature.offers.auth

// feature-offers/src/main/java/com/rc/feature/offers/auth/FakeAuthRepository.kt

// feature-offers/src/main/java/com/rc/feature/offers/auth/FakeAuthRepository.kt

import kotlinx.coroutines.delay
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeAuthRepository @Inject constructor() : AuthRepository {
    private val users = mutableMapOf<String, Pair<User, String>>() // email -> (user, password)
    private var loggedIn: User? = null

    override suspend fun signIn(email: String, password: String): Result<User> {
        delay(700)
        val match = users[email.lowercase()]
        return if (match != null && match.second == password) {
            loggedIn = match.first
            Result.success(match.first)
        } else Result.failure(IllegalArgumentException("Invalid email or password"))
    }

    override suspend fun signUp(fullName: String, email: String, password: String): Result<User> {
        delay(900)
        val key = email.lowercase()
        if (users.containsKey(key)) return Result.failure(IllegalStateException("Account already exists"))
        if (!email.contains("@") || password.length < 6) {
            return Result.failure(IllegalArgumentException("Enter a valid email and a 6+ character password"))
        }
        val user = User(UserId(UUID.randomUUID().toString()), fullName.trim(), key)
        users[key] = user to password
        loggedIn = user
        return Result.success(user)
    }

    override fun currentUser(): User? = loggedIn

    override fun signOut() { loggedIn = null }
}
