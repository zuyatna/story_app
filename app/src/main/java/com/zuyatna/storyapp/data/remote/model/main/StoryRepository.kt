package com.zuyatna.storyapp.data.remote.model.main

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.zuyatna.storyapp.data.local.database.StoryDatabase
import com.zuyatna.storyapp.data.local.entity.Story
import com.zuyatna.storyapp.data.remote.StoryRemoteMediator
import com.zuyatna.storyapp.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@OptIn(ExperimentalPagingApi::class)
class StoryRepository @Inject constructor(private val database: StoryDatabase, private val apiService: ApiService) {
    fun getStories(userAuth: String) : Flow<PagingData<Story>> =
        Pager(
            config = PagingConfig(pageSize = 10),
            remoteMediator = StoryRemoteMediator(
                database,
                apiService,
                generateAuthorization(userAuth)
            ),
            pagingSourceFactory ={
                database.storyDao().getStories()
            }
        ).flow

    private fun generateAuthorization(token: String): String {
        return "Bearer $token"
    }
}