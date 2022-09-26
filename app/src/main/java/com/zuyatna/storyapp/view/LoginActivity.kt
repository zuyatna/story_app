package com.zuyatna.storyapp.view

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import com.zuyatna.storyapp.R
import com.zuyatna.storyapp.databinding.ActivityLoginBinding
import com.zuyatna.storyapp.model.login.LoginModel
import com.zuyatna.storyapp.service.ApiConfig
import com.zuyatna.storyapp.utility.NetworkResult
import com.zuyatna.storyapp.viewmodel.LoginViewModel
import com.zuyatna.storyapp.viewmodel.LoginViewModelFactory
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    private lateinit var loginViewModel: LoginViewModel

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

        val login = LoginModel(ApiConfig.getInstance())
        loginViewModel = ViewModelProvider(this, LoginViewModelFactory(login))[LoginViewModel::class.java]
        postLoginForm()

        playPropertyAnimation()
        setLoginButtonEnable()

        binding.etLoginEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //before text changed code here..
            }

            override fun afterTextChanged(s: Editable?) {
                //after text changed code here..
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                completeEmail = !(!s.isNullOrEmpty() && !Patterns.EMAIL_ADDRESS.matcher(s).matches())
                setLoginButtonEnable()
            }
        })

        binding.etLoginPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //before text changed code here..
            }

            override fun afterTextChanged(s: Editable?) {
                //after text changed code here..
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                completePassword = !(!s.isNullOrEmpty() && s.length < 6)
                setLoginButtonEnable()
            }
        })
    }

    private fun playPropertyAnimation() {
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

                lifecycle.coroutineScope.launchWhenResumed {
                    if(registerJob.isActive) registerJob.cancel()
                    registerJob = launch {
                        loginViewModel.login(email, password).collect { result ->
                            when (result) {
                                is NetworkResult.Success -> {
                                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                                    Toast.makeText(this@LoginActivity, getString(R.string.successful_login), Toast.LENGTH_SHORT).show()
                                    finish()
                                }
                                is NetworkResult.Error -> {
                                    Toast.makeText(this@LoginActivity, getString(R.string.failed_login), Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}