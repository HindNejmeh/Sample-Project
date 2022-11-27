package com.myapplication.data.remote.requests

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginBody(val email: String, val password: String)