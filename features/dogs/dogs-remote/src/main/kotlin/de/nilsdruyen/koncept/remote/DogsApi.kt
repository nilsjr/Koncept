package de.nilsdruyen.koncept.remote

import arrow.core.Either
import de.nilsdruyen.koncept.domain.DataSourceError
import de.nilsdruyen.koncept.remote.entities.DogWebEntity
import retrofit2.http.GET

interface DogsApi {

    @GET("breeds")
    suspend fun getBreeds(): Either<DataSourceError, List<DogWebEntity>>

    @GET("breeds/search")
    suspend fun searchBreed(): Either<DataSourceError, List<DogWebEntity>>
}