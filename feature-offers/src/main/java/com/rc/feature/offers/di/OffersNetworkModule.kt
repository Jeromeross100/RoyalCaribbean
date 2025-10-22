// feature-offers/src/main/java/com/rc/feature/offers/di/OffersNetworkModule.kt
package com.rc.feature.offers.di

import com.rc.feature.offers.data.graphql.OffersGraphQLService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OffersNetworkModule {

    @Provides
    @Singleton
    fun provideOkHttp(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }
            )
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        val json = Json { ignoreUnknownKeys = true; isLenient = true }
        val contentType = "application/json".toMediaType()

        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:4000/") // emulator -> host machine
            .addConverterFactory(json.asConverterFactory(contentType))
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideService(retrofit: Retrofit): OffersGraphQLService =
        retrofit.create(OffersGraphQLService::class.java) // explicit type fixes inference error
}
