package de.nilsdruyen.koncept.data.converters

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter

@ProvidedTypeConverter
class IntRangeConverter {

    @TypeConverter
    fun rangeToString(value: IntRange): String {
        return "${value.first};${value.last}"
    }

    @TypeConverter
    fun stringToRange(value: String): IntRange {
        if (value.isEmpty()) return IntRange.EMPTY
        val (start, end) = value.split(";")
        return start.toInt()..end.toInt()
    }
}