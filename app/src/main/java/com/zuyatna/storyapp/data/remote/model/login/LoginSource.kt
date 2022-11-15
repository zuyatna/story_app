package com.zuyatna.storyapp.data.remote.model.login

import com.zuyatna.storyapp.data.remote.retrofit.ApiService
import javax.inject.Inject

class LoginSource @Inject constructor(private val apiService: ApiService) {
    suspend fun login(email: String, password: String) = apiService.login(email, password)

    suspend fun getStoriesLocation(auth: String) = apiService.getStories(auth = auth, location = 1)
}