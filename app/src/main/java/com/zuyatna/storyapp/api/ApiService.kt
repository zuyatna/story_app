package com.zuyatna.storyapp.api

import com.zuyatna.storyapp.model.login.ResponseLogin
import com.zuyatna.storyapp.model.main.ResponseMain
import com.zuyatna.storyapp.model.register.ResponseRegister
import retrofit2.http.*

interface ApiService {
    @POST("register")
    @FormUrlEncoded
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): ResponseRegister

    @POST("login")
    @FormUrlEncoded
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): ResponseLogin

    @GET("stories")
    suspend fun getStories(
        @Header("Authorization") auth: String
    ): ResponseMain
}