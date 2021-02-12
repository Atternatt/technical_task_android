package com.m2f.sliidetest.SliideTest.business.data.features.users.model

import com.google.gson.annotations.SerializedName

data class UsersResponse(
        @SerializedName("code") val code: Int,
        @SerializedName("meta") val meta: Meta,
        @SerializedName("data") val data: List<UserEntity>
)