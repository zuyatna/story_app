package com.zuyatna.storyapp.ui

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.zuyatna.storyapp.R
import com.zuyatna.storyapp.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private val binding: ActivityRegisterBinding by lazy {
        ActivityRegisterBinding.inflate(layoutInflater)
    }

    var completeUsername = false
    var completeEmail = false
    var completePassword = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.title = resources.getString(R.string.register)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.tvRegisterLogin.setOnClickListener {
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
                completeUsername = s.isNullOrEmpty()
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
                completePassword = !(!s.isNullOrEmpty() && s.length < 6)
                setRegisterButtonEnable()
            }
        })
    }

    private fun playPropertyAnimation() {
        val title = ObjectAnimator.ofFloat(binding.tvRegisterTitle, View.ALPHA, 1f).setDuration(500)
        val etUsername = ObjectAnimator.ofFloat(binding.etRegisterUsername, View.ALPHA, 1f).setDuration(500)
        val etEmail = ObjectAnimator.ofFloat(binding.etRegisterEmail, View.ALPHA, 1f).setDuration(500)
        val etPassword = ObjectAnimator.ofFloat(binding.etRegisterPassword, View.ALPHA, 1f).setDuration(500)
        val btRegister = ObjectAnimator.ofFloat(binding.btRegister, View.ALPHA, 1f).setDuration(500)
        val tvHaveAccount = ObjectAnimator.ofFloat(binding.tvHaveAccount, View.ALPHA, 1f).setDuration(4000)
        val tvLogin = ObjectAnimator.ofFloat(binding.tvRegisterLogin, View.ALPHA, 1f).setDuration(4000)

        AnimatorSet().apply {
            playSequentially(title, etUsername, etEmail, etPassword, btRegister)
            playTogether(tvHaveAccount, tvLogin)
            start()
        }
    }

    private fun setRegisterButtonEnable() {
        val btRegister = binding.btRegister
        btRegister.isEnabled = !completeUsername && completeEmail && completePassword
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}