package com.rc.feature.offers.profile

import androidx.lifecycle.ViewModel
import com.rc.feature.offers.auth.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    // now RepositoryBindingsModule is "used"
}
