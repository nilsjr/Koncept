package de.nilsdruyen.koncept.dogs.cache.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dog_table")
data class DogCacheEntity(
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "isFavorite", defaultValue = "0")
    val isFavorite: Boolean,
    @ColumnInfo(name = "lifeSpan", defaultValue = "")
    val lifeSpan: IntRange,
    @ColumnInfo(name = "weight", defaultValue = "")
    val weight: IntRange,
    @ColumnInfo(name = "height", defaultValue = "")
    val height: IntRange,
    @ColumnInfo(name = "temperament", defaultValue = "")
    val temperament: List<String>,
    @ColumnInfo(name = "origin", defaultValue = "")
    val origin: List<String>,
    @ColumnInfo(name = "bredFor", defaultValue = "")
    val bredFor: String,
    @ColumnInfo(name = "group", defaultValue = "0")
    val group: String,
    @ColumnInfo(name = "imageId", defaultValue = "")
    val imageId: String,
)