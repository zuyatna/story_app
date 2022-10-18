package com.zuyatna.storyapp.data.local.model.upload

import com.zuyatna.storyapp.data.local.model.user.UserAuth
import com.zuyatna.storyapp.data.local.retrofit.ApiService
import com.zuyatna.storyapp.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class UploadStoryRepository constructor(private val apiService: ApiService) {
    suspend fun uploadStory(userAuth: String, description: String, file: File) : Flow<NetworkResult<UploadStoryResponse>> = flow {
        try {
            val generateToken = UserAuth.generateUserAuthorization(userAuth)
            val desc = description.toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpg".toMediaTypeOrNull())
            val imageMultipart = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )

            val service = apiService.uploadStory(generateToken,imageMultipart,desc)
            emit(NetworkResult.Success(service))
        } catch (e : Exception) {
            val exception = (e as? HttpException)?.response()?.errorBody()?.string()
            emit(NetworkResult.Error(exception.toString()))
        }
    }.flowOn(Dispatchers.IO)
}