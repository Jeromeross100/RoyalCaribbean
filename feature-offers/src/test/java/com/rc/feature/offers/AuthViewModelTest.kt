package com.rc.feature.offers

import com.rc.feature.offers.auth.AuthRepository
import com.rc.feature.offers.auth.User
import com.rc.feature.offers.ui.auth.AuthEvent
import com.rc.feature.offers.ui.auth.AuthViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertFalse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {

    @MockK
    private lateinit var mockRepo: AuthRepository

    private lateinit var viewModel: AuthViewModel
    private val testDispatcher = StandardTestDispatcher()

    private val testFullName = "John Doe"
    private val testEmail = "john@example.com"
    private val testPassword = "password123"

    // FIX: use correct param names for User data class
    private val testUser = User(id = "1", fullName = testFullName, email = testEmail)

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = AuthViewModel(mockRepo)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // --- SIGN IN TESTS ---

    @Test
    fun `submit in SignIn mode should update state to Success and emit SignedIn`() = runTest {
        coEvery { mockRepo.signIn(testEmail, testPassword) } returns Result.success(testUser)

        viewModel.updateEmail(testEmail)
        viewModel.updatePassword(testPassword)
        viewModel.submit()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertEquals(testUser, state.user)
        assertNull(state.error)

        val event = viewModel.events.first()
        assertTrue(event is AuthEvent.SignedIn)
        assertEquals(testUser, event.user)
    }

    @Test
    fun `submit in SignIn mode should update state to Error on failure`() = runTest {
        val errorMsg = "Invalid credentials"
        coEvery { mockRepo.signIn(testEmail, testPassword) } returns Result.failure(Exception(errorMsg))

        viewModel.updateEmail(testEmail)
        viewModel.updatePassword(testPassword)
        viewModel.submit()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertNull(state.user)
        assertEquals(errorMsg, state.error)
    }

    // --- SIGN UP TESTS ---

    @Test
    fun `submit in CreateAccount mode should update state to Success and emit SignedUp`() = runTest {
        coEvery { mockRepo.signUp(testFullName, testEmail, testPassword) } returns Result.success(testUser)

        viewModel.toggleMode() // switch to CreateAccount
        viewModel.updateFullName(testFullName)
        viewModel.updateEmail(testEmail)
        viewModel.updatePassword(testPassword)
        viewModel.submit()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertEquals(testUser, state.user)
        assertNull(state.error)

        val event = viewModel.events.first()
        assertTrue(event is AuthEvent.SignedUp)
        assertEquals(testUser, event.user)
    }

    @Test
    fun `submit in CreateAccount mode should update state to Error on failure`() = runTest {
        val errorMsg = "User already exists"
        coEvery { mockRepo.signUp(any(), any(), any()) } returns Result.failure(Exception(errorMsg))

        viewModel.toggleMode()
        viewModel.updateFullName(testFullName)
        viewModel.updateEmail(testEmail)
        viewModel.updatePassword(testPassword)
        viewModel.submit()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertNull(state.user)
        assertEquals(errorMsg, state.error)
    }

    // --- MODE TOGGLE TEST ---

    @Test
    fun `toggleMode should switch between SignIn and CreateAccount`() {
        val initialMode = viewModel.state.value.mode
        viewModel.toggleMode()
        assertNotEquals(initialMode, viewModel.state.value.mode)
        viewModel.toggleMode()
        assertEquals(initialMode, viewModel.state.value.mode)
    }

    // --- PASSWORD VISIBILITY TEST ---

    @Test
    fun `togglePasswordVisibility should flip boolean state`() {
        val initial = viewModel.state.value.isPasswordVisible
        viewModel.togglePasswordVisibility()
        assertEquals(!initial, viewModel.state.value.isPasswordVisible)
    }

    // --- FIELD UPDATE TESTS ---

    @Test
    fun `update functions should modify fields correctly`() {
        viewModel.updateFullName(testFullName)
        viewModel.updateEmail(testEmail)
        viewModel.updatePassword(testPassword)

        val state = viewModel.state.value
        assertEquals(testFullName, state.fullName)
        assertEquals(testEmail, state.email)
        assertEquals(testPassword, state.password)
    }
}
