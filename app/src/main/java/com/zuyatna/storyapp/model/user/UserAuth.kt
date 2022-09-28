package com.zuyatna.storyapp.model.user

object UserAuth {
    fun generateUserAuthorization(token: String) : String {
        return "Bearer $token"
    }
}