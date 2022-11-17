package com.zuyatna.storyapp.ui.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.zuyatna.storyapp.data.local.entity.Story
import com.zuyatna.storyapp.databinding.ActivityDetailStoryBinding

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

        binding.apply {
            ivBack.setOnClickListener {
                onBackPressed()
            }
        }

        fetchData()
    }

    private fun fetchData() {
        val getIntent = intent.getParcelableExtra<Story>(DETAIL_STORY)

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