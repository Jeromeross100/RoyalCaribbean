// feature-offers/src/main/java/com/rc/feature/offers/di/AuthRepositoryModule.kt
package com.rc.feature.offers.di

import com.rc.feature.offers.auth.AuthRepository
import com.rc.feature.offers.auth.FakeAuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Suppress("unused") // Hilt uses this reflectively; Studio can't see call sites
@Module
@InstallIn(SingletonComponent::class)
abstract class AuthRepositoryModule {

    // This binding is now the only one, resolving the Dagger error.
    @Binds
    @Singleton
    @Suppress("unused") // same reason as above
    abstract fun bindAuthRepository(impl: FakeAuthRepository): AuthRepository
}