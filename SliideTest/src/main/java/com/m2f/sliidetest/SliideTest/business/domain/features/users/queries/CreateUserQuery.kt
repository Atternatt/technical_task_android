package com.m2f.sliidetest.SliideTest.business.domain.features.users.queries

import com.m2f.sliidetest.SliideTest.business.domain.features.users.model.Gender
import com.m2f.sliidetest.SliideTest.core_architecture.repository.query.Query

data class CreateUserQuery(
    val name: String,
    val email: String,
    val gender: Gender,
    val status: String
): Query()