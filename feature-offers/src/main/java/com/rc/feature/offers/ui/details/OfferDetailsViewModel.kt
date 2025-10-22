// feature-offers/src/main/java/com/rc/feature/offers/ui/details/OfferDetailsViewModel.kt
package com.rc.feature.offers.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rc.feature.offers.data.OffersRepository
import com.rc.feature.offers.domain.OfferDetails
import com.rc.feature.offers.util.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OfferDetailsViewModel @Inject constructor(
    private val repository: OffersRepository
) : ViewModel() {

    private val _state = MutableStateFlow<UIState<OfferDetails>>(UIState.Loading)
    val state: StateFlow<UIState<OfferDetails>> = _state

    fun load(id: String) {
        _state.value = UIState.Loading
        viewModelScope.launch {
            runCatching { repository.getOfferDetails(id) }
                .onSuccess { _state.value = UIState.Success(it) }
                .onFailure { _state.value = UIState.Error("Failed to load offer", it) }
        }
    }
}
