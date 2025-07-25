package com.rifftyo.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class DetailResponse(

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val data: DetailItemResponse
)

data class DetailItemResponse(

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("image")
    val image: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("city")
    val city: String,

    @field:SerializedName("category")
    val category: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("rating")
    val rating: Double,

    @field:SerializedName("bookmarked")
    val bookmarked: Boolean
)