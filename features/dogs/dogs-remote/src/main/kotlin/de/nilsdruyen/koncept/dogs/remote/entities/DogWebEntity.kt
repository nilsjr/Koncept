package de.nilsdruyen.koncept.dogs.remote.entities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DogWebEntity(
    val id: Int,
    val name: String,
    val temperament: String?, // "Stubborn, Curious, Playful, Adventurous, Active, Fun-loving",
    @Json(name = "life_span")
    val lifeSpan: String?,
    @Json(name = "reference_image_id")
    val imageId: String?,
    @Json(name = "breed_group")
    val group: String?,
    val weight: MeasureWebEntity?,
    val height: MeasureWebEntity?,
    @Json(name = "bred_for")
    val bredFor: String?,
    val origin: String?,
    val description: String?,
    @Json(name = "country_code")
    val countryCode: String?,
)

@JsonClass(generateAdapter = true)
data class MeasureWebEntity(val metric: String) // "3 - 6"