package com.rc.feature.offers

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rc.feature.offers.bookings.BookingsScreen
import com.rc.feature.offers.bookings.BookingsViewModel
import com.rc.feature.offers.data.graphql.BookingDto
import com.rc.feature.offers.util.UIState
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BookingsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockViewModel: BookingsViewModel = mockk(relaxed = true)
    private val mockFlowState = MutableStateFlow<UIState<List<BookingDto>>>(UIState.Loading)
    private val mockOnNavigateToOffers: () -> Unit = mockk(relaxed = true)

    @Before
    fun setup() {
        every { mockViewModel.state } returns mockFlowState

        composeTestRule.setContent {
            BookingsScreen(
                onNavigateToOffers = mockOnNavigateToOffers,
                vm = mockViewModel
            )
        }
    }

    // 1️⃣ Initial State & Loading
    @Test
    fun initial_load_calls_vm_load_and_shows_progress() {
        coVerify(exactly = 1) { mockViewModel.load() }
        composeTestRule.onNode(hasContentDescription("Loading")).assertIsDisplayed()
    }

    // 2️⃣ Success State (Bookings Available)
    @Test
    fun success_with_data_shows_list_and_cards() {
        mockFlowState.value = UIState.Success(MockBookings.ALL_MOCK_BOOKINGS)
        composeTestRule.onNodeWithText("Confirmation: RCM001234").assertIsDisplayed()
        composeTestRule.onNodeWithText("Guest: Alice Johnson").assertIsDisplayed()
        composeTestRule.onNodeWithText("Cancel").assertIsDisplayed()
    }

    // 3️⃣ Success State (No Bookings)
    @Test
    fun success_empty_list_shows_no_voyages_ui_and_explore_button() {
        mockFlowState.value = UIState.Success(MockBookings.EMPTY_BOOKINGS_LIST)
        composeTestRule.onNodeWithText("No Upcoming Voyages").assertIsDisplayed()
        composeTestRule.onNodeWithText("Explore Cruise Deals").assertIsDisplayed()
    }

    // 4️⃣ Error State
    @Test
    fun error_state_shows_retry_button_and_calls_load_on_click() {
        mockFlowState.value = UIState.Error("Failed to load bookings", Exception("Test Error"))
        composeTestRule.onNodeWithText("Unable to load bookings").assertIsDisplayed()

        val retryButton = composeTestRule.onNodeWithText("Retry")
        retryButton.assertIsDisplayed()
        retryButton.performClick()

        coVerify(exactly = 2) { mockViewModel.load() }
    }

    // 5️⃣ Interaction: Explore Offers
    @Test
    fun explore_button_navigates_to_offers() {
        mockFlowState.value = UIState.Success(MockBookings.EMPTY_BOOKINGS_LIST)
        composeTestRule.onNodeWithText("Explore Cruise Deals").performClick()
        coVerify(exactly = 1) { mockOnNavigateToOffers() }
    }

    // 6️⃣ Interaction: Cancellation Flow
    @Test
    fun cancel_button_shows_dialog() {
        mockFlowState.value = UIState.Success(MockBookings.ALL_MOCK_BOOKINGS)
        composeTestRule.onNode(hasParent(hasText("Confirmation: RCM001234")) and hasText("Cancel")).performClick()
        composeTestRule.onNodeWithText("Confirm Cancellation").assertIsDisplayed()
        composeTestRule.onNodeWithText("for guest Alice Johnson?").assertIsDisplayed()
    }

    @Test
    fun dialog_confirm_calls_vm_cancel_and_dismisses() {
        mockFlowState.value = UIState.Success(MockBookings.ALL_MOCK_BOOKINGS)
        composeTestRule.onNode(hasParent(hasText("Confirmation: RCM001234")) and hasText("Cancel")).performClick()
        composeTestRule.onNodeWithText("Confirm Cancel").performClick()
        coVerify(exactly = 1) { mockViewModel.cancel(MockBookings.VALID_BOOKING_1.id, any()) }
        composeTestRule.onNodeWithText("Confirm Cancellation").assertDoesNotExist()
    }

    @Test
    fun dialog_dismiss_does_not_call_vm_cancel() {
        mockFlowState.value = UIState.Success(MockBookings.ALL_MOCK_BOOKINGS)
        composeTestRule.onNode(hasParent(hasText("Confirmation: RCM001234")) and hasText("Cancel")).performClick()
        composeTestRule.onNodeWithText("Keep Booking").performClick()
        coVerify(exactly = 0) { mockViewModel.cancel(any(), any()) }
        composeTestRule.onNodeWithText("Confirm Cancellation").assertDoesNotExist()
    }
}
