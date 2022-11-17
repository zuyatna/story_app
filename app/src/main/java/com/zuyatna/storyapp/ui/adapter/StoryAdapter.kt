package com.zuyatna.storyapp.ui.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.zuyatna.storyapp.R
import com.zuyatna.storyapp.data.local.entity.Story
import com.zuyatna.storyapp.databinding.CardItemStoryBinding
import com.zuyatna.storyapp.ui.view.DetailStoryActivity

class StoryAdapter : PagingDataAdapter<Story, StoryAdapter.StoryViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = CardItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = getItem(position)

        if (story != null) {
            holder.bind(holder.itemView.context, story)
        }
    }

    inner class StoryViewHolder(private val binding: CardItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(context: Context, story: Story) {
            with(binding) {
                tvCardUsername.text = story.name
                tvCardDesc.text = story.description

                val options = RequestOptions()
                    .placeholder(R.drawable.ic_baseline_refresh_24)
                    .error(R.drawable.ic_baseline_error_outline_24)

                Glide.with(context)
                    .load(story.photoUrl)
                    .apply(options)
                    .into(ivCardItemStory)

                root.setOnClickListener {
                    val optionsCompat: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(root.context as Activity,
                        Pair(binding.tvCardUsername, "name"),
                        Pair(binding.tvCardDesc, "description"),
                        Pair(binding.ivCardItemStory, "image")
                    )

                    Intent(context, DetailStoryActivity::class.java).also { intent ->
                        intent.putExtra(DetailStoryActivity.DETAIL_STORY, story)
                        context.startActivity(intent, optionsCompat.toBundle())
                    }
                }
            }
        }
    }

    companion object {
        val DiffCallback = object : DiffUtil.ItemCallback<Story>() {

            override fun areItemsTheSame(oldItem: Story, newItem: Story) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Story, newItem: Story) =
                oldItem == newItem
        }
    }
}