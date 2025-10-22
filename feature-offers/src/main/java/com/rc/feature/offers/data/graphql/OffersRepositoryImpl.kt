// feature-offers/src/main/java/com/rc/feature/offers/data/OffersRepositoryImpl.kt
package com.rc.feature.offers.data.graphql

import com.rc.feature.offers.data.OffersRepository
import com.rc.feature.offers.data.graphql.OffersGraphQLService
import com.rc.feature.offers.data.graphql.OffersGraphQLService.Companion.BOOK_NOW_MUTATION
import com.rc.feature.offers.data.graphql.OffersGraphQLService.Companion.OFFER_DETAILS_QUERY
import com.rc.feature.offers.data.graphql.OffersGraphQLService.Companion.OFFERS_QUERY
import com.rc.feature.offers.domain.OfferDetails
import com.rc.feature.offers.domain.OfferSummary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OffersRepositoryImpl(
    private val service: OffersGraphQLService
) : OffersRepository {

    override suspend fun getOffers(): List<OfferSummary> = withContext(Dispatchers.IO) {
        val request = GraphQLRequest(query = OFFERS_QUERY.trimIndent())
        val resp = service.fetchOffers(request)
        val dtos = resp.data?.offers.orEmpty()
        dtos.map { dto ->
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
        val dto = resp.data?.offer ?: error("Offer not found")
        OfferDetails(
            id = dto.id,
            title = dto.title,
            image = dto.image,
            description = dto.description,
            itinerary = dto.itinerary,
            price = dto.price
        )
    }

    override suspend fun bookNow(offerId: String, guest: String, email: String) = withContext(Dispatchers.IO) {
        val req = GraphQLRequest(
            query = BOOK_NOW_MUTATION.trimIndent(),
            variables = mapOf(
                "offerId" to offerId,
                "guest" to guest,
                "email" to email
            )
        )
        val resp = service.bookNow(req)
        resp.data?.bookNow ?: error("No response")
    }
}
