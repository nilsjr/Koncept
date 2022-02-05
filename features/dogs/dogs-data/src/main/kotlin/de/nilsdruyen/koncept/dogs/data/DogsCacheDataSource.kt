package de.nilsdruyen.koncept.dogs.data

import androidx.paging.PagingSource
import arrow.core.Either
import de.nilsdruyen.koncept.dogs.entity.Breed
import de.nilsdruyen.koncept.dogs.entity.BreedImage
import de.nilsdruyen.koncept.dogs.entity.Dog
import de.nilsdruyen.koncept.domain.DataSourceError
import kotlinx.coroutines.flow.Flow

interface DogsCacheDataSource {
    suspend fun getDogList(): Flow<Either<DataSourceError, List<Dog>>>

    suspend fun setDogList(list: List<Dog>)

    fun getBreedPaging(): PagingSource<Int, Breed>
}