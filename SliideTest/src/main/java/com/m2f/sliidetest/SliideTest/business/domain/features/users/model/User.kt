package com.m2f.sliidetest.SliideTest.business.domain.features.users.model

data class User(
        val id: Long,
        val name: String,
        val email: String,
        val gender: Gender
)