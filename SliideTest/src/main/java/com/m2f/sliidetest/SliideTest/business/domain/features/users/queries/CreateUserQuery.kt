package com.m2f.sliidetest.SliideTest.business.domain.features.users.queries

data class CreateUserQuery(
    val name: String,
    val email: String,
    val gender: String,
    val status: String
)