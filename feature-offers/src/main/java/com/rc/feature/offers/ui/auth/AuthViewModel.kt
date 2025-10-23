// feature-offers/src/main/java/com/rc/feature/offers/ui/auth/AuthViewModel.kt
package com.rc.feature.offers.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rc.feature.offers.auth.AuthRepository
import com.rc.feature.offers.auth.User
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

enum class AuthMode { SignIn, CreateAccount }

data class AuthUiState(
    val mode: AuthMode = AuthMode.SignIn,
    val fullName: String = "",
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val user: User? = null
)

sealed interface AuthEvent {
    data class SignedIn(val user: User) : AuthEvent
    data class SignedUp(val user: User) : AuthEvent
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repo: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AuthUiState())
    val state: StateFlow<AuthUiState> = _state

    // One-shot events so navigation never gets “missed”
    private val _events = MutableSharedFlow<AuthEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<AuthEvent> = _events

    fun toggleMode() {
        _state.value = _state.value.copy(
            mode = if (_state.value.mode == AuthMode.SignIn) AuthMode.CreateAccount else AuthMode.SignIn,
            error = null
        )
    }

    fun updateFullName(v: String) { _state.value = _state.value.copy(fullName = v) }
    fun updateEmail(v: String) { _state.value = _state.value.copy(email = v) }
    fun updatePassword(v: String) { _state.value = _state.value.copy(password = v) }
    fun togglePasswordVisibility() { _state.value = _state.value.copy(isPasswordVisible = !_state.value.isPasswordVisible) }

    fun submit() {
        val s = _state.value
        _state.value = s.copy(isLoading = true, error = null)

        viewModelScope.launch {
            val result = when (s.mode) {
                AuthMode.SignIn -> repo.signIn(s.email.trim(), s.password)
                AuthMode.CreateAccount -> repo.signUp(s.fullName.trim(), s.email.trim(), s.password)
            }
            result
                .onSuccess { user ->
                    _state.value = _state.value.copy(isLoading = false, user = user)
                    // fire one-shot nav event so UI always navigates
                    _events.tryEmit(
                        if (s.mode == AuthMode.SignIn) AuthEvent.SignedIn(user)
                        else AuthEvent.SignedUp(user)
                    )
                }
                .onFailure { t ->
                    _state.value = _state.value.copy(isLoading = false, error = t.message ?: "Something went wrong")
                }
        }
    }
}
