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
    // The Composable sets the content description of the loading indicator based on the state itself.
    private val mockFlowState = MutableStateFlow<UIState<List<BookingDto>>>(UIState.Loading)
    private val mockOnNavigateToOffers: () -> Unit = mockk(relaxed = true)

    @Before
    fun setup() {
        // Mock the state flow that the Composable collects
        every { mockViewModel.state } returns mockFlowState

        // Set up the Composable under test
        composeTestRule.setContent {
            BookingsScreen(
                onNavigateToOffers = mockOnNavigateToOffers,
                vm = mockViewModel
            )
        }
    }

    // --- 1. Initial State & Loading ---

    @Test
    fun initial_load_calls_vm_load_and_shows_progress() {
        // Assert initial state setup
        coVerify(exactly = 1) { mockViewModel.load() }

        // 🛠️ FIX: Replaced isCircularProgressIndicator() which is not a standard API,
        // with a more generic assertion to confirm the Loading indicator's presence.
        composeTestRule.onNode(hasContentDescription("Loading")).assertIsDisplayed()
    }

    // --- 2. Success State (Bookings Available) ---

    @Test
    fun success_with_data_shows_list_and_cards() {
        // Arrange: Emit success state with data
        mockFlowState.value = UIState.Success(MockBookings.ALL_MOCK_BOOKINGS)

        // Assert: List content is visible
        composeTestRule.onNodeWithText("Confirmation: RCM001234").assertIsDisplayed()
        composeTestRule.onNodeWithText("Guest: Alice Johnson").assertIsDisplayed()
        composeTestRule.onNodeWithText("Cancel").assertIsDisplayed()
    }

    // --- 3. Success State (No Bookings) ---

    @Test
    fun success_empty_list_shows_no_voyages_ui_and_explore_button() {
        // Arrange: Emit success state with empty list
        mockFlowState.value = UIState.Success(MockBookings.EMPTY_BOOKINGS_LIST)

        // Assert: Empty state UI is visible
        composeTestRule.onNodeWithText("No Upcoming Voyages").assertIsDisplayed()
        composeTestRule.onNodeWithText("Explore Cruise Deals").assertIsDisplayed()
    }

    // --- 4. Error State ---

    @Test
    fun error_state_shows_retry_button_and_calls_load_on_click() {
        // Arrange: Emit error state
        mockFlowState.value = UIState.Error("Failed to load bookings", Exception("Test Error"))

        // Assert: Error UI is visible
        composeTestRule.onNodeWithText("Unable to load bookings").assertIsDisplayed()

        // Assert and click Retry button
        val retryButton = composeTestRule.onNodeWithText("Retry")
        retryButton.assertIsDisplayed()

        retryButton.performClick()

        // Assert: load() called twice (once on launch, once on retry)
        coVerify(exactly = 2) { mockViewModel.load() }
    }

    // --- 5. Interaction: Explore Offers ---

    @Test
    fun explore_button_navigates_to_offers() {
        // Arrange: Ensure empty state is visible
        mockFlowState.value = UIState.Success(MockBookings.EMPTY_BOOKINGS_LIST)

        // Act: Click the button
        composeTestRule.onNodeWithText("Explore Cruise Deals").performClick()

        // Assert: Navigation callback was triggered
        coVerify(exactly = 1) { mockOnNavigateToOffers() }
    }

    // --- 6. Interaction: Cancellation Flow ---

    @Test
    fun cancel_button_shows_dialog() {
        // Arrange: Ensure list is visible
        mockFlowState.value = UIState.Success(MockBookings.ALL_MOCK_BOOKINGS)

        // Act: Click the cancel button for the first booking
        composeTestRule.onNode(hasParent(hasText("Confirmation: RCM001234")) and hasText("Cancel")).performClick()

        // Assert: Dialog is displayed
        composeTestRule.onNodeWithText("Confirm Cancellation").assertIsDisplayed()
        composeTestRule.onNodeWithText("for guest Alice Johnson?").assertIsDisplayed()
    }

    @Test
    fun dialog_confirm_calls_vm_cancel_and_dismisses() {
        // Arrange: Ensure list is visible
        mockFlowState.value = UIState.Success(MockBookings.ALL_MOCK_BOOKINGS)

        // 1. Show the dialog
        composeTestRule.onNode(hasParent(hasText("Confirmation: RCM001234")) and hasText("Cancel")).performClick()

        // 2. Click the confirm button
        composeTestRule.onNodeWithText("Confirm Cancel").performClick()

        // Assert:
        // 1. vm.cancel was called with the correct ID
        coVerify(exactly = 1) { mockViewModel.cancel(MockBookings.VALID_BOOKING_1.id, any()) }

        // 2. Dialog is dismissed
        composeTestRule.onNodeWithText("Confirm Cancellation").assertDoesNotExist()
    }

    @Test
    fun dialog_dismiss_does_not_call_vm_cancel() {
        // 🛠️ FIX: Corrected typo from ALL_MOCKINGS to MockBookings.ALL_MOCK_BOOKINGS
        // Arrange: Ensure list is visible
        mockFlowState.value = UIState.Success(MockBookings.ALL_MOCK_BOOKINGS)

        // 1. Show the dialog
        composeTestRule.onNode(hasParent(hasText("Confirmation: RCM001234")) and hasText("Cancel")).performClick()

        // 2. Click the dismiss button
        composeTestRule.onNodeWithText("Keep Booking").performClick()

        // Assert:
        // 1. vm.cancel was NOT called
        coVerify(exactly = 0) { mockViewModel.cancel(any(), any()) }

        // 2. Dialog is dismissed
        composeTestRule.onNodeWithText("Confirm Cancellation").assertDoesNotExist()
    }
}