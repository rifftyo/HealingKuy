package com.rifftyo.core.data.source.remote

import android.util.Log
import com.rifftyo.core.data.source.remote.network.ApiResponse
import com.rifftyo.core.data.source.remote.network.ApiService
import com.rifftyo.core.data.source.remote.response.AuthResponse
import com.rifftyo.core.data.source.remote.response.DestinationItemResponse
import com.rifftyo.core.data.source.remote.response.DetailItemResponse
import com.rifftyo.core.data.source.remote.response.GetRateResponse
import com.rifftyo.core.data.source.remote.response.MessageResponse
import com.rifftyo.core.data.source.remote.response.RatingResponse
import com.rifftyo.core.data.source.remote.response.UserDataResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(private val apiService: ApiService) {

    fun register(
        username: RequestBody,
        email: RequestBody,
        password: RequestBody,
        profile: MultipartBody.Part
    ): Flow<ApiResponse<AuthResponse>> {
        return safeApiCall("register") {
            apiService.register(username, email, password, profile)
        }
    }

    fun login(body: RequestBody): Flow<ApiResponse<AuthResponse>> {
        return safeApiCall("login") {
            apiService.login(body)
        }
    }

    fun getUser(): Flow<ApiResponse<UserDataResponse>> {
        return safeApiCall("getUser") {
            apiService.getUser().data
        }
    }

    fun getPopularDestinations(): Flow<ApiResponse<List<DestinationItemResponse>>> {
        return safeApiCall("getPopularDestinations") {
            apiService.getPopularDestinations().data
        }
    }

    fun getDestinationsByCity(city: String): Flow<ApiResponse<List<DestinationItemResponse>>> {
        return safeApiCall("getDestinationsByCity") {
            apiService.getDestinationsByCity(city).data
        }
    }

    fun getDetailDestinations(destinationId: String): Flow<ApiResponse<DetailItemResponse>> {
        return safeApiCall("getDetailDestinations") {
            apiService.getDetailDestination(destinationId).data
        }
    }

    fun postRate(requestBody: RequestBody): Flow<ApiResponse<RatingResponse>> {
        return safeApiCall("postRate") {
            apiService.postRate(requestBody)
        }
    }

    fun getRateDestination(destinationId: String): Flow<ApiResponse<GetRateResponse>> {
        return safeApiCall("getRateDestination") {
            apiService.getRateDestination(destinationId)
        }
    }

    fun updateRate(requestBody: RequestBody): Flow<ApiResponse<RatingResponse>> {
        return safeApiCall("updateRate") {
            apiService.updateRate(requestBody)
        }
    }

    fun getDestinationBySearch(query: String): Flow<ApiResponse<List<DestinationItemResponse>>> {
        return safeApiCall("getDestinationBySearch") {
            apiService.getDestinationBySearch(query).data
        }
    }

    fun getDestinationsByCategory(category: String): Flow<ApiResponse<List<DestinationItemResponse>>> {
        return safeApiCall("getCategories") {
            apiService.getDestinationsByCategory(category).data
        }
    }

    fun getBookmarks(): Flow<ApiResponse<List<DestinationItemResponse>>> {
        return safeApiCall("getBookmarks") {
            apiService.getBookmarks().data
        }
    }

    fun addBookmark(requestBody: RequestBody): Flow<ApiResponse<MessageResponse>> {
        return safeApiCall("addBookmark") {
            apiService.addBookmark(requestBody)
        }
    }

    fun deleteBookmark(requestBody: RequestBody): Flow<ApiResponse<MessageResponse>> {
        return safeApiCall("deleteBookmark") {
            apiService.deleteBookmark(requestBody)
        }
    }

    fun updateUser(username: RequestBody, profile: MultipartBody.Part?): Flow<ApiResponse<MessageResponse>> {
        return safeApiCall("updateUser") {
            apiService.updateUser(username, profile)
        }
    }

    private fun <T> safeApiCall(
        apiName: String,
        apiCall: suspend () -> T
    ): Flow<ApiResponse<T>> {
        return flow {
            try {
                val result = apiCall()
                emit(ApiResponse.Success(result))
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorMessage = try {
                    JSONObject(errorBody ?: "").getString("message")
                } catch (jsonException: Exception) {
                    "HTTP ${e.code()} ${e.message()}"
                }
                emit(ApiResponse.Error(errorMessage))
                Log.e("RemoteDataSource", "$apiName error: $errorMessage", e)
            } catch (e: IOException) {
                emit(ApiResponse.Error("Network error, please check your connection"))
                Log.e("RemoteDataSource", "$apiName network error: ${e.message}", e)
            } catch (e: Exception) {
                emit(ApiResponse.Error("Unexpected error: ${e.localizedMessage}"))
                Log.e("RemoteDataSource", "$apiName unexpected error: ${e.message}", e)
            }
        }.flowOn(Dispatchers.IO)
    }
}