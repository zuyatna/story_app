package com.zuyatna.storyapp.data.remote.model.user

object UserAuth {
    fun generateUserAuthorization(token: String) : String {
        return "Bearer $token"
    }
}