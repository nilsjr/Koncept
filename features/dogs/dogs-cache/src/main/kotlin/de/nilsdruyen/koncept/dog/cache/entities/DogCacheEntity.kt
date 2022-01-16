package de.nilsdruyen.koncept.dog.cache.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dog_table")
data class DogCacheEntity(
    @PrimaryKey
    val id: String,
    val name: String,
)