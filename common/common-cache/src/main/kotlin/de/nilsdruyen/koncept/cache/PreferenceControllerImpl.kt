package de.nilsdruyen.koncept.cache

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PreferenceControllerImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : PreferenceController {

    override suspend fun get(key: String, default: String): String = getFlow(key, default).first()

    override suspend fun set(key: String, value: String) {
        dataStore.edit { set(key, value) }
    }

    override suspend fun getFlow(key: String, default: String): Flow<String> {
        return dataStore.data.map { it[stringPreferencesKey(key)] ?: default }
    }
}