package com.rifftyo.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class UserResponse(

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val data: UserDataResponse
)

data class UserDataResponse(

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("username")
    val username: String,

    @field:SerializedName("fotoprofile")
    val profile: String
)