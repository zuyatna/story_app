package com.zuyatna.storyapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zuyatna.storyapp.data.local.model.login.LoginRepository

class LoginViewModel constructor(private val loginRepository: LoginRepository) : ViewModel() {
    suspend fun login(email: String, password: String) = loginRepository.loginUser(email, password)
}

@Suppress("UNCHECKED_CAST")
class LoginViewModelFactory constructor(private val loginRepository: LoginRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(loginRepository) as T
            }
            else -> {
                throw IllegalArgumentException("Class ViewModel not Implement")
            }
        }
    }
}