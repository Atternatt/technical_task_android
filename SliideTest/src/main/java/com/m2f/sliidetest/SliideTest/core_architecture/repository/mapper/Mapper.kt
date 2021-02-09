package com.m2f.sliidetest.SliideTest.core_architecture.repository.mapper


/**
 * Interface to map an object type to another object type
 *
 * @author Marc Moreno
 * @since 9/1/2018.
 */
interface Mapper<in From, out To> : (From) -> To {

    fun map(from: From): To

    override operator fun invoke(from: From): To = map(from)
}