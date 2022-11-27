package com.myapplication.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User(
    val token: String
)