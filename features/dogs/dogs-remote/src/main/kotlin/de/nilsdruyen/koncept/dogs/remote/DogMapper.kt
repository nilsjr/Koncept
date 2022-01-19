package de.nilsdruyen.koncept.dogs.remote

import de.nilsdruyen.koncept.dogs.entity.BreedImage
import de.nilsdruyen.koncept.dogs.entity.Dog
import de.nilsdruyen.koncept.dogs.remote.entities.BreedImageWebEntity
import de.nilsdruyen.koncept.dogs.remote.entities.DogWebEntity

fun DogWebEntity.toModel(): Dog = Dog(id, name)

fun BreedImageWebEntity.toModel(): BreedImage {
    val breed = breeds.first()
    return BreedImage(id, url, breed.id, breed.name)
}