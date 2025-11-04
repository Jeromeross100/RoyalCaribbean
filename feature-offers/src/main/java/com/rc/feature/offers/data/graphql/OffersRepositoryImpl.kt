// feature-offers/src/main/java/com/rc/feature/offers/data/OffersRepositoryImpl.kt
package com.rc.feature.offers.data.graphql // CORRECTED: Should not be .graphql

import com.rc.feature.offers.data.BookNowResult
import com.rc.feature.offers.data.BookingDto
import com.rc.feature.offers.data.GraphQLRequest
import com.rc.feature.offers.data.OffersRepository
import com.rc.feature.offers.data.graphql.OffersGraphQLService.Companion.BOOK_NOW_MUTATION
import com.rc.feature.offers.data.graphql.OffersGraphQLService.Companion.BOOKINGS_QUERY
import com.rc.feature.offers.data.graphql.OffersGraphQLService.Companion.OFFER_DETAILS_QUERY
import com.rc.feature.offers.data.graphql.OffersGraphQLService.Companion.OFFERS_QUERY
import com.rc.feature.offers.domain.OfferDetails
import com.rc.feature.offers.domain.OfferSummary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.NoSuchElementException // Import the exception type
import javax.inject.Inject

class OffersRepositoryImpl @Inject constructor(
    private val service: OffersGraphQLService
) : OffersRepository {

    override suspend fun getOffers(): List<OfferSummary> = withContext(Dispatchers.IO) {
        val request = GraphQLRequest(query = OFFERS_QUERY.trimIndent())
        val resp = service.fetchOffers(request)

        // UPDATED: Using !isNullOrEmpty() for concise check
        if (!resp.errors.isNullOrEmpty()) {
            throw IOException("GraphQL Error during getOffers: ${resp.errors.firstOrNull()}")
        }

        resp.data?.offers.orEmpty().map { dto ->
            OfferSummary(
                id = dto.id,
                title = dto.title,
                image = dto.image,
                shortDescription = dto.shortDescription,
                price = dto.price
            )
        }
    }

    override suspend fun getOfferDetails(id: String): OfferDetails = withContext(Dispatchers.IO) {
        val request = GraphQLRequest(
            query = OFFER_DETAILS_QUERY.trimIndent(),
            variables = mapOf("id" to id)
        )
        val resp = service.fetchOfferDetails(request)

        // UPDATED: Using !isNullOrEmpty()
        if (!resp.errors.isNullOrEmpty()) {
            throw IOException("GraphQL Error during getOfferDetails: ${resp.errors.firstOrNull()}")
        }

        val dto = resp.data?.offer ?: throw NoSuchElementException("Offer with ID $id not found")
        OfferDetails(
            id = dto.id,
            title = dto.title,
            image = dto.image,
            description = dto.description,
            itinerary = dto.itinerary,
            price = dto.price
        )
    }

    override suspend fun getBookings(): List<BookingDto> = withContext(Dispatchers.IO) {
        val request = GraphQLRequest(query = BOOKINGS_QUERY.trimIndent())
        val resp = service.fetchBookings(request)

        // UPDATED: Using !isNullOrEmpty()
        if (!resp.errors.isNullOrEmpty()) {
            throw IOException("GraphQL Error during getBookings: ${resp.errors.firstOrNull()}")
        }

        resp.data?.bookings.orEmpty()
    }

    override suspend fun bookNow(offerId: String, guest: String, email: String): BookNowResult = withContext(Dispatchers.IO) {
        val req = GraphQLRequest(
            query = BOOK_NOW_MUTATION.trimIndent(),
            variables = mapOf(
                "offerId" to offerId,
                "guest" to guest,
                "email" to email
            )
        )
        val resp = service.bookNow(req)

        // UPDATED: Using !isNullOrEmpty()
        if (!resp.errors.isNullOrEmpty()) {
            throw IOException("GraphQL Error during bookNow: ${resp.errors.firstOrNull()}")
        }

        resp.data?.bookNow ?: throw IOException("Booking failed: No response from server.")
    }
}