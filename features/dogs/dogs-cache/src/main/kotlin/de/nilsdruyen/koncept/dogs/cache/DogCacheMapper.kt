package de.nilsdruyen.koncept.dogs.cache

import de.nilsdruyen.koncept.dogs.cache.entities.BreedCacheEntity
import de.nilsdruyen.koncept.dogs.cache.entities.MinimalDogCacheEntity
import de.nilsdruyen.koncept.dogs.entity.Breed
import de.nilsdruyen.koncept.dogs.entity.BreedId

fun BreedCacheEntity.toModel() = Breed(
    id = BreedId(id),
    name = name,
    isFavorite = isFavorite,
    temperament = temperament,
    lifeSpan = lifeSpan,
    weight = weight,
    height = height,
    bredFor = bredFor,
    origin = origin,
    group = group,
    imageUrl = imageUrl,
)

fun Breed.toEntity() = BreedCacheEntity(
    id = id.value,
    name = name,
    isFavorite = isFavorite,
    temperament = temperament,
    lifeSpan = lifeSpan,
    weight = weight,
    height = height,
    bredFor = bredFor,
    origin = origin,
    group = group,
    imageUrl = imageUrl,
)

fun Breed.toMinimalEntity() = MinimalDogCacheEntity(
    id = id.value,
    name = name,
)
