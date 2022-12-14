package com.zuyatna.storyapp.ui.view.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.zuyatna.storyapp.R
import com.zuyatna.storyapp.databinding.FragmentAccountBinding
import com.zuyatna.storyapp.manager.PreferenceManager
import com.zuyatna.storyapp.ui.view.LoginActivity

class AccountFragment : Fragment() {
    private lateinit var preferenceManager: PreferenceManager

    private var _binding: FragmentAccountBinding? = null

    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAccountBinding.inflate(LayoutInflater.from(requireActivity()))
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding?.toolbar)
            supportActionBar?.title = resources.getString(R.string.account)
        }

        binding?.toolbar?.apply {
            setTitleTextColor(Color.WHITE)
            setSubtitleTextColor(Color.WHITE)
        }

        preferenceManager = PreferenceManager(requireContext())

        binding?.apply {
            tvUsername.text = preferenceManager.username
        }

        binding?.logoutApp?.setOnClickListener {
            preferenceManager.clear()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            activity?.finish()
        }

        binding?.languageSetting?.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}