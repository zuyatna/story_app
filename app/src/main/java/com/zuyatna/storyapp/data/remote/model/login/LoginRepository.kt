package com.zuyatna.storyapp.data.remote.model.login

import com.zuyatna.storyapp.data.remote.retrofit.ApiConfig
import com.zuyatna.storyapp.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginRepository @Inject constructor(private val loginSource: LoginSource) : ApiConfig() {
    suspend fun login(email: String, password: String): Flow<NetworkResult<LoginResponse>> =
        flow {
            emit(safeApiCall {
                loginSource.login(email, password)
            })
        }.flowOn(Dispatchers.IO)
}