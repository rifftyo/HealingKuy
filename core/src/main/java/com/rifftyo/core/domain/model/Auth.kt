package com.rifftyo.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Auth(
    val message: String,
    val accessKey: String
) : Parcelable
