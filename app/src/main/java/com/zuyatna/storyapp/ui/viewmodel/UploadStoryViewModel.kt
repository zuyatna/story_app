package com.zuyatna.storyapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zuyatna.storyapp.data.local.model.upload.UploadStoryRepository
import java.io.File

class UploadStoryViewModel constructor(private val uploadStoryRepository: UploadStoryRepository) : ViewModel() {
    suspend fun uploadStory(userAuth: String, description: String, file: File) = uploadStoryRepository.uploadStory(userAuth, description, file)
}

@Suppress("UNCHECKED_CAST")
class UploadStoryViewModelFactory constructor(private val uploadStoryRepository: UploadStoryRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(UploadStoryViewModel::class.java) -> {
                UploadStoryViewModel(uploadStoryRepository) as T
            }
            else -> {
                throw IllegalArgumentException("Class ViewModel not Implement")
            }
        }
    }
}