package com.rifftyo.healingkuy.di

import com.rifftyo.core.domain.usecase.destination.DestinationInteractor
import com.rifftyo.core.domain.usecase.destination.DestinationUseCase
import com.rifftyo.core.domain.usecase.user.UserInteractor
import com.rifftyo.core.domain.usecase.user.UserUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun provideUserUseCase(userInteractor: UserInteractor): UserUseCase

    @Binds
    @Singleton
    abstract fun provideDestinationUseCase(destinationInteractor: DestinationInteractor): DestinationUseCase
}