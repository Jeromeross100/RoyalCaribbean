package com.rc.feature.offers.data

import com.rc.feature.offers.account.LoyaltyStatus
import retrofit2.http.GET

/**
 * Standard Retrofit service for non-GraphQL endpoints, such as Loyalty/Account status.
 * Offer fetching is handled by OffersGraphQLService.
 */
interface OffersService {

    /**
     * Retrieves the guest's current loyalty and account status from a standard REST endpoint.
     */
    // FIX: Suppress the UNUSED warning for this required interface method
    @Suppress("UNUSED")
    @GET("loyalty/status")
    suspend fun getLoyaltyStatus(): LoyaltyStatus
}