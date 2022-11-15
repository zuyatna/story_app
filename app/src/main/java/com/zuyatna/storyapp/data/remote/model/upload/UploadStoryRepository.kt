package com.zuyatna.storyapp.data.remote.model.upload

import com.zuyatna.storyapp.data.remote.model.user.UserAuth.generateUserAuthorization
import com.zuyatna.storyapp.data.remote.retrofit.ApiConfig
import com.zuyatna.storyapp.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UploadStoryRepository @Inject constructor(private val uploadStorySource: UploadStorySource) : ApiConfig() {
    suspend fun uploadStory(
        auth: String,
        description: String,
        lat: String?,
        lon: String?,
        file: MultipartBody.Part
    ): Flow<NetworkResult<UploadStoryResponse>> =
        flow {
            emit(safeApiCall {
                val generateToken = generateUserAuthorization(auth)
                uploadStorySource.uploadStory(generateToken, description, lat, lon, file)
            })
        }.flowOn(Dispatchers.IO)
}