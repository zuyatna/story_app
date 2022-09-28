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
import com.zuyatna.storyapp.api.ApiConfig
import com.zuyatna.storyapp.databinding.ActivityRegisterBinding
import com.zuyatna.storyapp.model.register.RegisterModel
import com.zuyatna.storyapp.utility.NetworkResult
import com.zuyatna.storyapp.viewmodel.RegisterViewModel
import com.zuyatna.storyapp.viewmodel.RegisterViewModelFactory
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class RegisterActivity : AppCompatActivity() {
    private val binding: ActivityRegisterBinding by lazy {
        ActivityRegisterBinding.inflate(layoutInflater)
    }

    private lateinit var registerViewModel: RegisterViewModel

    private var completeUsername = false
    private var completeEmail = false
    private var completePassword = false
    private var registerJob: Job = Job()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.title = resources.getString(R.string.registration)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val register = RegisterModel(ApiConfig.getInstance())
        registerViewModel = ViewModelProvider(this, RegisterViewModelFactory(register))[RegisterViewModel::class.java]
        postRegisterForm()

        binding.tvRegisterLoginBt.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        playPropertyAnimation()
        setRegisterButtonEnable()

        binding.etRegisterUsername.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //before text changed code here..
            }

            override fun afterTextChanged(s: Editable?) {
                //after text changed code here..
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                completeUsername = !s.isNullOrEmpty()
                setRegisterButtonEnable()
            }
        })

        binding.etRegisterEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //before text changed code here..
            }

            override fun afterTextChanged(s: Editable?) {
                //after text changed code here..
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                completeEmail = !(!s.isNullOrEmpty() && !Patterns.EMAIL_ADDRESS.matcher(s).matches())
                setRegisterButtonEnable()
            }
        })

        binding.etRegisterPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //before text changed code here..
            }

            override fun afterTextChanged(s: Editable?) {
                //after text changed code here..
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                completePassword = !s.isNullOrEmpty()
                setRegisterButtonEnable()
            }
        })
    }

    private fun playPropertyAnimation() {
        ObjectAnimator.ofFloat(binding.ivRegister, View.TRANSLATION_X, -40f, 40f).apply {
            duration = 5000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.tvRegisterTitle, View.ALPHA, 1f).setDuration(500)
        val etUsername = ObjectAnimator.ofFloat(binding.etRegisterUsername, View.ALPHA, 1f).setDuration(500)
        val etEmail = ObjectAnimator.ofFloat(binding.etRegisterEmail, View.ALPHA, 1f).setDuration(500)
        val etPassword = ObjectAnimator.ofFloat(binding.etRegisterPassword, View.ALPHA, 1f).setDuration(500)
        val btRegister = ObjectAnimator.ofFloat(binding.btRegister, View.ALPHA, 1f).setDuration(500)
        val tvHaveAccount = ObjectAnimator.ofFloat(binding.tvHaveAccount, View.ALPHA, 1f).setDuration(4000)
        val tvLogin = ObjectAnimator.ofFloat(binding.tvRegisterLoginBt, View.ALPHA, 1f).setDuration(4000)

        AnimatorSet().apply {
            playSequentially(title, etUsername, etEmail, etPassword, btRegister)
            playTogether(tvHaveAccount, tvLogin)
            start()
        }
    }

    private fun setRegisterButtonEnable() {
        val etUsername = binding.etRegisterUsername
        val etEmail = binding.etRegisterEmail
        val etPassword = binding.etRegisterPassword
        val btRegister = binding.btRegister

        if (etUsername.text.toString().isEmpty() && etEmail.text.toString().isEmpty() && etPassword.text.toString().isEmpty()) {
            btRegister.isEnabled = false
        } else {
            btRegister.isEnabled = completeUsername && completeEmail && completePassword
        }
    }

    private fun postRegisterForm() {
        binding.apply {
            btRegister.setOnClickListener {
                val username = binding.etRegisterUsername.text.toString().trim()
                val email = binding.etRegisterEmail.text.toString().trim()
                val password = binding.etRegisterPassword.text.toString().trim()

                setProgressBar(true)

                lifecycle.coroutineScope.launchWhenResumed {
                    if(registerJob.isActive) registerJob.cancel()
                    registerJob = launch {
                        registerViewModel.register(username, email, password).collect { result ->
                            when (result) {
                                is NetworkResult.Success -> {
                                    startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                                    Toast.makeText(this@RegisterActivity, getString(R.string.successful_registered), Toast.LENGTH_SHORT).show()
                                    finish()
                                }
                                is NetworkResult.Error -> {
                                    Toast.makeText(this@RegisterActivity, getString(R.string.failed_registered), Toast.LENGTH_SHORT).show()
                                    setProgressBar(false)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    private fun setProgressBar(loading: Boolean) {
        when(loading) {
            true -> {
                binding.btRegister.visibility = View.GONE
                binding.pbRegister.visibility = View.VISIBLE
            }
            false -> {
                binding.btRegister.visibility = View.VISIBLE
                binding.pbRegister.visibility = View.GONE
            }
        }
    }
}