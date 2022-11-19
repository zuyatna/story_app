package com.zuyatna.storyapp.data.remote.retrofit

import com.zuyatna.storyapp.data.remote.model.login.LoginResponse
import com.zuyatna.storyapp.data.remote.model.main.MainResponse
import com.zuyatna.storyapp.data.remote.model.register.RegisterResponse
import com.zuyatna.storyapp.data.remote.model.upload.UploadStoryResponse
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
    ) : retrofit2.Response<RegisterResponse>

    @POST("login")
    @FormUrlEncoded
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): retrofit2.Response<LoginResponse>

    @GET("stories")
    suspend fun getStories(
        @Header("Authorization") auth: String,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("location") location: Int? = null
    ): retrofit2.Response<MainResponse>

    @Multipart
    @POST("stories")
    suspend fun uploadStory(
        @Header("Authorization") auth: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: RequestBody?,
        @Part("lon") long: RequestBody?
    ) : retrofit2.Response<UploadStoryResponse>
}