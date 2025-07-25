package com.rifftyo.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Destinations(
    val id: String,
    val image: String,
    val name: String,
    val city: String,
    val rating: Double,
    val category: String?,
    val description: String?,
    var isBookmark: Boolean
) : Parcelable
