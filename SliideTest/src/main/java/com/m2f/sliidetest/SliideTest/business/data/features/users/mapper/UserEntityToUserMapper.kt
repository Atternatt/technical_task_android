package com.m2f.sliidetest.SliideTest.business.data.features.users.mapper

import com.m2f.sliidetest.SliideTest.business.data.features.users.model.UserEntity
import com.m2f.sliidetest.SliideTest.business.domain.features.users.model.Gender
import com.m2f.sliidetest.SliideTest.business.domain.features.users.model.User
import com.m2f.sliidetest.SliideTest.core_architecture.repository.mapper.Mapper


object UserEntityToUserMapper : Mapper<UserEntity, User> {
    override fun map(from: UserEntity): User = with(from) {
        val gender = Gender.values().findLast { it.value == gender } ?: Gender.MALE
        User(id, name, email, gender)
    }
}