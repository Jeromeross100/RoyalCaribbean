package com.rc.feature.offers

import com.rc.feature.offers.auth.AuthRepository
import com.rc.feature.offers.auth.LoginState
import com.rc.feature.offers.auth.User
import com.rc.feature.offers.ui.auth.LoginViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.resetMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    // Mock the dependency
    @MockK
    private lateinit var mockAuthRepository: AuthRepository

    private lateinit var viewModel: LoginViewModel

    private val testDispatcher = StandardTestDispatcher()

    // 🔴 FIX: Style Guide Fix for private properties (TEST_EMAIL -> testEmail)
    private val testEmail = "test@example.com"
    private val testPassword = "password123"

    @Before
    fun setUp() {
        MockKAnnotations.init(this) // Initialize mocks
        Dispatchers.setMain(testDispatcher) // Set the main dispatcher for coroutines
        // 🔴 FIX: Unresolved reference LoginViewModel is now fixed by defining the class
        viewModel = LoginViewModel(mockAuthRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // Reset the main dispatcher
    }

    @Test
    fun `submit - should transition from Idle to Loading and then to Success on successful sign-in`() = runTest {
        // Arrange
        // 🔴 FIX: Change 'name' parameter to 'fullName' to match User class definition
        val expectedUser = User(id = "1", email = testEmail, fullName = "Test User")
        coEvery { mockAuthRepository.signIn(any(), any()) } returns Result.success(expectedUser)

        // Act
        viewModel.submit(testEmail, testPassword)

        // Assert: Check that the state immediately transitions to Loading
        assertEquals(LoginState.Loading, viewModel.state.value)

        // Advance the coroutine to execute the repository call
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert: Check the final state
        assertEquals(LoginState.Success, viewModel.state.value)
    }

    @Test
    fun `submit - should transition from Idle to Loading and then to Error on sign-in failure`() = runTest {
        // Arrange
        val failureMessage = "Invalid credentials"
        coEvery { mockAuthRepository.signIn(any(), any()) } returns Result.failure(Exception(failureMessage))

        // Act
        viewModel.submit(testEmail, testPassword)

        // Assert: Check that the state immediately transitions to Loading
        assertEquals(LoginState.Loading, viewModel.state.value)

        // Advance the coroutine
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert: Check the final state
        val finalState = viewModel.state.value
        // 🔴 FIX: 'Condition 'finalState is LoginState.Error' is always false' will be
        // resolved once LoginViewModel is correctly implemented to set LoginState.Error
        assertTrue(finalState is LoginState.Error)
        assertEquals(failureMessage, (finalState as LoginState.Error).message)
    }

    @Test
    fun `submit - should use default error message when sign-in failure has no message`() = runTest {
        // Arrange
        coEvery { mockAuthRepository.signIn(any(), any()) } returns Result.failure(Exception(null as String?))

        // Act
        viewModel.submit(testEmail, testPassword)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        val finalState = viewModel.state.value
        assertTrue(finalState is LoginState.Error)
        assertEquals("Sign-in failed", (finalState as LoginState.Error).message)
    }
}