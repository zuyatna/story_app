package com.zuyatna.storyapp.model.main

import com.zuyatna.storyapp.model.user.UserAuth
import com.zuyatna.storyapp.service.ApiService
import com.zuyatna.storyapp.utility.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException

class MainModel constructor(private val apiService: ApiService) {
    suspend fun getStories(auth: String) : Flow<NetworkResult<ResponseMain>> = flow {
        try {
            val generateToken = UserAuth.generateAuthorization(auth)
            val response = apiService.getStories(generateToken)
            emit(NetworkResult.Success(response))
        } catch (e : Exception) {
            val exception = (e as? HttpException)?.response()?.errorBody()?.string()
            emit(NetworkResult.Error(exception.toString()))
        }
    }.flowOn(Dispatchers.IO)
}