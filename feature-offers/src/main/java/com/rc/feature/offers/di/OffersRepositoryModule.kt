// feature-offers/src/main/java/com/rc/feature/offers/di/OffersRepositoryModule.kt
package com.rc.feature.offers.di

import com.rc.feature.offers.BuildConfig
import com.rc.feature.offers.data.FakeOffersRepository
import com.rc.feature.offers.data.OffersRepository
import com.rc.feature.offers.data.graphql.OffersRepositoryImpl
import com.rc.feature.offers.data.graphql.OffersGraphQLService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OffersRepositoryModule {

    @Provides
    @Singleton
    fun provideRepository(service: OffersGraphQLService): OffersRepository =
        if (BuildConfig.USE_FAKE_OFFERS_REPO) FakeOffersRepository() else OffersRepositoryImpl(service)
}
