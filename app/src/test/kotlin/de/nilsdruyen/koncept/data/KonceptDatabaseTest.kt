package de.nilsdruyen.koncept.data

import android.content.Context
import android.os.Build
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.core.app.ApplicationProvider
import com.squareup.moshi.Moshi
import de.nilsdruyen.koncept.data.converters.IntRangeConverter
import de.nilsdruyen.koncept.data.converters.StringListConverter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.File

/**
 * Creates the database with an exported schema of an older version and opens it with the current
 * [KonceptDatabase], which runs the migrations and validates the result against the latest schema.
 */
@Config(sdk = [Build.VERSION_CODES.TIRAMISU])
@RunWith(ParameterizedRobolectricTestRunner::class)
internal class KonceptDatabaseTest(private val startVersion: Int) {

    private val context = ApplicationProvider.getApplicationContext<Context>()

    @After
    fun tearDown() {
        context.deleteDatabase(DB_NAME)
    }

    @Test
    fun `GIVEN an old schema WHEN the database is opened THEN it is migrated and keeps its data`() {
        createDatabase(startVersion) { it.execSQL("INSERT INTO dog_table (id, name) VALUES (1, 'Akita')") }

        val database = Room.databaseBuilder(context, KonceptDatabase::class.java, DB_NAME)
            .addTypeConverter(IntRangeConverter())
            .addTypeConverter(StringListConverter(Moshi.Builder().build()))
            .allowMainThreadQueries()
            .build()
        val breeds = runBlocking { database.dogDao().getAll().first() }
        database.close()

        assertEquals(listOf("Akita"), breeds.map { it.name })
    }

    private fun createDatabase(version: Int, insertData: (SupportSQLiteDatabase) -> Unit) {
        val schema = JSONObject(File("$SCHEMA_DIR/$version.json").readText()).getJSONObject("database")
        val callback = object : SupportSQLiteOpenHelper.Callback(version) {
            override fun onCreate(db: SupportSQLiteDatabase) {
                val entities = schema.getJSONArray("entities")
                for (i in 0 until entities.length()) {
                    val entity = entities.getJSONObject(i)
                    db.execSQL(entity.getString("createSql").replace("\${TABLE_NAME}", entity.getString("tableName")))
                }
                val setupQueries = schema.getJSONArray("setupQueries")
                for (i in 0 until setupQueries.length()) {
                    db.execSQL(setupQueries.getString(i))
                }
            }

            override fun onUpgrade(db: SupportSQLiteDatabase, oldVersion: Int, newVersion: Int) = Unit
        }
        val configuration = SupportSQLiteOpenHelper.Configuration.builder(context)
            .name(DB_NAME)
            .callback(callback)
            .build()
        FrameworkSQLiteOpenHelperFactory().create(configuration).use { insertData(it.writableDatabase) }
    }

    companion object {

        private const val DB_NAME = "migration-test"
        private val SCHEMA_DIR = "schemas/${KonceptDatabase::class.java.canonicalName}"

        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters(name = "startVersion={0}")
        fun data(): Collection<Array<Any>> = (1 until DB_VERSION).map { arrayOf(it) }
    }
}
