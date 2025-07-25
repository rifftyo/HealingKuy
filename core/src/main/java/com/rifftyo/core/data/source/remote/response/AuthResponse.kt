package com.rifftyo.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class AuthResponse(

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("accessKey")
    val accessKey: String
)