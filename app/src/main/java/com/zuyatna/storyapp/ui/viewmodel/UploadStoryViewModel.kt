package com.zuyatna.storyapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.zuyatna.storyapp.data.remote.model.upload.UploadStoryRepository
import okhttp3.MultipartBody
import javax.inject.Inject

class UploadStoryViewModel @Inject constructor(private val uploadStoryRepository: UploadStoryRepository) : ViewModel() {
    suspend fun uploadStory(auth: String, description: String, lat: String?, lon: String?, file: MultipartBody.Part) =  uploadStoryRepository.uploadStory(auth, description, lat, lon, file)
}