package com.zuyatna.storyapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zuyatna.storyapp.databinding.CardItemStoryBinding
import com.zuyatna.storyapp.model.main.ListStory

class MainAdapter(private val context: Context, private val clickListener: OnItemClickAdapter) :
    RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

    private val listStory = ArrayList<ListStory>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder = MainViewHolder(
        CardItemStoryBinding.inflate(LayoutInflater.from(context), parent, false)
    )

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(listStory[position])
    }

    override fun getItemCount(): Int = listStory.size

    @SuppressLint("NotifyDataSetChanged")
    fun setStory(list: List<ListStory>) {
        listStory.clear()
        listStory.addAll(list)
        notifyDataSetChanged()
    }

    inner class MainViewHolder(private val binding: CardItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(listStory: ListStory) {
            with(binding) {
                tvCardUsername.text = listStory.name
                tvCardDesc.text = listStory.description
                Glide.with(context)
                    .load(listStory.photoUrl)
                    .into(ivCardPhoto)
                //click listener here..
            }
        }
    }

    interface OnItemClickAdapter {
        fun onItemClicked(listStory: ListStory, optionsCompat: ActivityOptionsCompat)
    }
}