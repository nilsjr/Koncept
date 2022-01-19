package de.nilsdruyen.koncept.dogs.cache

import de.nilsdruyen.koncept.dogs.cache.entities.DogCacheEntity
import de.nilsdruyen.koncept.dogs.entities.Dog

fun DogCacheEntity.toModel() = Dog(id, name)

fun Dog.toEntity() = DogCacheEntity(id, name)