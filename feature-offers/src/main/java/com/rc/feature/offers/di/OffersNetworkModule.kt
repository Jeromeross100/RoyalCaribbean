// feature-offers/src/main/java/com/rc/feature/offers/di/OffersNetworkModule.kt

package com.rc.feature.offers.di

import com.rc.feature.offers.bookings.BookingsRepository
import com.rc.feature.offers.bookings.BookingsRepositoryImpl
import com.rc.feature.offers.data.OffersRepository
import com.rc.feature.offers.data.OffersService
import com.rc.feature.offers.data.graphql.OffersGraphQLService
import com.rc.feature.offers.data.graphql.OffersRepositoryImpl
// This line was causing a conflict due to duplicate name (OffersRepositoryImpl)
// import com.rc.feature.offers.data.graphql.OffersRepositoryImpl // Make sure this line is NOT present

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "http://10.0.2.2:4000/"

@Module
@InstallIn(SingletonComponent::class)
// This abstract class contains both @Binds (implicitly) and @Provides (via companion object)
abstract class OffersDataBindingsModule {

    // --- @BINDS METHODS (Interface bindings) ---

    @Binds
    @Singleton
    abstract fun bindOffersRepository(
        impl: OffersRepositoryImpl
    ): OffersRepository

    @Binds
    @Singleton
    abstract fun bindBookingsRepository(
        impl: BookingsRepositoryImpl
    ): BookingsRepository

    // --- @PROVIDES METHODS (Instance creation) ---

    // FIX: Remove the redundant @InstallIn annotation.
    // The outer class's @Module and @InstallIn annotations apply here.
    companion object {

        @Provides
        @Singleton
        fun provideOkHttpClient(): OkHttpClient {
            return OkHttpClient.Builder()
                .build()
        }

        @Provides
        @Singleton
        fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        @Suppress("UNUSED")
        @Provides
        @Singleton
        fun provideOffersService(retrofit: Retrofit): OffersService {
            return retrofit.create(OffersService::class.java)
        }

        @Suppress("UNUSED")
        @Provides
        @Singleton
        fun provideOffersGraphQLService(retrofit: Retrofit): OffersGraphQLService {
            return retrofit.create(OffersGraphQLService::class.java)
        }
    }
}