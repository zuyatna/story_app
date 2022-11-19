package com.zuyatna.storyapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.zuyatna.storyapp.data.remote.model.maps.MapsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(private val mapsRepository: MapsRepository) : ViewModel() {
    suspend fun getStoriesLocation(auth: String) = mapsRepository.getStoriesLocation(auth)
}