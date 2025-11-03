package com.rc.feature.offers

import com.rc.feature.offers.auth.AuthManager
import com.rc.feature.offers.auth.AuthUiState
import com.rc.feature.offers.auth.AuthViewModel
import com.rc.feature.offers.auth.User // Assuming User is defined here
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
class AuthViewModelTest {

    // Dependency changed: AuthViewModel now uses AuthManager directly
    @MockK
    private lateinit var mockAuthManager: AuthManager

    private lateinit var viewModel: AuthViewModel

    private val testDispatcher = StandardTestDispatcher()

    // Style guide fixes applied
    private val testFullName = "John Doe"
    private val testEmail = "john@example.com"
    private val testPassword = "password123"

    // FIX APPLIED HERE: Changed 'name' to 'fullName' to resolve the parameter mismatch error.
    private val testUser = User(id = "1", email = testEmail, fullName = testFullName)

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
        // ViewModel instantiation changed
        viewModel = AuthViewModel(mockAuthManager)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // --- Sign-In Logic Tests ---

    @Test
    fun `signIn - should transition from Idle to Loading and then to Success`() = runTest {
        // Arrange
        // Mocking the specific function and arguments required by the ViewModel
        coEvery { mockAuthManager.signIn(testEmail, testPassword) } returns Result.success(testUser)

        // Assert starting state
        assertEquals(AuthUiState.Idle, viewModel.state.value)

        // Act
        viewModel.signIn(testEmail, testPassword)

        // Assert pre-advance (Loading state)
        assertEquals(AuthUiState.Loading, viewModel.state.value)

        // Advance coroutine
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert post-advance (Success state)
        val finalState = viewModel.state.value
        assertTrue(finalState is AuthUiState.Success)
        assertEquals(testUser, finalState.user)
    }

    @Test
    fun `signIn - should transition from Idle to Loading and then to Error on failure`() = runTest {
        // Arrange
        val errorMessage = "Bad password"
        coEvery { mockAuthManager.signIn(any(), any()) } returns Result.failure(Exception(errorMessage))

        // Act
        viewModel.signIn(testEmail, testPassword)

        // Assert pre-advance
        assertEquals(AuthUiState.Loading, viewModel.state.value)

        // Advance coroutine
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert post-advance (Error state)
        val finalState = viewModel.state.value
        assertTrue(finalState is AuthUiState.Error)
        assertEquals(errorMessage, finalState.message)
    }

    // --- Sign-Up Logic Tests ---

    @Test
    fun `signUp - should transition from Idle to Loading and then to Success`() = runTest {
        // Arrange
        // The new ViewModel's signUp function only takes email and password
        coEvery { mockAuthManager.signUp(testEmail, testPassword) } returns Result.success(testUser)

        // Act
        viewModel.signUp(testEmail, testPassword)

        // Assert pre-advance
        assertEquals(AuthUiState.Loading, viewModel.state.value)

        // Advance coroutine
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert post-advance (Success state)
        val finalState = viewModel.state.value
        assertTrue(finalState is AuthUiState.Success)
        assertEquals(testUser, finalState.user)
    }

    @Test
    fun `signUp - should transition from Idle to Loading and then to Error on failure`() = runTest {
        // Arrange
        val errorMessage = "User already exists"
        coEvery { mockAuthManager.signUp(any(), any()) } returns Result.failure(Exception(errorMessage))

        // Act
        viewModel.signUp(testEmail, testPassword)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert post-advance (Error state)
        val finalState = viewModel.state.value
        assertTrue(finalState is AuthUiState.Error)
        assertEquals(errorMessage, finalState.message)
    }

    // --- Sign-Out Test ---

    @Test
    fun `signOut should transition to Idle state`() = runTest {
        // Arrange
        // Set an initial state (Success)
        viewModel.signIn(testEmail, testPassword) // This will set the state to Success
        testDispatcher.scheduler.advanceUntilIdle()

        // Mock the signOut method (it doesn't return anything useful but should be mocked)
        coEvery { mockAuthManager.signOut() } returns Unit

        // Act
        viewModel.signOut()
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertEquals(AuthUiState.Idle, viewModel.state.value)
    }
}