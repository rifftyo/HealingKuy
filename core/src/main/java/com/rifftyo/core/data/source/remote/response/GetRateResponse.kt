package com.rifftyo.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class GetRateResponse(
    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("rating")
    val rating: Double?
)
