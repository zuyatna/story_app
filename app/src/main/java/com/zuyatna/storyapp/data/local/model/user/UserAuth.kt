package com.zuyatna.storyapp.data.local.model.user

object UserAuth {
    fun generateUserAuthorization(token: String) : String {
        return "Bearer $token"
    }
}