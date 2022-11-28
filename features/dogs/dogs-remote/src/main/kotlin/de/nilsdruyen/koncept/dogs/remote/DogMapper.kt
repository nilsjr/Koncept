package de.nilsdruyen.koncept.dogs.remote

import de.nilsdruyen.koncept.dogs.entity.BreedId
import de.nilsdruyen.koncept.dogs.entity.BreedImage
import de.nilsdruyen.koncept.dogs.entity.Dog
import de.nilsdruyen.koncept.dogs.remote.entities.BreedImageWebEntity
import de.nilsdruyen.koncept.dogs.remote.entities.DogWebEntity
import de.nilsdruyen.koncept.dogs.remote.entities.MeasureWebEntity

fun DogWebEntity.toModel(): Dog {
    return Dog(
        id = BreedId(id),
        name = name,
        isFavorite = false,
        temperament = temperament?.split(",")?.map { it.trim() }.orEmpty(),
        lifeSpan = IntRange.EMPTY,
        weight = weight?.parseMeasureValue() ?: IntRange.EMPTY,
        height = height?.parseMeasureValue() ?: IntRange.EMPTY,
        bredFor = bredFor.orEmpty(),
        origin = origin?.split(",")?.map { it.trim() }.orEmpty(),
        group = group.orEmpty(),
    )
}

fun BreedImageWebEntity.toModel(): BreedImage {
    val breed = breeds.first()
    return BreedImage(id, url, breed.id, breed.name)
}

internal fun MeasureWebEntity.parseMeasureValue(): IntRange {
    val list = metric.split("-").map { it.trim() }.mapNotNull { it.toIntOrNull() }
    if (list.isEmpty()) return IntRange.EMPTY
    val max = list.max()
    val min = list.min()
    return min..max
}