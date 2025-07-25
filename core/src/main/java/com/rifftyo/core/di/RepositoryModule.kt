package com.rifftyo.core.di

import com.rifftyo.core.data.source.repository.DestinationRepository
import com.rifftyo.core.data.source.repository.UserRepository
import com.rifftyo.core.domain.repository.IDestinationRepository
import com.rifftyo.core.domain.repository.IUserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module(includes = [NetworkModule::class, DatabaseModule::class])
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun userRepository(userRepository: UserRepository): IUserRepository

    @Singleton
    @Binds
    abstract fun destinationRepository(destinationRepository: DestinationRepository): IDestinationRepository
}