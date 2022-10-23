package de.nilsdruyen.koncept.common.ui

import androidx.compose.runtime.Immutable

@Immutable
@Deprecated("use kotlinx.collections.immutable @see https://github.com/Kotlin/kotlinx.collections.immutable")
data class ImmutableList<T>(val items: List<T>) {

    val size
        get() = items.size
}

fun <T> emptyImmutableList(): ImmutableList<T> = ImmutableList(emptyList())

fun <T> List<T>.toImmutable() = ImmutableList(this)

fun <T> ImmutableList<T>.isEmpty() = items.isEmpty()