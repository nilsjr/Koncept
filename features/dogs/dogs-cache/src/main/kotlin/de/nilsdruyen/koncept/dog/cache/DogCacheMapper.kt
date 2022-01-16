package de.nilsdruyen.koncept.dog.cache

import de.nilsdruyen.koncept.dog.cache.entities.DogCacheEntity
import de.nilsdruyen.koncept.domain.Dog

fun DogCacheEntity.toModel() = Dog(id, name)

fun Dog.toEntity() = DogCacheEntity(id, name)