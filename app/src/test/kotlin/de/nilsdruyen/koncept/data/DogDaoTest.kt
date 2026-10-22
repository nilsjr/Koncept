package de.nilsdruyen.koncept.data

import android.content.Context
import android.os.Build
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.squareup.moshi.Moshi
import de.nilsdruyen.koncept.data.converters.IntRangeConverter
import de.nilsdruyen.koncept.data.converters.StringListConverter
import de.nilsdruyen.koncept.dogs.cache.DogsCacheDataSourceImpl
import de.nilsdruyen.koncept.dogs.cache.daos.DogDao
import de.nilsdruyen.koncept.dogs.cache.entities.BreedCacheEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(sdk = [Build.VERSION_CODES.TIRAMISU])
@RunWith(RobolectricTestRunner::class)
class DogDaoTest {

    private lateinit var database: KonceptDatabase
    private lateinit var dogDao: DogDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, KonceptDatabase::class.java)
            .addTypeConverter(IntRangeConverter())
            .addTypeConverter(StringListConverter(Moshi.Builder().build()))
            .allowMainThreadQueries()
            .build()
        dogDao = database.dogDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `GIVEN a favorite breed WHEN the list is refreshed THEN the favorite is kept`() = runBlocking {
        dogDao.upsertList(listOf(breed(1, "Akita"), breed(2, "Beagle")))
        dogDao.setFavorite(1)

        dogDao.upsertList(listOf(breed(1, "Akita Inu"), breed(2, "Beagle"), breed(3, "Collie")))

        val breeds = dogDao.getAll().first().sortedBy { it.id }
        assertEquals(listOf("Akita Inu", "Beagle", "Collie"), breeds.map { it.name })
        assertEquals(listOf(true, false, false), breeds.map { it.isFavorite })
    }

    @Test
    fun `GIVEN an unreadable row WHEN the dog list is read THEN an error is emitted`() = runBlocking {
        val dataSource = DogsCacheDataSourceImpl(Dispatchers.Unconfined, dogDao)
        database.openHelper.writableDatabase.execSQL(
            "INSERT INTO dog_table (id, name, lifeSpan) VALUES (1, 'Akita', 'not-a-range')",
        )

        val result = dataSource.getDogList().first()

        assertTrue(result.isLeft())
    }

    private fun breed(id: Int, name: String) = BreedCacheEntity(
        id = id,
        name = name,
        isFavorite = false,
        lifeSpan = IntRange.EMPTY,
        weight = IntRange.EMPTY,
        height = IntRange.EMPTY,
        temperament = emptyList(),
        origin = emptyList(),
        bredFor = "",
        group = "",
        imageUrl = null,
    )
}
