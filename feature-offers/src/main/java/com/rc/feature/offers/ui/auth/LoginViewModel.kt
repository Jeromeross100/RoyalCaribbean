package com.rc.feature.offers.ui.auth

// feature-offers/src/main/java/com/rc/feature/offers/ui/auth/LoginViewModel.kt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rc.feature.offers.auth.AuthRepository
import com.rc.feature.offers.auth.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repo: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow<LoginState>(LoginState.Idle)
    val state: StateFlow<LoginState> = _state

    fun submit(email: String, password: String) {
        _state.value = LoginState.Loading
        viewModelScope.launch {
            repo.signIn(email, password)
                .onSuccess { _state.value = LoginState.Success }
                .onFailure { _state.value = LoginState.Error(it.message ?: "Sign-in failed") }
        }
    }
}
