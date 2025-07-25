package com.rifftyo.core.data.source.repository

import com.rifftyo.core.data.NetworkBoundResource
import com.rifftyo.core.data.Resource
import com.rifftyo.core.data.source.local.LocalDataSource
import com.rifftyo.core.data.source.remote.RemoteDataSource
import com.rifftyo.core.data.source.remote.network.ApiResponse
import com.rifftyo.core.data.source.remote.response.UserDataResponse
import com.rifftyo.core.domain.model.Auth
import com.rifftyo.core.domain.model.User
import com.rifftyo.core.domain.repository.IUserRepository
import com.rifftyo.core.utils.DataMapper
import com.rifftyo.core.utils.TokenManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val tokenManager: TokenManager
) : IUserRepository {

    override fun register(
        username: String,
        email: String,
        password: String,
        profileName: String,
        profileBytes: ByteArray
    ): Flow<Resource<Auth>> {
        return flow {
            emit(Resource.Loading())

            val usernameBody = username.toRequestBody("text/plain".toMediaType())
            val emailBody = email.toRequestBody("text/plain".toMediaType())
            val passwordBody = password.toRequestBody("text/plain".toMediaType())

            val profileRequestBody = profileBytes.toRequestBody("image/*".toMediaTypeOrNull())
            val profilePart =
                MultipartBody.Part.createFormData("fotoprofile", profileName, profileRequestBody)

            when (val response = remoteDataSource.register(
                usernameBody,
                emailBody,
                passwordBody,
                profilePart
            ).first()) {
                is ApiResponse.Success -> {
                    val auth = DataMapper.mapAuthResponseToDomain(response.data)
                    response.data.accessKey.let { token ->
                        tokenManager.saveToken(token)
                    }
                    emit(Resource.Success(auth))
                }

                is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
                is ApiResponse.Empty -> emit(Resource.Error("Empty Data"))
            }
        }
    }

    override fun login(emailOrUsername: String, password: String): Flow<Resource<Auth>> {
        return flow {
            emit(Resource.Loading())

            val jsonObject = JSONObject().apply {
                put("emailOrUsername", emailOrUsername)
                put("password", password)
            }
            val requestBody = jsonObject.toString().toRequestBody("application/json".toMediaType())

            when (val response = remoteDataSource.login(requestBody).first()) {
                is ApiResponse.Success -> {
                    val auth = DataMapper.mapAuthResponseToDomain(response.data)
                    response.data.accessKey.let { token ->
                        tokenManager.saveToken(token)
                    }
                    emit(Resource.Success(auth))
                }

                is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
                is ApiResponse.Empty -> emit(Resource.Error("Empty Data"))
            }
        }
    }

    override fun getUser(): Flow<Resource<User?>> =
        object :
            NetworkBoundResource<User?, UserDataResponse>() {
            override fun loadFromDB(): Flow<User?> =
                localDataSource.getUser().map { entity ->
                    entity?.let { DataMapper.mapUserEntityToDomain(it) }
                }

            override fun shouldFetch(data: User?): Boolean = data == null

            override suspend fun createCall(): Flow<ApiResponse<UserDataResponse>> =
                remoteDataSource.getUser()

            override suspend fun saveCallResult(data: UserDataResponse) {
                val entities = DataMapper.mapUserResponseToEntity(data)
                localDataSource.insertUser(entities)
            }
        }.asFlow()

    override fun updateUser(username: String, profileName: String?, profileBytes: ByteArray?): Flow<Resource<String>> = flow {
        emit(Resource.Loading())

        val usernameBody = username.toRequestBody("text/plain".toMediaType())
        val profileRequestBody = profileBytes?.toRequestBody("image/*".toMediaTypeOrNull())
        val profilePart = if (profileBytes != null && profileName != null) {
            MultipartBody.Part.createFormData("fotoprofile", profileName, profileRequestBody!!)
        } else {
            null
        }

        when (val response = remoteDataSource.updateUser(usernameBody, profilePart).first()) {
            is ApiResponse.Success -> {
                val userResponse = remoteDataSource.getUser().first()
                if (userResponse is ApiResponse.Success) {
                    val userEntity = DataMapper.mapUserResponseToEntity(userResponse.data)
                    localDataSource.insertUser(userEntity)
                }

                emit(Resource.Success("Success"))
            }
            is ApiResponse.Error -> {
                val message = if (response.errorMessage.contains("Username is already taken", ignoreCase = true)) {
                    "Username sudah digunakan"
                } else {
                    response.errorMessage
                }
                emit(Resource.Error(message))
            }
            is ApiResponse.Empty -> emit(Resource.Error("Empty Data"))
        }
    }

    override suspend fun deleteUser() {
        localDataSource.deleteUser()
    }
}