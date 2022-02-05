package de.nilsdruyen.koncept.dogs.entity

data class Breed(
    val id: Int,
    val name: String,
    val countryCode: String,
    val breedGroup: String,
    val imageId: String,
)

data class PagedResponse<T>(
    val pageInfo: PageInfo,
    val results: List<T> = listOf()
)

data class PageInfo(
    val count: Int,
    val page: Int,
    val pages: Int,
)