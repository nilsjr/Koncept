package de.nilsdruyen.koncept.remote

import de.nilsdruyen.koncept.entities.Dog
import de.nilsdruyen.koncept.remote.entities.DogWebEntity

fun DogWebEntity.toModel(): Dog = Dog(id, name)