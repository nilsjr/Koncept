package de.nilsdruyen.koncept.dogs.remote.entities

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BreedImageWebEntity(
    val id: String,
    val url: String,
    val width: Int,
    val height: Int,
    val breeds: List<DogWebEntity>,
)