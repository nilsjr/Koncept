package de.nilsdruyen.koncept.dogs.entity

data class Dog(
    val id: Int,
    val name: String,
    val isFavorite: Boolean = false,
    val temperament: List<String> = emptyList(),
    val lifeSpan: IntRange = IntRange.EMPTY,
    val weight: IntRange = IntRange.EMPTY,
    val height: IntRange = IntRange.EMPTY,
    val bredFor: String = "",
    val origin: List<String> = emptyList(),
    val group: String = "",
)