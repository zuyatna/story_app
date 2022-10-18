package com.zuyatna.storyapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zuyatna.storyapp.data.local.model.register.RegisterRepository

class RegisterViewModel constructor(private val registerRepository: RegisterRepository) : ViewModel() {
    suspend fun register(name: String, email: String, password: String) = registerRepository.registration(name, email, password)
}

@Suppress("UNCHECKED_CAST")
class RegisterViewModelFactory constructor(private val registerRepository: RegisterRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(registerRepository) as T
            }
            else -> {
                throw IllegalArgumentException("Class ViewModel not Implement")
            }
        }
    }
}