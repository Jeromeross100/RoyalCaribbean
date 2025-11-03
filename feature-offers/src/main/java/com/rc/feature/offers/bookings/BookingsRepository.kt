package com.rc.feature.offers.bookings
// feature-offers/src/main/java/com/rc/feature/offers/bookings/BookingsRepository.kt

import com.rc.feature.offers.data.graphql.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface BookingsRepository {
    suspend fun list(): List<BookingDto>
    suspend fun cancel(id: String): CancelResult
}

class BookingsRepositoryImpl(
    private val service: com.rc.feature.offers.OffersGraphQLService
) : BookingsRepository {
    override suspend fun list(): List<BookingDto> = withContext(Dispatchers.IO) {
        val req = GraphQLRequest(query = OffersGraphQLService.BOOKINGS_QUERY.trimIndent())
        service.fetchBookings(req).data?.bookings.orEmpty()
    }

    override suspend fun cancel(id: String): CancelResult = withContext(Dispatchers.IO) {
        val req = GraphQLRequest(
            query = OffersGraphQLService.CANCEL_BOOKING_MUTATION.trimIndent(),
            variables = mapOf("id" to id)
        )
        service.cancelBooking(req).data?.cancel ?: CancelResult(false, "No response", null)
    }
}
