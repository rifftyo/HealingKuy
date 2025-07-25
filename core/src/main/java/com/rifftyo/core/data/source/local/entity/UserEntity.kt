package com.rifftyo.core.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey
    val id: String,

    @ColumnInfo("email")
    val email: String,

    @ColumnInfo("username")
    val username: String,

    @ColumnInfo("profile")
    val profile: String
)