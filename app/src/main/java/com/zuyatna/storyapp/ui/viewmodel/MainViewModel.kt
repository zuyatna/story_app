package com.zuyatna.storyapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.zuyatna.storyapp.data.local.entity.Story
import com.zuyatna.storyapp.data.remote.model.main.StoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val storyRepository: StoryRepository) : ViewModel() {
    fun getStories(userAuth: String) : LiveData<PagingData<Story>> = storyRepository.getStories(userAuth).cachedIn(viewModelScope).asLiveData()
}