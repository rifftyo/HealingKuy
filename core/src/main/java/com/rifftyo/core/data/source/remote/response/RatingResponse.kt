package com.rifftyo.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class RatingResponse(

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("newAverageRating")
    val rating: Double
)
