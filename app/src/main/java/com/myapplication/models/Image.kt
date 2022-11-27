package com.myapplication.models

import android.os.Parcelable
import com.myapplication.domain.base.Unique
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Image(
    val id: String,
    val imageSize: String,
    val type: String,
    val tags: String,
    val user: String,
    val largeImageURL: String,
    val previewURL : String,
    val views: String,
    val likes: String,
    val comments: String,
    val downloads: String,
) : Unique, Parcelable {
    override val uniqueId: Int
        get() = id.hashCode()
}