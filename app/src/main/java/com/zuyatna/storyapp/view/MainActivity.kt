package com.zuyatna.storyapp.view

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.zuyatna.storyapp.R
import com.zuyatna.storyapp.adapter.MainAdapter
import com.zuyatna.storyapp.databinding.ActivityMainBinding
import com.zuyatna.storyapp.manager.PreferenceManager
import com.zuyatna.storyapp.model.main.ListStory
import com.zuyatna.storyapp.model.main.MainModel
import com.zuyatna.storyapp.service.ApiConfig
import com.zuyatna.storyapp.utility.NetworkResult
import com.zuyatna.storyapp.viewmodel.MainViewModel
import com.zuyatna.storyapp.viewmodel.MainViewModelFactory

class MainActivity : AppCompatActivity(), MainAdapter.OnItemClickAdapter {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var preferenceManager: PreferenceManager
    private lateinit var mainAdapter: MainAdapter
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        preferenceManager = PreferenceManager(this)
        mainAdapter = MainAdapter(this, this)

        val story = MainModel(ApiConfig.getInstance())
        mainViewModel = ViewModelProvider(this, MainViewModelFactory(story))[MainViewModel::class.java]
        fetchData(preferenceManager.userToken)

        binding.srlMain.setOnRefreshListener {
            fetchData(preferenceManager.userToken)
        }
    }


    private fun fetchData(userAuth: String) {
        binding.rvMain.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = mainAdapter
        }

        mainViewModel.apply {
            fetchListStory(userAuth)
            dataListStory.observe(this@MainActivity) {
                when (it) {
                    is NetworkResult.Success -> {
                        if (it.data?.listStory != null) {
                            mainAdapter.setStory(it.data.listStory)
                        } else {
                            Toast.makeText(this@MainActivity, getString(R.string.no_data_story), Toast.LENGTH_SHORT).show()
                        }
                    }
                    is NetworkResult.Error -> {
                        Toast.makeText(this@MainActivity, getString(R.string.error_data_story), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_item, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.language -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }
            R.id.logout -> {
                preferenceManager.clearToken()
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onItemClicked(listStory: ListStory, optionsCompat: ActivityOptionsCompat) {
        //clicked item here..
    }
}