package de.nilsdruyen.koncept.common.ui.providers

interface PropertyProvider {

    fun <T> get(key: String, default: () -> T): T
}
