package com.zuyatna.storyapp.data.remote.model.register

import com.zuyatna.storyapp.data.remote.retrofit.ApiConfig
import com.zuyatna.storyapp.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RegisterRepository @Inject constructor(private val registerSource: RegisterSource) : ApiConfig() {
    suspend fun register(
        name: String,
        email: String,
        password: String
    ): Flow<NetworkResult<RegisterResponse>> = flow {
        emit(safeApiCall {
            registerSource.register(name, email, password)
        })
    }.flowOn(Dispatchers.IO)
}