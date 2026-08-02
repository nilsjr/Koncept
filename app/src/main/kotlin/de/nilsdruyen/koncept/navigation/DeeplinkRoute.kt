package de.nilsdruyen.koncept.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class DeeplinkRoute(val rawDate: String, val rawDate2: String) : NavKey
