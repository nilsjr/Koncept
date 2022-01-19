package de.nilsdruyen.koncept.dogs.remote

import de.nilsdruyen.koncept.dogs.entity.Dog
import de.nilsdruyen.koncept.dogs.remote.entities.DogWebEntity

fun DogWebEntity.toModel(): Dog = Dog(id, name)