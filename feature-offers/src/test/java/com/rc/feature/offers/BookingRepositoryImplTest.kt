package com.rc.feature.offers

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import java.io.IOException

// Use the real GraphQL service and models
import com.rc.feature.offers.data.graphql.*
import com.rc.feature.offers.bookings.BookingsRepositoryImpl

class BookingsRepositoryImplTest {

    private lateinit var mockService: OffersGraphQLService
    private lateinit var repository: BookingsRepositoryImpl

    @Before
    fun setup() {
        mockService = mockk()
        repository = BookingsRepositoryImpl(mockService)
    }

    // list() - successful response
    @Test
    fun list_Success_ReturnsBookings() = runTest {
        val mockData = listOf(
            BookingDto(
                id = "1",
                offerId = "OFFER_A",
                createdAt = "2025-11-03T10:00:00Z",
                email = "user@example.com",
                guestName = "John Doe",
                confirmationId = "CONF-001"
            ),
            BookingDto(
                id = "2",
                offerId = "OFFER_B",
                createdAt = "2025-11-04T10:00:00Z",
                email = "user@example.com",
                guestName = "Jane Smith",
                confirmationId = "CONF-002"
            )
        )

        val response = BookingsListEnvelope(BookingsListData(mockData))
        coEvery { mockService.fetchBookings(any()) } returns response

        val result = repository.list()
        assertEquals(mockData, result)
    }

    // list() - empty response
    @Test
    fun list_EmptyResponse_ReturnsEmptyList() = runTest {
        val response = BookingsListEnvelope(BookingsListData(emptyList()))
        coEvery { mockService.fetchBookings(any()) } returns response

        val result = repository.list()
        assertEquals(emptyList(), result)
    }

    // list() - network failure
    @Test
    fun list_NetworkError_ThrowsException() = runTest {
        coEvery { mockService.fetchBookings(any()) } throws IOException("Network down")

        assertFailsWith<IOException> {
            repository.list()
        }
    }

    // cancel() - success
    @Test
    fun cancel_Success_ReturnsOkTrue() = runTest {
        val targetId = "123"
        val successEnvelope = CancelEnvelope(
            CancelData(CancelResult(ok = true, message = "Success", id = targetId))
        )

        coEvery { mockService.cancelBooking(any()) } returns successEnvelope

        val result = repository.cancel(targetId)
        assertEquals(true, result.ok)
        assertEquals(targetId, result.id)
        assertEquals("Success", result.message)
    }

    // cancel() - failure
    @Test
    fun cancel_Failure_ReturnsOkFalse() = runTest {
        val failureEnvelope = CancelEnvelope(
            CancelData(CancelResult(ok = false, message = "Failed", id = null))
        )

        coEvery { mockService.cancelBooking(any()) } returns failureEnvelope

        val result = repository.cancel("123")
        assertEquals(false, result.ok)
        assertEquals(null, result.id)
        assertEquals("Failed", result.message)
    }

    // cancel() - null response from service
    @Test
    fun cancel_NoResponseData_ReturnsDefaultFailure() = runTest {
        val nullEnvelope = CancelEnvelope(CancelData(null))
        coEvery { mockService.cancelBooking(any()) } returns nullEnvelope

        val result = repository.cancel("123")
        assertEquals(false, result.ok)
        assertEquals("No response from cancellation service.", result.message)
    }
}
