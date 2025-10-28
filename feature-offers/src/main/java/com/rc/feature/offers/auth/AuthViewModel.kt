package com.rc.feature.offers.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface AuthUiState {
    data object Idle : AuthUiState
    data object Loading : AuthUiState
    data class Success(val user: User) : AuthUiState
    data class Error(val message: String) : AuthUiState
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    // If you created an interface (recommended), use AuthManagerContract.
    // Otherwise keep AuthManager.
    private val authManager: AuthManager
    // private val authManager: AuthManagerContract
) : ViewModel() {

    private val _state = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val state: StateFlow<AuthUiState> = _state

    fun signIn(email: String, password: String) {
        _state.value = AuthUiState.Loading
        viewModelScope.launch {
            val result = authManager.signIn(email, password)
            _state.value = result.fold(
                onSuccess = { AuthUiState.Success(it) },
                onFailure = { AuthUiState.Error(it.message ?: "Login failed") }
            )
        }
    }

    fun signUp(email: String, password: String) {
        _state.value = AuthUiState.Loading
        viewModelScope.launch {
            val result = authManager.signUp(email, password)
            _state.value = result.fold(
                onSuccess = { AuthUiState.Success(it) },
                onFailure = { AuthUiState.Error(it.message ?: "Sign up failed") }
            )
        }
    }

    fun signOut() {
        viewModelScope.launch {
            runCatching { authManager.signOut() }
            _state.value = AuthUiState.Idle
        }
    }
}
