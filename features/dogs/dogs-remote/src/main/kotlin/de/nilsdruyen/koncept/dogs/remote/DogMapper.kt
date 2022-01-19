package de.nilsdruyen.koncept.dogs.remote

import de.nilsdruyen.koncept.dogs.remote.entities.DogWebEntity
import de.nilsdruyen.koncept.dogs.entities.Dog

fun DogWebEntity.toModel(): Dog = Dog(id, name)