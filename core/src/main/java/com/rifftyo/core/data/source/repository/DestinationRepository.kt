package com.rifftyo.core.data.source.repository

import com.rifftyo.core.data.Resource
import com.rifftyo.core.data.source.remote.RemoteDataSource
import com.rifftyo.core.data.source.remote.network.ApiResponse
import com.rifftyo.core.domain.model.Destinations
import com.rifftyo.core.domain.repository.IDestinationRepository
import com.rifftyo.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DestinationRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : IDestinationRepository {

    override fun getPopularDestinations(): Flow<Resource<List<Destinations>>> = flow {
        emit(Resource.Loading())
        when (val response = remoteDataSource.getPopularDestinations().first()) {
            is ApiResponse.Success -> {
                val domain = DataMapper.mapDestinationResponseToDomain(response.data)
                emit(Resource.Success(domain))
            }
            is ApiResponse.Empty -> emit(Resource.Success(emptyList()))
            is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
        }
    }

    override fun getDestinationsByCity(city: String): Flow<Resource<List<Destinations>>> = flow {
        emit(Resource.Loading())
        when (val response = remoteDataSource.getDestinationsByCity(city).first()) {
            is ApiResponse.Success -> {
                val domain = DataMapper.mapDestinationResponseToDomain(response.data)
                emit(Resource.Success(domain))
            }
            is ApiResponse.Empty -> emit(Resource.Success(emptyList()))
            is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
        }
    }

    override fun getDetailDestination(id: String): Flow<Resource<Destinations>> = flow {
        emit(Resource.Loading())
        when (val response = remoteDataSource.getDetailDestinations(id).first()) {
            is ApiResponse.Success -> {
                val domain = DataMapper.mapDetailDestinationResponseToDomain(response.data)
                emit(Resource.Success(domain))
            }
            is ApiResponse.Empty -> emit(Resource.Error("Data detail kosong"))
            is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
        }
    }

    override fun getRateDestination(destinationId: String): Flow<Resource<Double>> = flow {
        emit(Resource.Loading())
        when (val response = remoteDataSource.getRateDestination(destinationId).first()) {
            is ApiResponse.Success -> {
                val rating = response.data.rating ?: 0.0
                emit(Resource.Success(rating))
            }
            is ApiResponse.Empty -> emit(Resource.Error("Data detail kosong"))
            is ApiResponse.Error -> {
                if (response.errorMessage.contains("No rating found", ignoreCase = true)) {
                    emit(Resource.Success(0.0))
                } else {
                    emit(Resource.Error(response.errorMessage))
                }
            }
        }
    }

    override fun postRate(destinationId: String, rating: Double): Flow<Resource<Double>> = flow {
        emit(Resource.Loading())

        val jsonObject = JSONObject().apply {
            put("destinationId", destinationId)
            put("rating", rating)
        }
        val requestBody = jsonObject.toString().toRequestBody("application/json".toMediaType())

        when(val response = remoteDataSource.postRate(requestBody).first()) {
            is ApiResponse.Success -> {
                val rating = response.data.rating
                emit(Resource.Success(rating))
            }
            is ApiResponse.Empty -> emit(Resource.Error("Data detail kosong"))
            is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
        }
    }

    override fun updateRate(destinationId: String, rating: Double): Flow<Resource<Double>> = flow {
        emit(Resource.Loading())

        val jsonObject = JSONObject().apply {
            put("destinationId", destinationId)
            put("rating", rating)
        }
        val requestBody = jsonObject.toString().toRequestBody("application/json".toMediaType())

        when(val response = remoteDataSource.updateRate(requestBody).first()) {
            is ApiResponse.Success -> {
                val rating = response.data.rating
                emit(Resource.Success(rating))
            }
            is ApiResponse.Empty -> emit(Resource.Error("Data detail kosong"))
            is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
        }
    }

    override fun getDestinationBySearch(query: String): Flow<Resource<List<Destinations>>> = flow {
        emit(Resource.Loading())
        when (val response = remoteDataSource.getDestinationBySearch(query).first()) {
            is ApiResponse.Success -> {
                val domain = DataMapper.mapDestinationResponseToDomain(response.data)
                emit(Resource.Success(domain))
            }
            is ApiResponse.Empty -> emit(Resource.Success(emptyList()))
            is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
        }
    }

    override fun getDestinationsByCategory(category: String): Flow<Resource<List<Destinations>>> = flow {
        emit(Resource.Loading())
        when (val response = remoteDataSource.getDestinationsByCategory(category).first()) {
            is ApiResponse.Success -> {
                val domain = DataMapper.mapDestinationResponseToDomain(response.data)
                emit(Resource.Success(domain))
            }
            is ApiResponse.Empty -> emit(Resource.Success(emptyList()))
            is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
        }
    }

    override fun getBookmarks(): Flow<Resource<List<Destinations>>> = flow {
        emit(Resource.Loading())
        when (val response = remoteDataSource.getBookmarks().first()) {
            is ApiResponse.Success -> {
                val domain = DataMapper.mapDestinationResponseToDomain(response.data)
                emit(Resource.Success(domain))
            }
            is ApiResponse.Empty -> emit(Resource.Success(emptyList()))
            is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
        }
    }

    override fun addBookmark(destinationId: String): Flow<Resource<String>> = flow {
        emit(Resource.Loading())

        val jsonObject = JSONObject().apply {
            put("destinationId", destinationId)
        }
        val requestBody = jsonObject.toString().toRequestBody("application/json".toMediaType())

        when (val response = remoteDataSource.addBookmark(requestBody).first()) {
            is ApiResponse.Success -> {
                val message = response.data.message
                emit(Resource.Success(message))
            }
            is ApiResponse.Empty -> {
                emit(Resource.Error("Data detail kosong"))
            }
            is ApiResponse.Error -> {
                emit(Resource.Error(response.errorMessage))
            }
        }
    }

    override fun deleteBookmark(destinationId: String): Flow<Resource<String>> = flow {
        emit(Resource.Loading())

        val jsonObject = JSONObject().apply {
            put("destinationId", destinationId)
        }
        val requestBody = jsonObject.toString().toRequestBody("application/json".toMediaType())

        when (val response = remoteDataSource.deleteBookmark(requestBody).first()) {
            is ApiResponse.Success -> {
                val message = response.data.message
                emit(Resource.Success(message))
            }
            is ApiResponse.Empty -> {
                emit(Resource.Error("Data detail kosong"))
            }
            is ApiResponse.Error -> {
                emit(Resource.Error(response.errorMessage))
            }
        }
    }
}