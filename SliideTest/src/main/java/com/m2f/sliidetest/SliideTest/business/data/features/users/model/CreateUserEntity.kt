package com.m2f.sliidetest.SliideTest.business.data.features.users.model

import com.google.gson.annotations.SerializedName

data class CreateUserEntity(
        @SerializedName("name") val name: String,
        @SerializedName("email") val email: String,
        @SerializedName("gender") val gender: String,
        @SerializedName("status") val status: String
)