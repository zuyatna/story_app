package com.zuyatna.storyapp.data.remote.model.main

import com.zuyatna.storyapp.data.remote.retrofit.ApiService
import javax.inject.Inject

class StorySource @Inject constructor(private val apiService: ApiService) {
    suspend fun getStoriesLocation(auth: String) = apiService.getStories(auth = auth, location = 1)
}