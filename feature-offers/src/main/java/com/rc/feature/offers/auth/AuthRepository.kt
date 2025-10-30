package com.rc.feature.offers.auth

import kotlinx.coroutines.flow.StateFlow

// feature-offers/src/main/java/com/rc/feature/offers/auth/AuthRepository.kt

interface AuthRepository {

    /**
     * The global stream of the currently logged-in user.
     * Use this to drive app navigation (logged in vs. logged out state).
     */
    val user: StateFlow<User?>

    /**
     * Signs the user in with email and password.
     */
    suspend fun signIn(email: String, password: String): Result<User>

    /**
     * Registers a new user with full details.
     * NOTE: The original AuthManager only took email/password, but FakeAuthRepository
     * suggests a fullName is needed for a real sign-up process.
     */
    suspend fun signUp(fullName: String, email: String, password: String): Result<User>

    /**
     * Retrieves the current user object synchronously.
     */
    fun currentUser(): User?

    /**
     * Logs the user out and clears the user state.
     */
    fun signOut()

    // Note: currentUserEmail() is often redundant if you have currentUser().
    // We omit it for simplicity unless there's a strong need for only the email.
    // If needed, it can be added back:
    // suspend fun currentUserEmail(): String?
}
