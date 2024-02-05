package de.nilsdruyen.koncept.common.ui.providers

import androidx.lifecycle.SavedStateHandle
import javax.inject.Inject

class PropertyProviderImpl @Inject constructor(private val savedStateHandle: SavedStateHandle) : PropertyProvider {

    override fun <T> get(key: String, default: () -> T): T {
        return savedStateHandle.get<T>(key) ?: default()
    }
}
