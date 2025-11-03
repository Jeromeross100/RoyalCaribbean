package com.rc.feature.offers

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rc.feature.offers.auth.SignUpScreen
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

// NOTE: AuthViewModel and AuthUiState types are assumed to be accessible here

// Mock implementation of AuthUiState for testing
sealed class AuthUiState {
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}

// Mock implementation of AuthViewModel (similar to what hiltViewModel provides)
interface AuthViewModel {
    val state: MutableStateFlow<AuthUiState>
    fun signUp(email: String, password: String)
}

@RunWith(AndroidJUnit4::class)
class SignUpScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockViewModel: AuthViewModel = mockk(relaxUnitFun = true)
    private val mockFlowState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    private val mockOnSuccess: () -> Unit = mockk(relaxUnitFun = true)
    private val mockOnBack: () -> Unit = mockk(relaxUnitFun = true)

    @Before
    fun setup() {
        every { mockViewModel.state } returns mockFlowState

        composeTestRule.setContent {
            // NOTE: We rely on the provided RoyalPalette object definition
            SignUpScreen(
                onSuccess = mockOnSuccess,
                onBack = mockOnBack,
                vm = mockViewModel as com.rc.feature.offers.auth.AuthViewModel // Cast needed if Hilt is used
            )
        }
    }

    @Test
    fun shows_top_bar_and_back_action() {
        composeTestRule.onNodeWithText("Create Account").assertIsDisplayed()

        // Click back button and verify callback
        composeTestRule.onNodeWithContentDescription("Back").performClick()
        verify(exactly = 1) { mockOnBack() }
    }

    @Test
    fun sign_up_button_disabled_initially() {
        // Assert fields are present
        composeTestRule.onNodeWithText("Email").assertIsDisplayed()
        composeTestRule.onNodeWithText("Password").assertIsDisplayed()

        // Assert button is disabled when fields are empty
        composeTestRule.onNodeWithText("Sign Up").assertIsNotEnabled()
    }

    @Test
    fun password_mismatch_shows_error_and_disables_button() {
        // 1. Enter Email
        composeTestRule.onNodeWithText("Email").performTextInput("test@rci.com")
        // 2. Enter Password
        composeTestRule.onNodeWithText("Password").performTextInput("pass123")
        // 3. Enter mismatching Confirm Password
        composeTestRule.onNodeWithText("Confirm Password").performTextInput("mismatch")

        // Assert error message is shown
        composeTestRule.onNodeWithText("Passwords do not match").assertIsDisplayed()

        // Assert button remains disabled
        composeTestRule.onNodeWithText("Sign Up").assertIsNotEnabled()
    }

    @Test
    fun sign_up_button_enabled_on_valid_input() {
        // Fill all fields with valid, matching data
        composeTestRule.onNodeWithText("Email").performTextInput("test@rci.com")
        composeTestRule.onNodeWithText("Password").performTextInput("password123")
        composeTestRule.onNodeWithText("Confirm Password").performTextInput("password123")

        // Assert button is enabled
        composeTestRule.onNodeWithText("Sign Up").assertIsEnabled()
    }

    @Test
    fun shows_loading_state_when_signing_up() {
        // Arrange: Set valid input
        composeTestRule.onNodeWithText("Email").performTextInput("test@rci.com")
        composeTestRule.onNodeWithText("Password").performTextInput("password123")
        composeTestRule.onNodeWithText("Confirm Password").performTextInput("password123")

        // Act: Emit Loading state
        mockFlowState.value = AuthUiState.Loading

        // Assert: Button text changes and loading indicator appears
        composeTestRule.onNodeWithText("Creating account…").assertIsDisplayed()
        // Check for the presence of a loading indicator (using the content description or component type if defined)
        composeTestRule.onNodeWithContentDescription("Loading").assertIsDisplayed()
        composeTestRule.onNodeWithText("Sign Up").assertDoesNotExist()
        composeTestRule.onNodeWithText("Creating account…").assertIsNotEnabled()
    }
}