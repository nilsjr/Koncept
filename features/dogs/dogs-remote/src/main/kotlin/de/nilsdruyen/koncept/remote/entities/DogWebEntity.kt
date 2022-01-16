package de.nilsdruyen.koncept.remote.entities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DogWebEntity(
    val id: String,
    val name: String,
    val temperament: String?,
    @Json(name = "life_span")
    val lifeSpan: String?,
    @Json(name = "reference_image_id")
    val imageId: String?,
)