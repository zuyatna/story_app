package com.zuyatna.storyapp.ui.view

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.coroutineScope
import com.zuyatna.storyapp.R
import com.zuyatna.storyapp.databinding.ActivityLoginBinding
import com.zuyatna.storyapp.manager.PreferenceManager
import com.zuyatna.storyapp.ui.viewmodel.LoginViewModel
import com.zuyatna.storyapp.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    private lateinit var preferenceManager: PreferenceManager

    private val loginViewModel: LoginViewModel by viewModels()

    private var completeEmail = false
    private var completePassword = false
    private var registerJob: Job = Job()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.tvLoginRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        postLoginForm()

        preferenceManager = PreferenceManager(this)

        playPropertyAnimation()
        setLoginButtonEnable()

        binding.etLoginEmail.addTextChangedListener(onTextChanged = { s, _, _, _ ->
            completeEmail = !(!s.isNullOrEmpty() && !Patterns.EMAIL_ADDRESS.matcher(s).matches())
            setLoginButtonEnable()
        })

        binding.etLoginPassword.addTextChangedListener(onTextChanged = { s, _, _, _ ->
            completePassword = !s.isNullOrEmpty() && s.length >= 6
            setLoginButtonEnable()
        })
    }

    private fun playPropertyAnimation() {
        ObjectAnimator.ofFloat(binding.ivLogin, View.TRANSLATION_X, -40f, 40f).apply {
            duration = 5000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.tvLoginTitle, View.ALPHA, 1f).setDuration(500)
        val etEmail = ObjectAnimator.ofFloat(binding.etLoginEmail, View.ALPHA, 1f).setDuration(500)
        val etPassword = ObjectAnimator.ofFloat(binding.etLoginPassword, View.ALPHA, 1f).setDuration(500)
        val btLogin = ObjectAnimator.ofFloat(binding.btLogin, View.ALPHA, 1f).setDuration(500)
        val tvHaveNotAccount = ObjectAnimator.ofFloat(binding.tvHaveNotAccount, View.ALPHA, 1f).setDuration(4000)
        val tvRegister = ObjectAnimator.ofFloat(binding.tvLoginRegister, View.ALPHA, 1f).setDuration(4000)

        AnimatorSet().apply {
            playSequentially(title, etEmail, etPassword, btLogin)
            playTogether(tvHaveNotAccount, tvRegister)
            start()
        }
    }

    private fun setLoginButtonEnable() {
        val etEmail = binding.etLoginEmail
        val etPassword = binding.etLoginPassword
        val btLogin = binding.btLogin

        if (etEmail.text.toString().isEmpty() && etPassword.text.toString().isEmpty()) {
            btLogin.isEnabled = false
        } else {
            btLogin.isEnabled = completeEmail && completePassword
        }
    }

    private fun postLoginForm() {
        binding.apply {
            btLogin.setOnClickListener {
                val email = binding.etLoginEmail.text.toString().trim()
                val password = binding.etLoginPassword.text.toString().trim()

                setProgressBar(true)

                lifecycle.coroutineScope.launchWhenResumed {
                    if(registerJob.isActive) registerJob.cancel()
                    registerJob = launch {
                        loginViewModel.login(email, password).collect { result ->
                            when (result) {
                                is NetworkResult.Success -> {
                                    preferenceManager.isUserLogin = !result.data?.error!!
                                    preferenceManager.userToken = result.data.loginModel.token

                                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                                    Toast.makeText(this@LoginActivity, getString(R.string.successful_login), Toast.LENGTH_SHORT).show()
                                    finish()
                                }
                                is NetworkResult.Error -> {
                                    Toast.makeText(this@LoginActivity, getString(R.string.failed_login), Toast.LENGTH_SHORT).show()
                                    setProgressBar(false)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setProgressBar(loading: Boolean) {
        when(loading) {
            true -> {
                binding.btLogin.visibility = View.GONE
                binding.pbLogin.visibility = View.VISIBLE
            }
            false -> {
                binding.btLogin.visibility = View.VISIBLE
                binding.pbLogin.visibility = View.GONE
            }
        }
    }
}