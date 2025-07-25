package com.rifftyo.core.domain.repository

import com.rifftyo.core.data.Resource
import com.rifftyo.core.domain.model.Auth
import com.rifftyo.core.domain.model.User
import kotlinx.coroutines.flow.Flow

interface IUserRepository {
    fun register(username: String, email: String, password: String, profileName: String, profileBytes: ByteArray): Flow<Resource<Auth>>

    fun login(emailOrUsername: String, password: String): Flow<Resource<Auth>>

    fun getUser(): Flow<Resource<User?>>

    fun updateUser(username: String, profileName: String?, profileBytes: ByteArray?): Flow<Resource<String>>

    suspend fun deleteUser()
}