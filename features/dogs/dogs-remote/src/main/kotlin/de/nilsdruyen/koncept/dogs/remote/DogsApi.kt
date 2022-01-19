package de.nilsdruyen.koncept.dogs.remote

import arrow.core.Either
import de.nilsdruyen.koncept.dogs.remote.entities.BreedImageWebEntity
import de.nilsdruyen.koncept.dogs.remote.entities.DogWebEntity
import de.nilsdruyen.koncept.domain.DataSourceError
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DogsApi {

    @GET("breeds")
    suspend fun getBreeds(): Either<DataSourceError, List<DogWebEntity>>

    @GET("breeds/search")
    suspend fun searchBreed(@Query("q") input: String): Either<DataSourceError, List<DogWebEntity>>

    @GET("images/search")
    suspend fun searchImages(
        @Query("limit") limit: Int,
        @Query("page") page: Int,
        @Query("order") order: String,
    ): Either<DataSourceError, List<BreedImageWebEntity>>

    @GET("images/search")
    suspend fun searchImagesForBreed(
        @Query("limit") limit: Int,
//        @Query("page") page: Int,
//        @Query("order") order: String,
        @Query("breed_id") breedId: Int,
//        @Query("size") size: String,
    ): Either<DataSourceError, List<BreedImageWebEntity>>

    @GET("images/{id}")
    suspend fun getImage(@Path("id") id: String): Either<DataSourceError, BreedImageWebEntity>
}