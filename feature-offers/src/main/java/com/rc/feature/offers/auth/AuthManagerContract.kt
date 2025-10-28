package com.rc.feature.offers.auth

import kotlinx.coroutines.flow.StateFlow

interface AuthManagerContract {
    val user: StateFlow<User?>
    suspend fun signIn(email: String, password: String): Result<User>
    suspend fun signUp(email: String, password: String): Result<User>
    fun signOut()
}
