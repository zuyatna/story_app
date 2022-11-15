package com.zuyatna.storyapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.zuyatna.storyapp.data.remote.model.login.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginRepository: LoginRepository) : ViewModel() {
    suspend fun login(email: String, password: String) = loginRepository.login(email, password)
}