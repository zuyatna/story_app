package com.zuyatna.storyapp.data.remote.model.main

import com.google.gson.annotations.SerializedName

data class MainResponse(
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("listStory")
    val storyModel: List<StoryModel>,
)