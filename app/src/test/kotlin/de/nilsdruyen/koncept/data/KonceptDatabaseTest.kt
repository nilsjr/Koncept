package de.nilsdruyen.koncept.data

// @RunWith(Parameterized::class)
// internal class KonceptDatabaseTest(val startVersion: Int, val endVersion: Int) {
//
//    private val name = "migration-test"
//
//    @get:Rule
//    val helper: MigrationTestHelper = MigrationTestHelper(
//        InstrumentationRegistry.getInstrumentation(),
//        KonceptDatabase::class.java.canonicalName,
//        FrameworkSQLiteOpenHelperFactory()
//    )
//
//    @Test
//    @Throws(IOException::class)
//    fun migrateVersion() {
//        helper.createDatabase(name, startVersion).close()
//        helper.runMigrationsAndValidate(
//            name,
//            endVersion,
//            true,
//            KonceptDatabase.migrations.first { it.startVersion == startVersion && it.endVersion == endVersion })
//    }
//
//    companion object {
//
//        @JvmStatic
//        @Parameterized.Parameters(name = "{index}: startVersion={0}, endVersion={1}")
//        fun data(): Collection<Array<Any>> {
//            return mutableListOf<Array<Any>>().apply {
//                // we can only test room migrations starting with schema 20 since we used greendao before
//                for (i in 20 until KonceptDatabase.DB_VERSION) {
//                    add(arrayOf(i, i + 1))
//                }
//            }
//        }
//    }
// }