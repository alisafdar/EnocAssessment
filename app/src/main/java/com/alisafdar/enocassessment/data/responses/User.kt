package com.alisafdar.enocassessment.data.responses

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey val id: Int,
    val email: String,
    val name: String,
    val access_token: String? = "",
    val refresh_token: String? = "",
)