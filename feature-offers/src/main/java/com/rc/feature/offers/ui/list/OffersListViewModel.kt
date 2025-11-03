package com.rc.feature.offers.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rc.feature.offers.data.OffersRepository
import com.rc.feature.offers.domain.OfferSummary
import com.rc.feature.offers.util.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OffersListViewModel @Inject constructor(
    private val repository: OffersRepository
) : ViewModel() {

    private val _state = MutableStateFlow<UIState<List<OfferSummary>>>(UIState.Loading)
    val state: StateFlow<UIState<List<OfferSummary>>> = _state

    fun loadOffers() {
        _state.value = UIState.Loading
        viewModelScope.launch {
            runCatching { repository.getOffers() }
                .onSuccess { offers ->
                    _state.value = UIState.Success(offers)
                }
                .onFailure { throwable ->
                    _state.value = UIState.Error(
                        message = "Failed to load offers",
                        cause = throwable
                    )
                }
        }
    }
}
