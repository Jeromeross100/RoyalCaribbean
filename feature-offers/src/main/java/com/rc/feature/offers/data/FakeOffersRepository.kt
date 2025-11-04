// feature-offers/src/main/java/com/rc/feature/offers/data/FakeOffersRepository.kt
package com.rc.feature.offers.data

import com.rc.feature.offers.domain.OfferDetails
import com.rc.feature.offers.domain.OfferSummary
import kotlinx.coroutines.delay
import java.util.UUID

class FakeOffersRepository : OffersRepository {

    private val items = (1..8).map {
        OfferSummary(
            id = it.toString(),
            title = "3-Night Bahamas & Perfect Day #$it",
            image = null,
            shortDescription = "Sail from Miami to CocoCay and Nassau on Icon class.",
            price = "$${199 + it * 10} pp"
        )
    }

    // NEW FAKE DATA: A small list of fake bookings
    private val fakeBookings = listOf(
        BookingDto(
            id = "b1",
            confirmationId = "RC-CRUISE1",
            offerId = "1",
            guestName = "Jane Doe",
            email = "jane@example.com",
            createdAt = "1700000000000"
        ),
        BookingDto(
            id = "b2",
            confirmationId = "RC-CRUISE2",
            offerId = "3",
            guestName = "Jane Doe",
            email = "jane@example.com",
            createdAt = "1700100000000"
        )
    )

    // --- FIX: Implementation for the abstract member from OffersRepository ---
    override suspend fun getBookings(): List<BookingDto> {
        delay(300)
        return fakeBookings
    }
    // -------------------------------------------------------------------------

    override suspend fun getOffers(): List<OfferSummary> {
        delay(400)
        return items
    }

    override suspend fun getOfferDetails(id: String): OfferDetails {
        delay(300)
        val base = items.first { it.id == id }
        return OfferDetails(
            id = base.id,
            title = base.title,
            image = base.image,
            description = "Full description for ${base.title}. Modern staterooms, dining, entertainment, and more.",
            itinerary = "Day 1: Miami • Day 2: Perfect Day at CocoCay • Day 3: Nassau • Day 4: Miami",
            price = base.price
        )
    }

    override suspend fun bookNow(offerId: String, guest: String, email: String): BookNowResult {
        delay(250)
        val confirmation = UUID.randomUUID().toString().takeLast(8).uppercase()

        return BookNowResult(
            ok = true,
            message = "Booking confirmed (FAKE)",
            booking = BookingDto(
                id = UUID.randomUUID().toString(),
                confirmationId = "RC-$confirmation",
                offerId = offerId,
                guestName = guest,
                email = email,
                createdAt = System.currentTimeMillis().toString()
            )
        )
    }
}