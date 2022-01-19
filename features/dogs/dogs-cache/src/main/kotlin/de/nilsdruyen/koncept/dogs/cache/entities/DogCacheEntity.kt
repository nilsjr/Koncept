package de.nilsdruyen.koncept.dogs.cache.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dog_table")
data class DogCacheEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
)