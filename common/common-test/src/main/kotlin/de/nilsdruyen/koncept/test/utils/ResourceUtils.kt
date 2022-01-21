package de.nilsdruyen.koncept.test.utils

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

private val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()

fun String.asResource(): String = try {
    object {}.javaClass.getResource(this)!!.readText()
} catch (e: Exception) {
    throw IllegalStateException("Failed to load resource=$this!", e)
}

fun <T> String.parse(type: Class<T>): T? = moshi.adapter(type).fromJson(asResource())

fun <T> String.parseList(type: Class<T>): List<T> {
    return moshi.adapter<List<T>>(Types.newParameterizedType(List::class.java, type))
        .fromJson(asResource()) ?: emptyList()
}