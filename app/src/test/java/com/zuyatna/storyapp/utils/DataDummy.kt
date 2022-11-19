package com.zuyatna.storyapp.utils

import com.zuyatna.storyapp.data.local.entity.Story
import com.zuyatna.storyapp.data.remote.model.login.LoginModel
import com.zuyatna.storyapp.data.remote.model.login.LoginResponse
import com.zuyatna.storyapp.data.remote.model.main.MainResponse
import com.zuyatna.storyapp.data.remote.model.main.StoryModel
import com.zuyatna.storyapp.data.remote.model.register.RegisterResponse
import com.zuyatna.storyapp.data.remote.model.upload.UploadStoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

object DataDummy {
    fun generateDummyStoriesResponse(): MainResponse {
        val error = false
        val message = "Stories fetched successfully"
        val listStory = mutableListOf<StoryModel>()

        for (i in 0 until 10) {
            val story = StoryModel(
                id = "story-FvU4u0Vp2S3PMsFg",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1641623658595_dummy-pic.png",
                createAt = "2022-01-08T06:34:18.598Z",
                name = "Dimas",
                description = "Lorem Ipsum",
                longitude = -16.002,
                latitude = -10.212
            )

            listStory.add(story)
        }

        return MainResponse(error, message, listStory)
    }

    fun generateDummyListStory(): List<Story> {
        val items = arrayListOf<Story>()

        for (i in 0 until 10) {
            val story = Story(
                id = "story-FvU4u0Vp2S3PMsFg",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1641623658595_dummy-pic.png",
                createdAt = "2022-01-08T06:34:18.598Z",
                name = "Dimas",
                description = "Lorem Ipsum",
                lon = -16.002,
                lat = -10.212
            )

            items.add(story)
        }

        return items
    }


    fun generateDummyLoginResponse(): LoginResponse {
        val loginResult = LoginModel (
            userId = "user-qpqW5cVRc6qNEVvr",
            name = "Antasuy22",
            token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLXFwcVc1Y1ZSYzZxTkVWdnIiLCJpYXQiOjE2Njg4NDcwNDR9.mVXObwq998n-sKvo7We3BlKG5fgsnB22KLeigV829SM"
        )

        return LoginResponse(
            loginModel = loginResult,
            error = false,
            message = "success"
        )
    }

    fun generateDummyRegisterResponse(): RegisterResponse {
        return RegisterResponse(
            error = false,
            message = "success"
        )
    }

    fun generateDummyMultipartFile(): MultipartBody.Part {
        val dummyText = "text"
        return MultipartBody.Part.create(dummyText.toRequestBody())
    }

    fun generateDummyRequestBody(): String {
        return "text"
    }

    fun generateDummyFileUploadResponse(): UploadStoryResponse {
        return UploadStoryResponse(
            error = false,
            message = "success"
        )
    }

    fun generateDummyToken() : String {
        return "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLXFwcVc1Y1ZSYzZxTkVWdnIiLCJpYXQiOjE2Njg4NDcwNDR9.mVXObwq998n-sKvo7We3BlKG5fgsnB22KLeigV829SM"
    }
}