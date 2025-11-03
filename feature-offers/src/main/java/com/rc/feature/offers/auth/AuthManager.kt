package com.rc.feature.offers.auth

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthManager @Inject constructor() : AuthManagerContract {

    private val _user = MutableStateFlow<User?>(null)
    override val user: StateFlow<User?> = _user

    override suspend fun signIn(email: String, password: String): Result<User> {
        delay(600) // simulate network
        if (!email.contains("@") || password.length < 6) {
            return Result.failure(IllegalArgumentException("Invalid email or password"))
        }

        val username = email.substringBefore("@")
        val user = User(
            id = UserId("u-$username").toString(),
            fullName = "Captain ${username.replaceFirstChar { it.uppercase() }}",
            email = email
        )
        _user.value = user
        return Result.success(user)
    }

    override suspend fun signUp(email: String, password: String): Result<User> {
        delay(800) // simulate network for signup
        if (!email.contains("@") || password.length < 6) {
            return Result.failure(IllegalArgumentException("Email must contain @ and password must be 6+ chars"))
        }

        val username = email.substringBefore("@")
        val user = User(
            id = UserId("u-$username").toString(),
            fullName = "New Sailor ${username.replaceFirstChar { it.uppercase() }}",
            email = email
        )
        _user.value = user
        return Result.success(user)
    }

    override fun signOut() {
        _user.value = null
    }
}
