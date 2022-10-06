package de.nilsdruyen.koncept.common.ui

import androidx.compose.runtime.Immutable

@Immutable
data class ImmutableList<T>(val items: List<T>)

fun <T> emptyImmutableList(): ImmutableList<T> = ImmutableList(emptyList())

fun <T> List<T>.toImmutable() = ImmutableList(this)

fun <T> ImmutableList<T>.isEmpty() = items.isEmpty()