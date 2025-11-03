package com.rc.feature.offers

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rc.feature.offers.account.WalletScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WalletScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun shows_top_bar_title() {
        composeTestRule.setContent { WalletScreen() }
        composeTestRule.onNodeWithText("My Wallet & Account").assertIsDisplayed()
    }

    @Test
    fun shows_balance_and_loyalty_card() {
        composeTestRule.setContent { WalletScreen() }

        // Assert Balance
        composeTestRule.onNodeWithText("$150.00").assertIsDisplayed()

        // Assert Loyalty Tier
        composeTestRule.onNodeWithText("Crown & Anchor Tier: Emerald").assertIsDisplayed()
    }

    @Test
    fun shows_active_packages_list() {
        composeTestRule.setContent { WalletScreen() }

        // Assert Section Header
        composeTestRule.onNodeWithText("My Active Packages").assertIsDisplayed()

        // Assert Package items and action button
        composeTestRule.onNodeWithText("Deluxe Drink").assertIsDisplayed()
        composeTestRule.onNodeWithText("VOOM Wi-Fi").assertIsDisplayed()
        composeTestRule.onNodeWithText("3 of 5 Used").assertIsDisplayed()
        composeTestRule.onAllNodesWithText("Manage").assertCountEquals(3)
    }

    @Test
    fun shows_transaction_history() {
        composeTestRule.setContent { WalletScreen() }

        // Assert Section Header
        composeTestRule.onNodeWithText("Transaction History").assertIsDisplayed()

        // Assert first transaction details
        composeTestRule.onNodeWithText("Drink Package Purchase").assertIsDisplayed()
        composeTestRule.onNodeWithText("$180.00").assertIsDisplayed()
        composeTestRule.onNodeWithText("10/30 4:30 PM").assertIsDisplayed()
    }
}