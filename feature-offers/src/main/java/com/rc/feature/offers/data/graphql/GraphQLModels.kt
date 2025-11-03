// feature-offers/src/main/java/com/rc/feature/offers/data/graphql/GraphQLModels.kt
@file:Suppress("unused")

package com.rc.feature.offers.data.graphql

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/* ---------- GENERIC GRAPHQL REQUEST ---------- */
@Serializable
data class GraphQLRequest(
    val query: String,
    val variables: Map<String, @Serializable(with = AnyAsStringSerializer::class) Any?>? = null
)

object AnyAsStringSerializer : KSerializer<Any> {
    override val descriptor =
        PrimitiveSerialDescriptor("AnyAsStringSerializer", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Any = decoder.decodeString()

    override fun serialize(encoder: Encoder, value: Any) =
        encoder.encodeString(value.toString())
}

/* ---------- OFFERS LIST ---------- */
@Serializable
data class OffersListEnvelope(@SerialName("data") val data: OffersListData? = null)

@Serializable
data class OffersListData(@SerialName("offers") val offers: List<OfferSummaryDto> = emptyList())

@Serializable
data class OfferSummaryDto(
    val id: String,
    val title: String,
    val image: String? = null,
    val shortDescription: String,
    val price: String
)

/* ---------- OFFER DETAILS ---------- */
@Serializable
data class OfferDetailsEnvelope(@SerialName("data") val data: OfferDetailsData? = null)

@Serializable
data class OfferDetailsData(@SerialName("offer") val offer: OfferDetailsDto? = null)

@Serializable
data class OfferDetailsDto(
    val id: String,
    val title: String,
    val image: String? = null,
    val description: String,
    val itinerary: String,
    val price: String
)

/* ---------- BOOK NOW (CREATE BOOKING) ---------- */
@Serializable
data class BookNowEnvelope(@SerialName("data") val data: BookNowData? = null)

@Serializable
data class BookNowData(@SerialName("bookNow") val bookNow: BookNowResult? = null)

@Serializable
data class BookNowResult(
    val ok: Boolean,
    val message: String,
    val booking: BookingDto? = null
)

/* ---------- SHARED BOOKING DTO ---------- */
@Serializable
data class BookingDto(
    val id: String,
    val confirmationId: String,
    val offerId: String,
    val guestName: String,
    val email: String,
    val createdAt: String
)

/* ---------- BOOKINGS LIST (MY BOOKINGS) ---------- */
@Serializable
data class BookingsListEnvelope(@SerialName("data") val data: BookingsListData? = null)

@Serializable
data class BookingsListData(@SerialName("bookings") val bookings: List<BookingDto> = emptyList())

/* ---------- CANCEL BOOKING ---------- */
@Serializable
data class CancelEnvelope(@SerialName("data") val data: CancelData? = null)

@Serializable
data class CancelData(@SerialName("cancelBooking") val cancel: CancelResult? = null) {
    val cancelBooking: CancelResult?
        get() {
            return cancel
        }
}

@Serializable
data class CancelResult(
    val ok: Boolean,
    val message: String,
    val id: String? = null
)
