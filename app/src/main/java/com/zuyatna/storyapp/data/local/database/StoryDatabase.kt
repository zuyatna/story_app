package com.zuyatna.storyapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.zuyatna.storyapp.data.local.entity.Story

@Database(
    entities =[Story::class, RemoteKeys::class],
    version = 1,
    exportSchema = false
)
abstract class StoryDatabase : RoomDatabase() {
    abstract fun storyDao(): StoryDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}