package com.rc.feature.offers.auth

// feature-offers/src/main/java/com/rc/feature/offers/auth/FakeAuthRepository.kt

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeAuthRepository @Inject constructor() : AuthRepository { // Removed 'abstract' keyword
    private val users = mutableMapOf<String, Pair<User, String>>() // email -> (user, password)

    // 1. Implementation of the required abstract member from AuthRepository
    private val _user = MutableStateFlow<User?>(null)
    override val user: StateFlow<User?> = _user // Implement the StateFlow

    // Removed the private var loggedIn: User? = null as it is redundant, using _user.value instead
    // private var loggedIn: User? = null

    override suspend fun signIn(email: String, password: String): Result<User> {
        delay(700)
        val match = users[email.lowercase()]
        return if (match != null && match.second == password) {
            val user = match.first
            _user.value = user // Use the StateFlow to track logged in status
            Result.success(user)
        } else Result.failure(IllegalArgumentException("Invalid email or password"))
    }

    override suspend fun signUp(fullName: String, email: String, password: String): Result<User> {
        delay(900)
        val key = email.lowercase()
        if (users.containsKey(key)) return Result.failure(IllegalStateException("Account already exists"))
        if (!email.contains("@") || password.length < 6) {
            return Result.failure(IllegalArgumentException("Enter a valid email and a 6+ character password"))
        }
        val user = User(UserId(UUID.randomUUID().toString()).toString(), fullName.trim(), key)
        users[key] = user to password
        _user.value = user // Use the StateFlow to track logged in status
        return Result.success(user)
    }

    // REMOVED: 'currentUserEmail()' because it is not defined in the AuthRepository interface.
    // override suspend fun currentUserEmail(): String? { ... } // THIS LINE IS DELETED

    override fun currentUser(): User? = _user.value // Use the StateFlow value

    override fun signOut() {
        _user.value = null
    }
}