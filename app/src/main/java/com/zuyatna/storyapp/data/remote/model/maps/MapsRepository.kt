package com.zuyatna.storyapp.data.remote.model.maps

import com.zuyatna.storyapp.data.remote.model.main.MainResponse
import com.zuyatna.storyapp.data.remote.model.main.StorySource
import com.zuyatna.storyapp.data.remote.retrofit.ApiConfig
import com.zuyatna.storyapp.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MapsRepository @Inject constructor(private val storySource: StorySource) : ApiConfig() {
    suspend fun getStoriesLocation(auth: String): Flow<NetworkResult<MainResponse>> =
        flow {
            emit(safeApiCall {
                storySource.getStoriesLocation(generateAuthorization(auth))
            })
        }.flowOn(Dispatchers.IO)

    private fun generateAuthorization(token: String): String {
        return "Bearer $token"
    }
}