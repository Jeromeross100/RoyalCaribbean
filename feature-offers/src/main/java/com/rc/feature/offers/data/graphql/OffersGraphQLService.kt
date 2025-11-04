// feature-offers/src/main/java/com/rc/feature/offers/data/graphql/OffersGraphQLService.kt
package com.rc.feature.offers.data.graphql

import retrofit2.http.Body
import retrofit2.http.POST
// ADDED IMPORTS for the new data models from the 'com.rc.feature.offers.data' package
import com.rc.feature.offers.data.BookingsListEnvelope
import com.rc.feature.offers.data.BookNowEnvelope
import com.rc.feature.offers.data.CancelEnvelope
import com.rc.feature.offers.data.GraphQLRequest
import com.rc.feature.offers.data.OfferDetailsEnvelope
import com.rc.feature.offers.data.OffersListEnvelope


/**
 * GraphQL transport for Offers & Bookings.
 * Used by the repository layer.
 */
interface OffersGraphQLService {

    // ----- Offers -----
    @POST("graphql")
    suspend fun fetchOffers(@Body body: GraphQLRequest): OffersListEnvelope

    @POST("graphql")
    suspend fun fetchOfferDetails(@Body body: GraphQLRequest): OfferDetailsEnvelope

    @POST("graphql")
    suspend fun bookNow(@Body body: GraphQLRequest): BookNowEnvelope

    // ----- Bookings -----
    @POST("graphql")
    suspend fun fetchBookings(@Body body: GraphQLRequest): BookingsListEnvelope

    @POST("graphql")
    suspend fun cancelBooking(@Body body: GraphQLRequest): CancelEnvelope

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

        // "Book Now" mutation
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

        // NEW: bookings list
        const val BOOKINGS_QUERY = """
            query Bookings {
              bookings {
                id
                confirmationId
                offerId
                guestName
                email
                createdAt
              }
            }
        """

        // NEW: cancel booking
        const val CANCEL_BOOKING_MUTATION = """
            mutation Cancel(${'$'}id: ID!) {
              cancelBooking(id: ${'$'}id) {
                ok
                message
                id
              }
            }
        """
    }
}