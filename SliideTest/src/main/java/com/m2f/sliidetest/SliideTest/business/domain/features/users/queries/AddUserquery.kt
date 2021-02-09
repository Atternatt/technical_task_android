package com.m2f.sliidetest.SliideTest.business.domain.features.users.queries

import com.m2f.sliidetest.SliideTest.core_architecture.repository.query.Query

class AddUserquery(val name: String, val email: String, val userName: String) : Query()