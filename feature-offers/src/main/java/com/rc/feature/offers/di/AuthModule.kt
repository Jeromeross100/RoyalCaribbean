// feature-offers/src/main/java/com/rc/feature/offers/di/AuthModule.kt
package com.rc.feature.offers.di

import com.rc.feature.offers.auth.AuthRepository
import com.rc.feature.offers.auth.FakeAuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {
    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: FakeAuthRepository): AuthRepository
}
