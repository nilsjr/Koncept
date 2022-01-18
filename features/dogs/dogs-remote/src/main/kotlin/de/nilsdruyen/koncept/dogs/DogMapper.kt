package de.nilsdruyen.koncept.dogs

import de.nilsdruyen.koncept.dogs.entities.DogWebEntity

fun DogWebEntity.toModel(): Dog = Dog(id, name)