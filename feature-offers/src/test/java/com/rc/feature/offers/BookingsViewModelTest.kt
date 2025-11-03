package com.rc.feature.offers

import com.rc.feature.offers.bookings.BookingsRepository
import com.rc.feature.offers.bookings.BookingsViewModel
import com.rc.feature.offers.data.graphql.BookingDto
import com.rc.feature.offers.data.graphql.CancelResult
import com.rc.feature.offers.util.UIState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class BookingsViewModelTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var mockRepo: BookingsRepository

    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    private lateinit var viewModel: BookingsViewModel

    private val mockBookings = listOf(
        BookingDto(
            id = "1",
            offerId = "OFFER_A",
            createdAt = "2025-11-03T10:00:00Z",
            email = "test@example.com",
            guestName = "John Doe",
            confirmationId = "CONF-001"
        ),
        BookingDto(
            id = "2",
            offerId = "OFFER_B",
            createdAt = "2025-11-04T10:00:00Z",
            email = "test@example.com",
            guestName = "Jane Smith",
            confirmationId = "CONF-002"
        )
    )

    private val testException = RuntimeException("Network Error")

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = BookingsViewModel(mockRepo)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // --- Test Cases for load() ---

    @Test
    fun `load should transition state from Loading to Success when repository returns data`() = testScope.runTest {
        `when`(mockRepo.list()).thenReturn(mockBookings)

        viewModel.load()
        advanceUntilIdle()

        val state = viewModel.state.value
        require(state is UIState.Success<*>) { "Expected Success state but got $state" }
        assertEquals(mockBookings, state.data)
    }

    @Test
    fun `load should transition state from Loading to Error when repository throws an exception`() = testScope.runTest {
        `when`(mockRepo.list()).thenThrow(testException)

        viewModel.load()
        advanceUntilIdle()

        val state = viewModel.state.value
        require(state is UIState.Error) { "Expected Error state but got $state" }
        assertEquals("Failed to load bookings", state.message)
    }

    // --- Test Cases for cancel() ---

    @Test
    fun `cancel should call repo_cancel and load bookings again on success`() = testScope.runTest {
        val bookingId = "123"
        val successResult = CancelResult(ok = true, message = "Success", id = bookingId)

        `when`(mockRepo.cancel(bookingId)).thenReturn(successResult)
        `when`(mockRepo.list()).thenReturn(mockBookings)

        var capturedResult: CancelResult? = null
        val onDone: (CancelResult) -> Unit = { capturedResult = it }

        viewModel.cancel(bookingId, onDone)
        advanceUntilIdle()

        verify(mockRepo).cancel(bookingId)
        assertEquals(successResult, capturedResult)
        verify(mockRepo).list()

        val state = viewModel.state.value
        require(state is UIState.Success<*>) { "Expected Success state but got $state" }
        assertEquals(mockBookings, state.data)
    }

    @Test
    fun `cancel should call repo_cancel and load bookings again on failure`() = testScope.runTest {
        val bookingId = "456"
        val errorException = RuntimeException("Cancel API Error")
        val expectedResult = CancelResult(ok = false, message = errorException.message ?: "", id = null)

        `when`(mockRepo.cancel(bookingId)).thenThrow(errorException)
        `when`(mockRepo.list()).thenReturn(mockBookings)

        var capturedResult: CancelResult? = null
        val onDone: (CancelResult) -> Unit = { capturedResult = it }

        viewModel.cancel(bookingId, onDone)
        advanceUntilIdle()

        verify(mockRepo).cancel(bookingId)
        assertEquals(expectedResult.ok, capturedResult!!.ok)
        assertEquals(expectedResult.message, capturedResult!!.message)
        verify(mockRepo).list()

        val state = viewModel.state.value
        require(state is UIState.Success<*>) { "Expected Success state but got $state" }
    }
}
