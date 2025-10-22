package com.rc.feature.offers.data.graphql

// feature-offers/src/main/java/com/rc/feature/offers/data/graphql/OffersGraphQLService.kt

import retrofit2.http.Body
import retrofit2.http.POST

/**
 * GraphQL transport for Offers.
 * Matches the usage in OffersRepositoryImpl:
 *  - fetchOffers(GraphQLRequest) -> OffersListEnvelope
 *  - fetchOfferDetails(GraphQLRequest) -> OfferDetailsEnvelope
 *  - bookNow(GraphQLRequest) -> BookNowEnvelope
 * Also exposes the query/mutation strings used by the repository.
 */
interface OffersGraphQLService {

    @POST("graphql")
    suspend fun fetchOffers(@Body body: GraphQLRequest): OffersListEnvelope

    @POST("graphql")
    suspend fun fetchOfferDetails(@Body body: GraphQLRequest): OfferDetailsEnvelope

    @POST("graphql")
    suspend fun bookNow(@Body body: GraphQLRequest): BookNowEnvelope

    companion object {
        // List of offers used on Screen 1
        const val OFFERS_QUERY = """
            query Offers {
              offers {
                id
                title
                image
                shortDescription
                price
              }
            }
        """

        // Details used on Screen 2
        const val OFFER_DETAILS_QUERY = """
            query Offer(${'$'}id: ID!) {
              offer(id: ${'$'}id) {
                id
                title
                image
                description
                itinerary
                price
              }
            }
        """

        // Optional "Book Now" placeholder mutation
        const val BOOK_NOW_MUTATION = """
            mutation BookNow(${'$'}offerId: ID!, ${'$'}guest: String!, ${'$'}email: String!) {
              bookNow(offerId: ${'$'}offerId, guestName: ${'$'}guest, email: ${'$'}email) {
                ok
                message
                booking {
                  id
                  confirmationId
                  offerId
                  guestName
                  email
                  createdAt
                }
              }
            }
        """
    }
}
