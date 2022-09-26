package com.zuyatna.storyapp.model.login

import com.zuyatna.storyapp.service.ApiService
import com.zuyatna.storyapp.utility.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class LoginModel constructor(private val apiService: ApiService) {
    suspend fun loginUser(email: String, password: String): Flow<NetworkResult<ResponseLogin>> = flow {
        try {
            val service = apiService.login(email, password)
            emit(NetworkResult.Success(service))
        } catch (e : Exception) {
            e.printStackTrace()
            emit(NetworkResult.Error(e.toString()))
        }
    }.flowOn(Dispatchers.IO)
}