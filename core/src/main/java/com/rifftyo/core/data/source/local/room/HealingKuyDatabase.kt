package com.rifftyo.core.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rifftyo.core.data.source.local.entity.UserEntity

@Database(entities = [UserEntity::class], version = 4, exportSchema = false)
abstract class HealingKuyDatabase: RoomDatabase() {

    abstract fun getUserDao(): UserDao
}