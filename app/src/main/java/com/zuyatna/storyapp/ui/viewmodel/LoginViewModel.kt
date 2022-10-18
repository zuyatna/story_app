package com.zuyatna.storyapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zuyatna.storyapp.data.local.model.login.LoginModel

class LoginViewModel constructor(private val loginModel: LoginModel) : ViewModel() {
    suspend fun login(email: String, password: String) = loginModel.loginUser(email, password)
}

@Suppress("UNCHECKED_CAST")
class LoginViewModelFactory constructor(private val loginModel: LoginModel) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(loginModel) as T
            }
            else -> {
                throw IllegalArgumentException("Class ViewModel not Implement")
            }
        }
    }
}