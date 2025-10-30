package com.rc.feature.offers.di

import com.rc.feature.offers.auth.AuthManager
import com.rc.feature.offers.auth.AuthManagerContract
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthBindingsModule {
    @Binds
    @Singleton
    abstract fun bindAuthManager(impl: AuthManager): AuthManagerContract
}
