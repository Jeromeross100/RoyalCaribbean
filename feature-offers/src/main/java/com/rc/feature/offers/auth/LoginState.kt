package com.rc.feature.offers.auth

// feature-offers/src/main/java/com/rc/feature/offers/auth/LoginState.kt

sealed class LoginState {
    data object Idle : LoginState()
    data object Loading : LoginState()
    data object Success : LoginState()
    data class Error(val message: String) : LoginState()
}
