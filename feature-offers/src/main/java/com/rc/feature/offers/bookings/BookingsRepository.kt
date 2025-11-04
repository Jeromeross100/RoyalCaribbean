// feature-offers/src/main/java/com/rc/feature/offers/bookings/BookingsRepository.kt

package com.rc.feature.offers.bookings

// FIXED IMPORTS: Use the standard 'data' package for all shared DTOs
import com.rc.feature.offers.data.BookingDto // CORRECT DTO PACKAGE
import com.rc.feature.offers.data.CancelResult // CORRECT DTO PACKAGE
import com.rc.feature.offers.data.GraphQLRequest // CORRECT DTO PACKAGE
import com.rc.feature.offers.data.graphql.OffersGraphQLService // GraphQLService stays in its package
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

interface BookingsRepository {
    // Interface now uses the correct DTO package
    suspend fun list(): List<BookingDto>
    suspend fun cancel(id: String): CancelResult
}

class BookingsRepositoryImpl @Inject constructor(
    private val service: OffersGraphQLService
) : BookingsRepository {

    override suspend fun list(): List<BookingDto> = withContext(Dispatchers.IO) {
        val request = GraphQLRequest(
            query = OffersGraphQLService.BOOKINGS_QUERY.trimIndent()
        )
        // NOTE: While the response object is an Envelope, the list() implementation 
        // correctly extracts the desired List<BookingDto> from inside the envelope.
        service.fetchBookings(request).data?.bookings.orEmpty()
    }

    override suspend fun cancel(id: String): CancelResult = withContext(Dispatchers.IO) {
        val request = GraphQLRequest(
            query = OffersGraphQLService.CANCEL_BOOKING_MUTATION.trimIndent(),
            variables = mapOf("id" to id)
        )

        // Ensure you extract and handle errors/nulls for mutations as well
        val resp = service.cancelBooking(request)

        if (!resp.errors.isNullOrEmpty()) {
            throw IOException("GraphQL Error during cancellation: ${resp.errors.firstOrNull()}")
        }

        resp.data?.cancelBooking
            ?: CancelResult(
                ok = false,
                message = "No response from cancellation service.",
                id = id
            )
    }
}