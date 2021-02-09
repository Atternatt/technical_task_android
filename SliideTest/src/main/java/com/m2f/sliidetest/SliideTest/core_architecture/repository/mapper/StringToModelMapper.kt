package com.m2f.sliidetest.SliideTest.core_architecture.repository.mapper

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * @author Marc Moreno
 * @since 9/1/2018.
 */

/**
 * Map a json string to a class object
 */
class StringToModelMapper<out T>(private val clz: Class<T>, private val gson: Gson) : Mapper<String, T> {
    override fun map(from: String): T = gson.fromJson(from, clz)
}

/**
 * Map a class object to a json string
 */
class ModelToStringMapper<in T>(private val gson: Gson) : Mapper<T, String> {
    override fun map(from: T): String = gson.toJson(from)
}

/**
 * Map a list json string to a list class object
 */
class StringToListModelMapper<out T>(private val clz: TypeToken<List<T>>, private val gson: Gson) : Mapper<String, List<T>> {
    override fun map(from: String): List<T> {
        return gson.fromJson(from, clz.type)
    }
}

/**
 * Map a list class object to a json string
 */
class ListModelToStringMapper<in T>(private val gson: Gson) : Mapper<List<T>, String> {
    override fun map(from: List<T>): String = gson.toJson(from)
}