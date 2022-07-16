package de.nilsdruyen.koncept.dogs.cache

import de.nilsdruyen.koncept.dogs.cache.entities.DogCacheEntity
import de.nilsdruyen.koncept.dogs.cache.entities.MinimalDogCacheEntity
import de.nilsdruyen.koncept.dogs.entity.Dog

fun DogCacheEntity.toModel() = Dog(
    id = id,
    name = name,
    isFavorite = isFavorite
)

fun Dog.toEntity() = DogCacheEntity(
    id = id,
    name = name,
    isFavorite = isFavorite
)

fun Dog.toMinimalEntity() = MinimalDogCacheEntity(
    id = id,
    name = name,
)