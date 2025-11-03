package com.rc.feature.offers

import com.rc.feature.offers.data.graphql.BookingDto
import java.time.Instant

// Utility function remains public as it might be used globally
fun createMockBooking(
    id: String,
    offerId: String,
    guestName: String,
    confirmationId: String,
    email: String = "${guestName.lowercase().replace(" ", ".")}@rc.com",
    createdAt: String = Instant.now().toString()
) = BookingDto(
    id = id,
    confirmationId = confirmationId,
    offerId = offerId,
    guestName = guestName,
    email = email,
    createdAt = createdAt
)

object MockBookings {
    // 🔴 FIX: Changed visibility to internal to resolve the 'could be private' warnings
    internal val VALID_BOOKING_1 = createMockBooking(
        id = "b-101",
        offerId = "o-miami-001",
        guestName = "Alice Johnson",
        confirmationId = "RCM001234",
        createdAt = "2025-10-15T10:00:00Z"
    )

    internal val VALID_BOOKING_2 = createMockBooking(
        id = "b-102",
        offerId = "o-bahamas-002",
        guestName = "Bob Smith",
        confirmationId = "RCM005678",
        createdAt = "2025-10-16T15:30:00Z"
    )

    // Booking used specifically for testing a successful cancellation attempt
    internal val CANCEL_TARGET_BOOKING = createMockBooking(
        id = "b-999",
        offerId = "o-cancel-test",
        guestName = "Target Tester",
        confirmationId = "RCM999999"
    )

    internal val ALL_MOCK_BOOKINGS = listOf(VALID_BOOKING_1, VALID_BOOKING_2, CANCEL_TARGET_BOOKING)

    internal val EMPTY_BOOKINGS_LIST = emptyList<BookingDto>()
}