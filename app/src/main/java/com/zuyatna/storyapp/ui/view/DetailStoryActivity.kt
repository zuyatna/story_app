package com.zuyatna.storyapp.ui.view

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.zuyatna.storyapp.R
import com.zuyatna.storyapp.databinding.ActivityDetailStoryBinding
import com.zuyatna.storyapp.data.local.model.main.StoryModel

class DetailStoryActivity : AppCompatActivity() {
    private val binding: ActivityDetailStoryBinding by lazy {
        ActivityDetailStoryBinding.inflate(layoutInflater)
    }

    companion object {
        const val DETAIL_STORY = "detail_story"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        fetchData()
    }

    @Suppress("DEPRECATION")
    private fun fetchData() {
        val getIntent = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(DETAIL_STORY, StoryModel::class.java)
        } else {
            intent.getParcelableExtra(DETAIL_STORY)
        }

        binding.apply {
            tvDetailStoryUsername.text = getIntent?.name
            tvDetailStoryDescription.text = getIntent?.description
            Glide.with(this@DetailStoryActivity)
                .load(getIntent?.photoUrl)
                .into(ivDetailStoryPhoto)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}