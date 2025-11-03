package com.rc.feature.offers

// Dependencies needed: JUnit, MockK, kotlinx-coroutines-test
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import java.io.IOException

// Import your mock data and DTOs
import com.rc.feature.offers.data.graphql.*
import com.rc.feature.offers.bookings.*

// --- Mock DTO Definitions (to resolve Unresolved references) ---

// 🛠️ FIX: Defining the necessary DTOs within the same package for compilation
data class CancelResultDto(
    val ok: Boolean,
    val id: String?,
    val message: String?
)
// NOTE: These are simplified for compilation; the actual DTOs live in the graphql package.
data class BookingsListEnvelope(val data: BookingsListData)
data class BookingsListData(val bookings: List<BookingDto>)
data class CancelEnvelope(val data: CancelData)
data class CancelData(val cancelBooking: CancelResultDto?)

// --- Mock Service (Must be in scope) ---
interface OffersGraphQLService {
    suspend fun fetchBookings(userId: String? = null): BookingsListEnvelope
    suspend fun cancelBooking(bookingId: String): CancelEnvelope
}

// --- Mock Cancel Results ---
object MockCancelResults {

    // Utility function to create a successful result
    fun success(id: String) = CancelResultDto(
        ok = true,
        id = id,
        message = "Success"
    )

    // Business rule failure mock
    val FAILURE_BUSINESS_RULE = CancelResultDto(
        ok = false,
        id = null,
        message = "Cancellation failed: Booking is within 7 days of departure."
    )

    // Default failure for when the service returns null data
    val REPOSITORY_DEFAULT_FAILURE = CancelResultDto(
        ok = false,
        id = null,
        message = "Cancellation failed due to unknown service response."
    )
}

class BookingsRepositoryImplTest {

    // Assuming this class is defined in com.rc.feature.offers.bookings
    private lateinit var mockService: OffersGraphQLService
    private lateinit var repository: BookingsRepositoryImpl

    @Before
    fun setup() {
        // Initialize mock dependencies
        mockService = mockk()
        repository = BookingsRepositoryImpl(mockService)
    }

    // --- 1. list() Function Tests ---

    @Test
    fun list_Success_ReturnsBookings() = runTest {
        // Arrange: Mock GraphQL service to return a list of bookings
        // NOTE: MockBookings and BookingDto must be defined externally or in this package.
        val mockData = MockBookings.ALL_MOCK_BOOKINGS
        coEvery { mockService.fetchBookings(any()) } returns
                BookingsListEnvelope(BookingsListData(mockData))

        // Act
        val result = repository.list()

        // Assert
        assertEquals(mockData, result)
    }

    @Test
    fun list_EmptyResponse_ReturnsEmptyList() = runTest {
        // Arrange: Mock GraphQL service to return no bookings
        coEvery { mockService.fetchBookings(any()) } returns
                BookingsListEnvelope(BookingsListData(emptyList()))

        // Act
        val result = repository.list()

        // Assert
        assertEquals(0, result.size)
        assertEquals(emptyList(), result)
    }

    @Test
    fun list_NetworkError_ThrowsException() = runTest {
        // Arrange: Mock GraphQL service to throw an IOException (network failure)
        coEvery { mockService.fetchBookings(any()) } throws IOException("Network down")

        // Assert
        assertFailsWith<IOException> {
            repository.list()
        }
    }

    // --- 2. cancel() Function Tests ---

    @Test
    fun cancel_Success_ReturnsOkTrue() = runTest {
        val targetId = MockBookings.CANCEL_TARGET_BOOKING.id
        // Arrange: Mock GraphQL service to return a successful cancellation result
        coEvery { mockService.cancelBooking(any()) } returns
                CancelEnvelope(CancelData(MockCancelResults.success(targetId)))

        // Act
        val result = repository.cancel(targetId)

        // Assert
        assertEquals(true, result.ok)
        assertEquals(targetId, result.id)
    }

    @Test
    fun cancel_Failure_ReturnsOkFalse() = runTest {
        val targetId = "some-id"
        // Arrange: Mock GraphQL service to return a business failure result
        coEvery { mockService.cancelBooking(any()) } returns
                CancelEnvelope(CancelData(MockCancelResults.FAILURE_BUSINESS_RULE))

        // Act
        val result = repository.cancel(targetId)

        // Assert
        assertEquals(false, result.ok)
        assertEquals(null, result.id)
        assertEquals("Cancellation failed: Booking is within 7 days of departure.", result.message)
    }

    @Test
    fun cancel_NoResponseData_ReturnsDefaultFailure() = runTest {
        val targetId = "some-id"
        // Arrange: Mock GraphQL service returns an envelope with null data
        coEvery { mockService.cancelBooking(any()) } returns
                CancelEnvelope(CancelData(null))

        // Act
        val result = repository.cancel(targetId)

        // Assert
        assertEquals(MockCancelResults.REPOSITORY_DEFAULT_FAILURE.ok, result.ok)
        assertEquals(MockCancelResults.REPOSITORY_DEFAULT_FAILURE.message, result.message)
    }
}