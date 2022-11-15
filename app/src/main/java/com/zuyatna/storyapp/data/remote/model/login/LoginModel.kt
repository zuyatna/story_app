package com.zuyatna.storyapp.data.remote.model.login

import com.google.gson.annotations.SerializedName

data class LoginModel(
    @field:SerializedName("userId")
    val userId: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("token")
    val token: String,
)