package com.rifftyo.core.di

import android.content.Context
import androidx.room.Room
import com.rifftyo.core.data.source.local.room.HealingKuyDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): HealingKuyDatabase {
        return Room.databaseBuilder(
            context,
            HealingKuyDatabase::class.java, "HealingKuy.db"
        ).build()
    }

    @Provides
    fun provideUserDao(database: HealingKuyDatabase) = database.getUserDao()
}