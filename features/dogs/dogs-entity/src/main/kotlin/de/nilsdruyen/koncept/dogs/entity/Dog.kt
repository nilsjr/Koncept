package de.nilsdruyen.koncept.dogs.entity

data class Dog(
    val id: Int,
    val name: String,
    val isFavorite: Boolean = false,
)