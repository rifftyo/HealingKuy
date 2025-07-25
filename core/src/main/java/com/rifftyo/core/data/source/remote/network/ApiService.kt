package com.rifftyo.core.data.source.remote.network

import com.rifftyo.core.data.source.remote.response.AuthResponse
import com.rifftyo.core.data.source.remote.response.DestinationsResponse
import com.rifftyo.core.data.source.remote.response.DetailResponse
import com.rifftyo.core.data.source.remote.response.GetRateResponse
import com.rifftyo.core.data.source.remote.response.MessageResponse
import com.rifftyo.core.data.source.remote.response.RatingResponse
import com.rifftyo.core.data.source.remote.response.UserResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @Multipart
    @POST("api/auth/register")
    suspend fun register(
        @Part("username") username: RequestBody,
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody,
        @Part fotoprofile: MultipartBody.Part
    ): AuthResponse

    @POST("api/auth/login")
    suspend fun login(
        @Body requestBody: RequestBody
    ): AuthResponse

    @GET("api/user/profile")
    suspend fun getUser(): UserResponse

    @GET("api/destinations/popular")
    suspend fun getPopularDestinations(): DestinationsResponse

    @GET("api/destinations/city")
    suspend fun getDestinationsByCity(
        @Query("name") city: String
    ): DestinationsResponse

    @GET("api/destinations/category")
    suspend fun getDestinationsByCategory(
        @Query("name") category: String
    ): DestinationsResponse

    @GET("api/destinations/{destinationId}")
    suspend fun getDetailDestination(
        @Path("destinationId") destinationId: String
    ) : DetailResponse

    @POST("api/destinations/rate")
    suspend fun postRate(
        @Body requestBody: RequestBody
    ) : RatingResponse

    @GET("api/destinations/rate/{destinationId}")
    suspend fun getRateDestination(
        @Path("destinationId") destinationId: String
    ) : GetRateResponse

    @PUT("api/destinations/rate")
    suspend fun updateRate(
        @Body requestBody: RequestBody
    ) : RatingResponse

    @GET("api/destinations/search")
    suspend fun getDestinationBySearch(
        @Query("name") query: String
    ) : DestinationsResponse

    @GET("api/bookmarks")
    suspend fun getBookmarks() : DestinationsResponse

    @POST("api/bookmarks")
    suspend fun addBookmark(
        @Body requestBody: RequestBody
    ): MessageResponse

    @HTTP(method = "DELETE", path = "api/bookmarks", hasBody = true)
    suspend fun deleteBookmark(
        @Body requestBody: RequestBody
    ): MessageResponse

    @Multipart
    @PUT("api/user/profile")
    suspend fun updateUser(
        @Part("username") username: RequestBody,
        @Part fotoprofile: MultipartBody.Part?
    ): MessageResponse
}