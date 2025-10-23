package com.rc.feature.offers.auth

// app/src/main/java/com/rc/app/auth/AuthManager.kt

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthManager @Inject constructor() {
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    suspend fun signIn(email: String, password: String): Result<User> {
        delay(600) // simulate network
        if (!email.contains("@") || password.length < 6) {
            return Result.failure(IllegalArgumentException("Invalid email or password"))
        }

        val username = email.substringBefore("@")
        val user = User(
            id = UserId("u-$username"),
            fullName = "Captain ${username.replaceFirstChar { it.uppercase() }}",
            email = email
            // crownAnchorsTier and points use defaults from the User model
        )

        _user.value = user
        return Result.success(user)
    }

    fun signOut() {
        _user.value = null
    }
}
