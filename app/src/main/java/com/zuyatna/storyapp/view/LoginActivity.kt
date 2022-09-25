package com.zuyatna.storyapp.view

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.zuyatna.storyapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    var completeEmail = false
    var completePassword = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.hide()
        binding.tvLoginRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

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
        val btLogin = binding.btLogin
        btLogin.isEnabled = completeEmail && completePassword
    }
}