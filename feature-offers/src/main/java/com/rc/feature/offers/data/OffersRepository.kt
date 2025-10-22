// feature-offers/src/main/java/com/rc/feature/offers/data/OffersRepository.kt
package com.rc.feature.offers.data

import com.rc.feature.offers.data.graphql.BookNowResult
import com.rc.feature.offers.domain.OfferDetails
import com.rc.feature.offers.domain.OfferSummary

interface OffersRepository {
    suspend fun getOffers(): List<OfferSummary>
    suspend fun getOfferDetails(id: String): OfferDetails
    // Optional (only if you wired the mutation)
    suspend fun bookNow(offerId: String, guest: String, email: String): BookNowResult
}
