package de.nilsdruyen.koncept.dogs.remote

import arrow.core.right
import com.squareup.moshi.Moshi
import de.nilsdruyen.koncept.adapters.EitherCallAdapterFactory
import de.nilsdruyen.koncept.dogs.data.DogsRemoteDataSource
import de.nilsdruyen.koncept.dogs.entity.BreedId
import de.nilsdruyen.koncept.dogs.entity.Dog
import de.nilsdruyen.koncept.domain.DispatcherProvider
import de.nilsdruyen.koncept.test.CoroutinesTestExtension
import de.nilsdruyen.koncept.test.InjectTestDispatcherProvider
import de.nilsdruyen.koncept.test.utils.asResource
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

@ExtendWith(CoroutinesTestExtension::class)
class DogsRemoteDataSourceImpl2Test {

    @InjectTestDispatcherProvider
    lateinit var dispatcherProvider: DispatcherProvider

    lateinit var mockWebServer: MockWebServer

    lateinit var tested: DogsRemoteDataSource

    @BeforeEach
    fun setup() {
        mockWebServer = MockWebServer()

        val okHttpClient = OkHttpClient.Builder()
            .writeTimeout(1, TimeUnit.SECONDS)
            .readTimeout(1, TimeUnit.SECONDS)
            .connectTimeout(1, TimeUnit.SECONDS)
            .build()

        // Creates Moshi
        val moshi = Moshi.Builder().build()

        // Creates Retrofit
        val api = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(EitherCallAdapterFactory())
            .baseUrl(mockWebServer.url("/"))
            .callFactory { okHttpClient.newCall(it) }
            .build()
            .create(DogsApi::class.java)

        // Creates the repository
        tested = DogsRemoteDataSourceImpl(api, dispatcherProvider.io)
    }

    @AfterEach
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `GIVEN a valid response WHEN getList is called THEN list of dogs is returned`() = runTest {
        val expected = listOf(
            Dog(
                id = BreedId(value = 1),
                name = "Affenpinscher",
                isFavorite = false,
                temperament = "Stubborn, Curious, Playful, Adventurous, Active, Fun-loving".split(", "),
                lifeSpan = IntRange.EMPTY,
                weight = 3..6,
                height = 23..29,
                bredFor = "Small rodent hunting, lapdog",
                origin = listOf("Germany", "France"),
                group = "Toy"
            ),
            Dog(
                id = BreedId(value = 2),
                name = "Afghan Hound",
                isFavorite = false,
                temperament = "Aloof, Clownish, Dignified, Independent, Happy".split(", "),
                lifeSpan = IntRange.EMPTY,
                weight = 23..27,
                height = 64..69,
                bredFor = "Coursing and hunting",
                origin = "Afghanistan, Iran, Pakistan".split(", "),
                group = "Hound"
            ),
        )

        mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody("/dog-list-2.json".asResource()))

        val result = tested.getList()

        assertThat(result).isEqualTo(expected.right())
    }
}
