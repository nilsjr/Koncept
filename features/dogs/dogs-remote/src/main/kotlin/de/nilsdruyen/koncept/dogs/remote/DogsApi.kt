package de.nilsdruyen.koncept.dogs.remote

import arrow.core.Either
import de.nilsdruyen.koncept.dogs.remote.entities.DogWebEntity
import de.nilsdruyen.koncept.domain.DataSourceError
import retrofit2.http.GET

interface DogsApi {

    @GET("breeds")
    suspend fun getBreeds(): Either<DataSourceError, List<DogWebEntity>>

    @GET("breeds/search")
    suspend fun searchBreed(): Either<DataSourceError, List<DogWebEntity>>
}