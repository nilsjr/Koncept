package de.nilsdruyen.koncept.data.converters

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

@ProvidedTypeConverter
class StringListConverter constructor(moshi: Moshi) {

    private val adapter: JsonAdapter<List<String>> = moshi.adapter(
        Types.newParameterizedType(List::class.java, String::class.java)
    )

    @TypeConverter
    fun listToJson(value: List<String>): String {
        return adapter.toJson(value)
    }

    @TypeConverter
    fun jsonToList(value: String): List<String> {
        return adapter.fromJson(value) ?: emptyList()
    }
}
