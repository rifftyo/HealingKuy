package com.rifftyo.core.data.source.local

import com.rifftyo.core.data.source.local.entity.UserEntity
import com.rifftyo.core.data.source.local.room.UserDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSource @Inject constructor(private val userDao: UserDao) {

    // Info User
    fun getUser() = userDao.getUser()
    suspend fun deleteUser() = userDao.deleteUser()
    suspend fun insertUser(user: UserEntity) = userDao.insertUser(user)
}