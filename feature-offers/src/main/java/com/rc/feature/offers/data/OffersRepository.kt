// feature-offers/src/main/java/com/rc/feature/offers/data/OffersRepository.kt
package com.rc.feature.offers.data

// Ensure this is imported/defined correctly
import com.rc.feature.offers.domain.OfferDetails
import com.rc.feature.offers.domain.OfferSummary

interface OffersRepository {
    suspend fun getOffers(): List<OfferSummary>
    suspend fun getOfferDetails(id: String): OfferDetails

    // FIX 1: Add the getBookings method
    suspend fun getBookings(): List<BookingDto>

    // FIX 2: Ensure the return type matches the implementation (BookNowResult)
    suspend fun bookNow(offerId: String, guest: String, email: String): BookNowResult
}