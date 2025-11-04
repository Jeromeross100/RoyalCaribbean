package com.rc.feature.offers.data

import com.rc.feature.offers.domain.OfferDetails
import com.rc.feature.offers.domain.OfferSummary

data class OfferSummary(
    val id: String,
    val title: String,
    // Ensure you match all fields from OFFERS_QUERY
    val image: String? = null,
    val shortDescription: String,
    val price: String
)

data class BookingDto(
    val id: String,
    val confirmationId: String,
    val offerId: String,
    val guestName: String,
    val email: String,
    val createdAt: String
)

// This matches the return fields for the 'bookNow' mutation
data class BookNowResult(
    val ok: Boolean,
    val message: String,
    val booking: BookingDto?
)

// This matches the return fields for the 'cancelBooking' mutation
data class CancelResult(
    val ok: Boolean,
    val message: String,
    val id: String? // The ID of the item that was canceled
)

// --- 1. The GraphQL Request Body ---
data class GraphQLRequest(
    val query: String,
    // Add variables if your query uses them, e.g. for fetchOfferDetails
    val variables: Map<String, Any?>? = null
)

// --- 2. The Top-Level Response Wrapper (for Offers) ---
data class OffersListEnvelope(
    val data: OffersDataWrapper?,
    val errors: List<Any>? = null
)

// --- 3. The Nested Data Field (for Offers) ---
data class OffersDataWrapper(
    val offers: List<OfferSummary>?
)

// --- 4. The Top-Level Response Wrapper (for Bookings) ---
data class BookingsListEnvelope(
    val data: BookingsDataWrapper?,
    val errors: List<Any>? = null
)

// --- 5. The Nested Data Field (for Bookings) ---
data class BookingsDataWrapper(
    val bookings: List<BookingDto>?
)

// --- 6. Offer Details Envelope (FIXED: Unresolved reference) ---
data class OfferDetailsEnvelope(
    val data: OfferDetailsDataWrapper?,
    val errors: List<Any>? = null
)

// The root field name is 'offer'
data class OfferDetailsDataWrapper(
    val offer: OfferDetails? // Uses the imported domain model
)

// --- 7. Book Now Envelope (Mutation) (FIXED: Unresolved reference) ---
data class BookNowEnvelope(
    val data: BookNowDataWrapper?,
    val errors: List<Any>? = null
)

// The root field name is 'bookNow'
data class BookNowDataWrapper(
    val bookNow: BookNowResult?
)

// --- 8. Cancel Booking Envelope (Mutation) (FIXED: Unresolved reference) ---
data class CancelEnvelope(
    val data: CancelDataWrapper?,
    val errors: List<Any>? = null
)

// The root field name is 'cancelBooking'
data class CancelDataWrapper(
    val cancelBooking: CancelResult?
)