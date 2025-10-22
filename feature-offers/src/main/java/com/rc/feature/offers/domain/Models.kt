package com.rc.feature.offers.domain
data class OfferSummary(
    val id: String,
    val title: String,
    val image: String?,
    val shortDescription: String,
    val price: String
)
data class OfferDetails(
    val id: String,
    val title: String,
    val image: String?,
    val description: String,
    val itinerary: String,
    val price: String
)
