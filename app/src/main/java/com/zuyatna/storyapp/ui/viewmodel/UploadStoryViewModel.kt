package com.zuyatna.storyapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zuyatna.storyapp.data.local.model.upload.UploadStoryModel
import java.io.File

class UploadStoryViewModel constructor(private val uploadStoryModel: UploadStoryModel) : ViewModel() {
    suspend fun uploadStory(userAuth: String, description: String, file: File) = uploadStoryModel.uploadStory(userAuth, description, file)
}

@Suppress("UNCHECKED_CAST")
class UploadStoryViewModelFactory constructor(private val uploadStoryModel: UploadStoryModel) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(UploadStoryViewModel::class.java) -> {
                UploadStoryViewModel(uploadStoryModel) as T
            }
            else -> {
                throw IllegalArgumentException("Class ViewModel not Implement")
            }
        }
    }
}