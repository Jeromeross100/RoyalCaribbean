package com.rc.feature.offers.util

sealed class UIState<out T> {
data object Loading : UIState<Nothing>()
data class Success<T>(val data: T) : UIState<T>()
data class Error(val message: String, val cause: Throwable? = null) : UIState<Nothing>()
}
