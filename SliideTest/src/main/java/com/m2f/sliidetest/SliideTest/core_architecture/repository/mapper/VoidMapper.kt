package com.m2f.sliidetest.SliideTest.core_architecture.repository.mapper

class VoidMapper<in From, out To>: Mapper<From, To> {

    override fun map(from: From): To = throw NotImplementedError()
}