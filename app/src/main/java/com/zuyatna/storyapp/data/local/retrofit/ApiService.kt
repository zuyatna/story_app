package com.zuyatna.storyapp.data.local.retrofit

import com.zuyatna.storyapp.data.local.model.login.LoginResponse
import com.zuyatna.storyapp.data.local.model.main.MainResponse
import com.zuyatna.storyapp.data.local.model.register.RegisterResponse
import com.zuyatna.storyapp.data.local.model.upload.UploadStoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {
    @POST("register")
    @FormUrlEncoded
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @POST("login")
    @FormUrlEncoded
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @GET("stories")
    suspend fun getStories(
        @Header("Authorization") auth: String
    ): MainResponse

    @Multipart
    @POST("stories")
    suspend fun uploadStory(
        @Header("Authorization") auth: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ) : UploadStoryResponse
}