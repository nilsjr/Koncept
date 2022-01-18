package de.nilsdruyen.koncept.dogs

import de.nilsdruyen.koncept.dogs.entities.DogCacheEntity

fun DogCacheEntity.toModel() = Dog(id, name)

fun Dog.toEntity() = DogCacheEntity(id, name)