package com.zuyatna.storyapp.data.remote.model.register

import com.zuyatna.storyapp.data.remote.retrofit.ApiService
import javax.inject.Inject

class RegisterSource @Inject constructor(private val apiService: ApiService) {
    suspend fun register(name: String, email: String, password: String) = apiService.register(name, email, password)
}