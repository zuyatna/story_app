package com.zuyatna.storyapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zuyatna.storyapp.model.register.Register

class RegisterViewModel constructor(private val register: Register) : ViewModel() {
    suspend fun register(name: String, email: String, password: String) = register.registration(name, email, password)
}

@Suppress("UNCHECKED_CAST")
class RegisterViewModelFactory constructor(private val register: Register) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(register) as T
            }
            else -> {
                throw IllegalArgumentException("Class ViewModel not Implement")
            }
        }
    }
}