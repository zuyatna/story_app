package com.zuyatna.storyapp.model.register

import com.zuyatna.storyapp.retrofit.ApiService
import com.zuyatna.storyapp.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RegisterModel constructor(private val apiService: ApiService) {
    suspend fun registration(name: String, email: String, password: String): Flow<NetworkResult<RegisterResponse>> = flow {
        try {
            val service = apiService.register(name, email, password)
            emit(NetworkResult.Success(service))
        } catch (e : Exception) {
            e.printStackTrace()
            emit(NetworkResult.Error(e.toString()))
        }
    }.flowOn(Dispatchers.IO)
}