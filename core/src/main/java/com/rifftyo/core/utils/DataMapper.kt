package com.rifftyo.core.utils

import com.rifftyo.core.data.source.local.entity.UserEntity
import com.rifftyo.core.data.source.remote.response.AuthResponse
import com.rifftyo.core.data.source.remote.response.DestinationItemResponse
import com.rifftyo.core.data.source.remote.response.DetailItemResponse
import com.rifftyo.core.data.source.remote.response.UserDataResponse
import com.rifftyo.core.domain.model.Auth
import com.rifftyo.core.domain.model.Destinations
import com.rifftyo.core.domain.model.User

object DataMapper {

    fun mapAuthResponseToDomain(input: AuthResponse): Auth =
        Auth(
            message = input.message,
            accessKey = input.accessKey
        )

    // User
    fun mapUserResponseToEntity(input: UserDataResponse): UserEntity =
        UserEntity(
            id = input.id,
            email = input.email,
            username = input.username,
            profile = input.profile
        )

    fun mapUserEntityToDomain(input: UserEntity): User =
        User(
            id = input.id,
            email = input.email,
            username = input.username,
            profile = input.profile
        )

    fun mapDestinationResponseToDomain(input: List<DestinationItemResponse>): List<Destinations> =
        input.map {
            Destinations(
                id = it.id,
                image = it.image,
                name = it.name,
                city = it.city,
                rating = it.rating,
                category = null,
                description = null,
                isBookmark = false
            )
        }

    fun mapDetailDestinationResponseToDomain(input: DetailItemResponse): Destinations =
        Destinations(
            id = input.id,
            image = input.image,
            name = input.name,
            city = input.city,
            rating = input.rating,
            category = input.category,
            description = input.description,
            isBookmark = input.bookmarked
        )
}