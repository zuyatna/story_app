package com.zuyatna.storyapp.ui.view.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.zuyatna.storyapp.databinding.FragmentStoryBinding
import com.zuyatna.storyapp.manager.PreferenceManager
import com.zuyatna.storyapp.ui.adapter.LoadingStateAdapter
import com.zuyatna.storyapp.ui.adapter.MainAdapter
import com.zuyatna.storyapp.ui.view.UploadStoryActivity
import com.zuyatna.storyapp.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@ExperimentalPagingApi
class StoryFragment : Fragment() {
    private lateinit var preferenceManager: PreferenceManager
    private lateinit var mainAdapter: MainAdapter

    private var _binding: FragmentStoryBinding? = null

    private val binding get() = _binding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStoryBinding.inflate(LayoutInflater.from(requireActivity()))
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preferenceManager = PreferenceManager(requireContext())
        mainAdapter = MainAdapter()

//        (activity as AppCompatActivity).apply {
//            setSupportActionBar(binding?.toolbar)
//            supportActionBar?.title = resources.getString(R.string.story_app)
//        }

        binding?.toolbar?.apply {
            setTitleTextColor(Color.WHITE)
            setSubtitleTextColor(Color.WHITE)
        }

        setHasOptionsMenu(true)
        setPagingStory()
        getStory()

        binding?.fabUploadStory?.setOnClickListener {
            startActivity(Intent(requireContext(), UploadStoryActivity::class.java))
        }
    }

    private fun getStory() {
        viewModel.getStories(preferenceManager.userToken).observe(viewLifecycleOwner) {
            mainAdapter.submitData(lifecycle, it)
        }
    }

    private fun setPagingStory() {
        binding?.apply {
            rvStory.setHasFixedSize(true)
            rvStory.layoutManager = LinearLayoutManager(requireContext())
            rvStory.adapter = mainAdapter.withLoadStateHeaderAndFooter(
                header = LoadingStateAdapter { mainAdapter.retry() },
                footer = LoadingStateAdapter { mainAdapter.retry() }
            )

            swlStory.setOnRefreshListener {
                mainAdapter.refresh()
                swlStory.isRefreshing = false
                rvStory.visibility = View.GONE
            }

            btnTry.setOnClickListener {
                mainAdapter.retry()
            }
        }

        mainAdapter.addLoadStateListener {
            binding?.apply {
                rvStory.isVisible = it.source.refresh is LoadState.NotLoading
                pbMain.isVisible = it.source.refresh is LoadState.Loading
                btnTry.isVisible = it.source.refresh is LoadState.Error
                tvError.isVisible = it.source.refresh is LoadState.Error

                if (it.source.refresh is LoadState.NotLoading && it.append.endOfPaginationReached && mainAdapter.itemCount < 1) {
                    rvStory.isVisible = false
                    tvError.isVisible = true
                } else {
                    rvStory.isVisible = true
                    tvError.isVisible = false
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}