package com.m2f.sliidetest.SliideTest.business.data.features.users.model

import com.google.gson.annotations.SerializedName

data class UserEntity(
    @SerializedName("email")
    val email: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("username")
    val username: String
)