package com.zuyatna.storyapp.model.main

import com.zuyatna.storyapp.api.ApiService
import com.zuyatna.storyapp.model.user.UserAuth
import com.zuyatna.storyapp.utility.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException

class MainModel constructor(private val apiService: ApiService) {
    suspend fun getStories(userAuth: String) : Flow<NetworkResult<MainResponse>> = flow {
        try {
            val generateToken = UserAuth.generateUserAuthorization(userAuth)
            val service = apiService.getStories(generateToken)
            emit(NetworkResult.Success(service))
        } catch (e : Exception) {
            val exception = (e as? HttpException)?.response()?.errorBody()?.string()
            emit(NetworkResult.Error(exception.toString()))
        }
    }.flowOn(Dispatchers.IO)
}