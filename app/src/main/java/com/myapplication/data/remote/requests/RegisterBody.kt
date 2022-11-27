package com.myapplication.data.remote.requests

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RegisterBody(val email: String, val password: String, val age: String)