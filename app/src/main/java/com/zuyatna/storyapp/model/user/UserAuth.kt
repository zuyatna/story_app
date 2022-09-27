package com.zuyatna.storyapp.model.user

object UserAuth {
    fun generateAuthorization(token: String) : String {
        return "Bearer $token"
    }
}