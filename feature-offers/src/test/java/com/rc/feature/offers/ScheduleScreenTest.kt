package com.rc.feature.offers

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rc.feature.offers.schedule.ScheduleScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ScheduleScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun shows_top_bar_title() {
        composeTestRule.setContent { ScheduleScreen() }
        composeTestRule.onNodeWithText("My Voyage Schedule").assertIsDisplayed()
    }

    @Test
    fun shows_all_day_headers() {
        composeTestRule.setContent { ScheduleScreen() }

        // Assert Day 1 header
        composeTestRule.onNodeWithText("Day 1 – Miami (Embark)").assertIsDisplayed()
        // Assert Day 2 header
        composeTestRule.onNodeWithText("Day 2 – Perfect Day at CocoCay").assertIsDisplayed()
        // Assert Day 4 header
        composeTestRule.onNodeWithText("Day 4 – Miami (Debark)").assertIsDisplayed()
    }

    @Test
    fun shows_first_day_timeline_events() {
        composeTestRule.setContent { ScheduleScreen() }

        // Assert first event details (Boarding)
        composeTestRule.onNodeWithText("12:00 PM  •  Boarding Opens").assertIsDisplayed()
        composeTestRule.onNodeWithText("Terminal A", substring = true).assertIsDisplayed()

        // Assert second event details (Sail Away)
        composeTestRule.onNodeWithText("04:00 PM  •  Sail Away Party").assertIsDisplayed()
        composeTestRule.onNodeWithText("Pool Deck").assertIsDisplayed()
    }

    @Test
    fun shows_timeline_structure_for_multiday_events() {
        composeTestRule.setContent { ScheduleScreen() }

        // Assert Day 2 event (CocoCay)
        composeTestRule.onNodeWithText("10:00 AM  •  Thrill Waterpark").assertIsDisplayed()

        // Assert Day 3 event (Nassau)
        composeTestRule.onNodeWithText("10:00 AM  •  Snorkel Excursion").assertIsDisplayed()
        composeTestRule.onNodeWithText("Nassau", useUnmergedTree = true).assertIsDisplayed()
    }
}