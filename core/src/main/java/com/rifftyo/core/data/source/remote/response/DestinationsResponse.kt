package com.rifftyo.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class DestinationsResponse(

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val data: List<DestinationItemResponse>
)

data class DestinationItemResponse(

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("image")
    val image: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("city")
    val city: String,

    @field:SerializedName("rating")
    val rating: Double,
)