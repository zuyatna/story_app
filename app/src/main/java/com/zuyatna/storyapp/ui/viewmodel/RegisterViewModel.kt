package com.zuyatna.storyapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.zuyatna.storyapp.data.remote.model.register.RegisterRepository
import javax.inject.Inject

class RegisterViewModel @Inject constructor(private val registerRepository: RegisterRepository) : ViewModel() {
    suspend fun register(name: String, email: String, password: String) = registerRepository.register(name, email, password)
}