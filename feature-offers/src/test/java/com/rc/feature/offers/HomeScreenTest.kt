package com.rc.feature.offers

// Example structure for a Compose test

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.rc.feature.offers.theme.RcTheme // Assume the theme is accessible
import com.rc.feature.offers.ui.auth.HomeScreen
import com.rc.feature.offers.ui.auth.UpcomingVoyage
import com.rc.feature.offers.ui.auth.User
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertTrue

class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val testUser = User("Alex")
    private val testVoyage = UpcomingVoyage("Icon of the Seas", "Dec 1", 60, "Barcelona", "url")

    // --- Logged Out Tests ---

    @Test
    fun `LoggedOutHomeContent should display Sign In button and Welcome message`() {
        composeTestRule.setContent {
            RcTheme {
                HomeScreen(
                    user = null,
                    upcomingVoyage = null,
                    onExploreOffers = {},
                    onSignIn = {},
                    onViewDestinations = {},
                    onLearnMoreLoyalty = {}
                )
            }
        }
        // Verify Logged Out specific content
        composeTestRule.onNodeWithText("Welcome aboard! 👋").assertExists()
        composeTestRule.onNodeWithText("Sign In").assertExists()
        composeTestRule.onNodeWithText("Explore Offers").assertExists()

        // Verify Logged In content is NOT present
        composeTestRule.onNodeWithText("Welcome Back, ${testUser.firstName}!").assertDoesNotExist()
        composeTestRule.onNodeWithText(testVoyage.shipName).assertDoesNotExist()
    }

    @Test
    fun `LoggedOutHomeContent should invoke onSignIn when Sign In button is clicked`() {
        var signInClicked = false
        composeTestRule.setContent {
            RcTheme {
                HomeScreen(
                    user = null,
                    upcomingVoyage = null,
                    onExploreOffers = {},
                    onSignIn = { signInClicked = true },
                    onViewDestinations = {},
                    onLearnMoreLoyalty = {}
                )
            }
        }
        composeTestRule.onNodeWithText("Sign In").performClick()
        assertTrue(signInClicked)
    }

    // --- Logged In Tests ---

    @Test
    fun `VoyageDashboardContent should display Welcome Back and Voyage Banner`() {
        composeTestRule.setContent {
            RcTheme {
                HomeScreen(
                    user = testUser,
                    upcomingVoyage = testVoyage,
                    onExploreOffers = {},
                    onSignIn = {},
                    onViewDestinations = {},
                    onLearnMoreLoyalty = {}
                )
            }
        }
        // Verify Logged In specific content
        composeTestRule.onNodeWithText("Welcome Back, ${testUser.firstName}!").assertExists()
        composeTestRule.onNodeWithText(testVoyage.shipName).assertExists()
        composeTestRule.onNodeWithText("View Schedule").assertExists()

        // Verify Logged Out content is NOT present
        composeTestRule.onNodeWithText("Welcome aboard! 👋").assertDoesNotExist()
    }

    @Test
    fun `VoyageDashboardContent should invoke onViewSchedule when View Schedule button is clicked`() {
        var scheduleClicked = false
        composeTestRule.setContent {
            RcTheme {
                HomeScreen(
                    user = testUser,
                    upcomingVoyage = testVoyage,
                    onExploreOffers = {},
                    onSignIn = {},
                    onViewDestinations = {},
                    onLearnMoreLoyalty = {},
                    onViewSchedule = { scheduleClicked = true }
                )
            }
        }
        composeTestRule.onNodeWithText("View Schedule").performClick()
        assertTrue(scheduleClicked)
    }

    // --- Utility Tests ---

    @Test
    fun `Quick Access Tools should invoke onNavigateToShipUtility when Ship Map icon is clicked`() {
        var shipUtilityClicked = false
        composeTestRule.setContent {
            RcTheme {
                HomeScreen(
                    user = null,
                    upcomingVoyage = null,
                    onExploreOffers = {},
                    onSignIn = {},
                    onViewDestinations = {},
                    onLearnMoreLoyalty = {},
                    onNavigateToShipUtility = { shipUtilityClicked = true }
                )
            }
        }
        // This targets the column/card containing the text "Ship Map" and performs the click
        composeTestRule.onNodeWithText("Ship Map").assertExists().performClick()
        assertTrue(shipUtilityClicked)
    }
}