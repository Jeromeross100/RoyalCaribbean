package com.rc.feature.offers.bookings

import com.rc.feature.offers.data.graphql.BookingDto
import com.rc.feature.offers.data.graphql.CancelResult
import com.rc.feature.offers.data.graphql.GraphQLRequest
import com.rc.feature.offers.data.graphql.OffersGraphQLService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface BookingsRepository {
    suspend fun list(): List<BookingDto>
    suspend fun cancel(id: String): CancelResult
}

class BookingsRepositoryImpl(
    private val service: OffersGraphQLService
) : BookingsRepository {

    override suspend fun list(): List<BookingDto> = withContext(Dispatchers.IO) {
        val request = GraphQLRequest(
            query = OffersGraphQLService.BOOKINGS_QUERY.trimIndent()
        )
        service.fetchBookings(request).data?.bookings.orEmpty()
    }

    override suspend fun cancel(id: String): CancelResult = withContext(Dispatchers.IO) {
        val request = GraphQLRequest(
            query = OffersGraphQLService.CANCEL_BOOKING_MUTATION.trimIndent(),
            variables = mapOf("id" to id)
        )

        service.cancelBooking(request).data?.cancelBooking
            ?: CancelResult(
                ok = false,
                message = "No response from cancellation service.",
                id = null
            )
    }
}
