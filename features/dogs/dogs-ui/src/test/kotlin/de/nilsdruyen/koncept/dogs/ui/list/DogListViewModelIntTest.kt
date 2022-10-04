@file:Suppress("CommentSpacing")

package de.nilsdruyen.koncept.dogs.ui.list

//@UninstallModules(DogsRemoteStaticModule::class)
//@HiltAndroidTest
//@RunWith(RobolectricTestRunner::class)
//@Config(sdk = [28], application = HiltTestApplication::class)
//class DogListViewModelIntTest {
//
//    @get:Rule(order = 1)
//    var hiltRule = HiltAndroidRule(this)
//
////    @get:Rule(order = 2)
////    val mockRule = MockitoJUnit.rule()
//
//    @Inject
//    lateinit var dispatcher: TestDispatcher
//
//    @Inject
//    lateinit var getDogListUseCase: GetDogListUseCase
//
//    private lateinit var viewModel: DogListViewModel
//
//    private val scope = lazy { TestScope(dispatcher) }
//
//    @Before
//    fun init() {
//        hiltRule.inject()
//        Dispatchers.setMain(dispatcher)
//        viewModel = DogListViewModel(dispatcher, getDogListUseCase)
//    }
//
//    @After
//    fun cleanup() {
//        Dispatchers.resetMain()
//    }
//
//    @Test
//    fun `load dogs`() = scope.value.runTest {
////        val dogEntityList = "/json/dog-list.json".parseList(DogWebEntity::class.java).take(25)
////        val dogWebEntityList = Either.Right(dogEntityList)
////        val dogCacheEntityList = listOf(DogCacheEntity(1, "Dog 1", false))
//
////        whenever(dogsApi.getBreeds()) doReturn dogWebEntityList
////        whenever(dogDao.getAll()) doReturn flowOf(dogCacheEntityList)
////        whenever(dogDao.addList(any())) doReturn emptyList()
//
//        viewModel.state.test {
//            assert(awaitItem().list.isEmpty())
//            assert(awaitItem().list.size == 1) // cache
//            assert(awaitItem().list.size == 25) // web
//            cancelAndIgnoreRemainingEvents()
//        }
//    }
//}
//
//@Module
//@TestInstallIn(
//    components = [SingletonComponent::class],
//    replaces = [DispatchersModule::class]
//)
//object TestDispatchersModule {
//
//    @Provides
//    @Singleton
//    fun provideTestDispatcher(): TestDispatcher = StandardTestDispatcher(TestCoroutineScheduler())
//
//    @Provides
//    @IoDispatcher
//    fun providesIoDispatcher(dispatcher: TestDispatcher): CoroutineDispatcher = dispatcher
//
//    @Provides
//    @MainDispatcher
//    fun providesMainDispatcher(dispatcher: TestDispatcher): CoroutineDispatcher = dispatcher
//
//    @Provides
//    @DefaultDispatcher
//    fun providesDefaultDispatcher(dispatcher: TestDispatcher): CoroutineDispatcher = dispatcher
//}
//
//@Module
//@InstallIn(SingletonComponent::class)
//object TestCacheModule {
//
//    @Provides
//    fun provideoDao(): DogDao = object : DogDao {
//
//        override fun getAll(): Flow<List<DogCacheEntity>> {
//            val dogCacheEntityList = listOf(DogCacheEntity(1, "Dog 1", false))
//            return flowOf(dogCacheEntityList)
//        }
//
//        override fun getDogById(id: Int): Flow<DogCacheEntity> {
//            TODO("Not yet implemented")
//        }
//
//        override suspend fun addList(list: List<DogCacheEntity>): List<Long> {
//            TODO("Not yet implemented")
//        }
//
//        override suspend fun addMinimalList(list: List<MinimalDogCacheEntity>): List<Long> {
//            return emptyList()
//        }
//
//        override suspend fun updateList(list: List<MinimalDogCacheEntity>) {
//
//        }
//
//        override fun getAllFavorites(): Flow<List<DogCacheEntity>> {
//            TODO("Not yet implemented")
//        }
//
//        override suspend fun setFavorite(breedId: Int) {
//
//        }
//
//        override suspend fun removeFavorite(breedId: Int) {
//
//        }
//
//    }
//}
//
//@Module
//@TestInstallIn(
//    components = [SingletonComponent::class],
//    replaces = [DogsRemoteStaticModule::class]
//)
//object TestModule {
//
//    @Provides
//    fun provideApi(): DogsApi = object : DogsApi {
//
//        override suspend fun getBreeds(): Either<DataSourceError, List<DogWebEntity>> {
//            val dogEntityList = "/json/dog-list.json".parseList(DogWebEntity::class.java).take(25)
//            return dogEntityList.right()
//        }
//
//        override suspend fun searchBreed(input: String): Either<DataSourceError, List<DogWebEntity>> {
//            TODO("Not yet implemented")
//        }
//
//        override suspend fun searchImages(
//            limit: Int,
//            page: Int,
//            order: String
//        ): Either<DataSourceError, List<BreedImageWebEntity>> {
//            TODO("Not yet implemented")
//        }
//
//        override suspend fun searchImagesForBreed(
//            limit: Int,
//            breedId: Int,
//            size: String
//        ): Either<DataSourceError, List<BreedImageWebEntity>> {
//            TODO("Not yet implemented")
//        }
//
//        override suspend fun getImage(id: String): Either<DataSourceError, BreedImageWebEntity> {
//            TODO("Not yet implemented")
//        }
//
//    }
//}