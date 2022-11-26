package de.nilsdruyen.koncept.cache

import kotlinx.coroutines.flow.Flow

interface PreferenceController {

    suspend fun get(key: String, default: String): String

    suspend fun set(key: String, value: String)

    suspend fun getFlow(key: String, default: String): Flow<String>
}