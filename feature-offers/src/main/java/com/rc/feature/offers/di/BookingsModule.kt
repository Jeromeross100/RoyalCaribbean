package com.rc.feature.offers.di

// feature-offers/src/main/java/com/rc/feature/offers/di/BookingsModule.kt
// Wire BookingsRepository into Hilt.

import com.rc.feature.offers.bookings.BookingsRepository
import com.rc.feature.offers.bookings.BookingsRepositoryImpl
import com.rc.feature.offers.data.graphql.OffersGraphQLService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BookingsModule {
    @Provides @Singleton
    fun provideBookingsRepo(service: OffersGraphQLService): BookingsRepository =
        BookingsRepositoryImpl(service)
}
