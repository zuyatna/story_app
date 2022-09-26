package com.zuyatna.storyapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zuyatna.storyapp.model.register.RegisterModel

class RegisterViewModel constructor(private val registerModel: RegisterModel) : ViewModel() {
    suspend fun register(name: String, email: String, password: String) = registerModel.registration(name, email, password)
}

@Suppress("UNCHECKED_CAST")
class RegisterViewModelFactory constructor(private val registerModel: RegisterModel) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(registerModel) as T
            }
            else -> {
                throw IllegalArgumentException("Class ViewModel not Implement")
            }
        }
    }
}