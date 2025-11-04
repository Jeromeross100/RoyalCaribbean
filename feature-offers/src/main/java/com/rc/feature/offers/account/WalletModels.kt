package com.rc.feature.offers.account

// com/rc/feature/offers/account/WalletModels.kt (Create this new file)

import androidx.compose.ui.graphics.Color

data class OnboardTransaction(val date: String, val description: String, val amount: String)
data class Package(val name: String, val status: String, val color: Color)