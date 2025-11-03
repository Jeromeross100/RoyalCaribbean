package com.rc.feature.offers.bookings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rc.feature.offers.data.graphql.BookingDto
import com.rc.feature.offers.data.graphql.CancelResult
import com.rc.feature.offers.util.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookingsViewModel @Inject constructor(
    private val repo: BookingsRepository
) : ViewModel() {

    private val _state = MutableStateFlow<UIState<List<BookingDto>>>(UIState.Loading)
    val state: StateFlow<UIState<List<BookingDto>>> = _state

    fun load() {
        _state.value = UIState.Loading
        viewModelScope.launch {
            runCatching { repo.list() }
                .onSuccess { bookings ->
                    _state.value = UIState.Success(bookings)
                }
                .onFailure { throwable ->
                    //  Use "cause" instead of "throwable"
                    _state.value = UIState.Error(
                        message = "Failed to load bookings",
                        cause = throwable
                    )
                }
        }
    }

    fun cancel(id: String, onDone: (CancelResult) -> Unit) {
        viewModelScope.launch {
            val result = runCatching { repo.cancel(id) }
                .getOrElse { CancelResult(false, it.message ?: "Error", null) }

            onDone(result)
            load() // refresh after cancel
        }
    }
}
