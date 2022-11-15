package com.zuyatna.storyapp.data.remote.model.upload

import com.zuyatna.storyapp.data.remote.retrofit.ApiService
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import javax.inject.Inject

class UploadStorySource @Inject constructor(private val apiService: ApiService) {
    suspend fun uploadStory(auth: String, description: String, lat: String?, lon: String?, file: MultipartBody.Part) : Response<UploadStoryResponse> {
        val desc = description.toRequestBody("text/plain".toMediaType())
        val latitude = lat?.toRequestBody("text/plain".toMediaType())
        val longitude = lon?.toRequestBody("text/plain".toMediaType())
        return apiService.uploadStory(auth, file, desc, latitude, longitude)
    }
}