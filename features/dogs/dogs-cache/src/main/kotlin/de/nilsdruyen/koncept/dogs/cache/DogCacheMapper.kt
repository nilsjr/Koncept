package de.nilsdruyen.koncept.dogs.cache

import de.nilsdruyen.koncept.dogs.cache.entities.DogCacheEntity
import de.nilsdruyen.koncept.dogs.cache.entities.MinimalDogCacheEntity
import de.nilsdruyen.koncept.dogs.entity.Dog

fun DogCacheEntity.toModel() = Dog(
    id = id,
    name = name,
    isFavorite = isFavorite,
    temperament = temperament,
    lifeSpan = lifeSpan,
    weight = weight,
    height = height,
    bredFor = bredFor,
    origin = origin,
    group = group
)

fun Dog.toEntity() = DogCacheEntity(
    id = id,
    name = name,
    isFavorite = isFavorite,
    temperament = temperament,
    lifeSpan = lifeSpan,
    weight = weight,
    height = height,
    bredFor = bredFor,
    origin = origin,
    group = group
)

fun Dog.toMinimalEntity() = MinimalDogCacheEntity(
    id = id,
    name = name,
)