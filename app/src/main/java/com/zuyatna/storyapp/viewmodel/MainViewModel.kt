package com.zuyatna.storyapp.viewmodel

import androidx.lifecycle.*
import com.zuyatna.storyapp.model.main.MainModel
import com.zuyatna.storyapp.model.main.ResponseMain
import com.zuyatna.storyapp.utility.NetworkResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainViewModel constructor(private val mainModel: MainModel) : ViewModel() {
    private val listStory = MutableLiveData<NetworkResult<ResponseMain>>()
    private var job: Job? = null

    val dataListStory: LiveData<NetworkResult<ResponseMain>> = listStory

    fun fetchListStory(userAuth: String) {
        job = viewModelScope.launch {
            mainModel.getStories(userAuth).collectLatest {
                listStory.value = it
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory constructor(private val mainModel: MainModel) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(mainModel) as T
            }
            else -> {
                throw IllegalArgumentException("Class ViewModel not Implement")
            }
        }
    }
}