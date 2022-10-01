package com.zuyatna.storyapp.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zuyatna.storyapp.databinding.CardItemStoryBinding
import com.zuyatna.storyapp.model.main.ListStory

class MainAdapter(private val context: Context, private val clickListener: OnItemClickAdapter) :
    ListAdapter<ListStory, MainAdapter.MainViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder = MainViewHolder(
        CardItemStoryBinding.inflate(LayoutInflater.from(context), parent, false)
    )

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val item = currentList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = currentList.size

    inner class MainViewHolder(private val binding: CardItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(listStory: ListStory) {
            with(binding) {
                tvCardUsername.text = listStory.name
                tvCardDesc.text = listStory.description
                Glide.with(context)
                    .load(listStory.photoUrl)
                    .into(ivCardItemStory)
                root.setOnClickListener {
                    val optionsCompat: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(context as Activity,
                        Pair(binding.tvCardUsername, "name"),
                        Pair(binding.tvCardDesc, "description"),
                        Pair(binding.ivCardItemStory, "image")
                    )

                    clickListener.onItemClicked(listStory, optionsCompat)
                }
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<ListStory>() {

        override fun areItemsTheSame(oldItem: ListStory, newItem: ListStory) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ListStory, newItem: ListStory) =
            oldItem == newItem
    }


    interface OnItemClickAdapter {
        fun onItemClicked(listStory: ListStory, optionsCompat: ActivityOptionsCompat)
    }
}