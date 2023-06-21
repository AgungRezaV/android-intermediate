package com.dicoding.submissionone.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.dicoding.submissionone.data.request.LoginRequest
import com.dicoding.submissionone.data.response.UserData
import com.dicoding.submissionone.databinding.ActivityLoginBinding
import com.dicoding.submissionone.ui.main.MainActivity
import com.dicoding.submissionone.ui.register.RegisterActivity
import com.dicoding.submissionone.utils.Result
import com.dicoding.submissionone.utils.SharedPref
import com.dicoding.submissionone.utils.ViewModelFactory

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var viewModelFactory: ViewModelFactory
    private lateinit var sharedPref: SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        viewModelFactory = ViewModelFactory.getInstance(this)
        sharedPref = SharedPref(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]

        binding.apply {
            progressBar.visibility = View.GONE
            tvNotif.visibility = View.GONE
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.editTextInputEmail.text.toString()
            val password = binding.editTextInputPassword.text.toString()

            when {
                email.isEmpty() -> {
                    binding.editTextInputEmail.error = "Masukkan Email"
                    binding.editTextInputEmail.requestFocus()
                }
                password.isEmpty() -> {
                    binding.editTextInputPassword.error = "Masukkan Password"
                    binding.editTextInputPassword.requestFocus()
                }
                password.length < 8 -> {
                    binding.editTextInputPassword.error =
                        "Password harus terdiri dari setidaknya 8 karakter"
                    binding.editTextInputPassword.requestFocus()
                }
                else -> {
                    binding.progressBar.visibility = View.VISIBLE

                    login(email, password)
                }
            }
        }

        binding.btnDaftar.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        playAnimation()
    }

    //Property Animation
    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.textViewNamaLogin, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val animasiLogo = ObjectAnimator.ofFloat(binding.ivLogoApp, View.ALPHA, 2F).setDuration(1000)
        val animasiEtEmail = ObjectAnimator.ofFloat(binding.editTextInputEmail, View.ALPHA, 1f).setDuration(750)
        val animasiEtPassword = ObjectAnimator.ofFloat(binding.editTextInputPassword, View.ALPHA, 1f).setDuration(750)
        val animasiBtnLogin = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(500)
        val animasiTvDecor = ObjectAnimator.ofFloat(binding.textDecorOne, View.ALPHA, 1f).setDuration(500)
        val animasiBtnDaftar = ObjectAnimator.ofFloat(binding.btnDaftar, View.ALPHA, 1F).setDuration(500)

        AnimatorSet().apply {
            playSequentially(animasiLogo, animasiEtEmail, animasiEtPassword, animasiBtnLogin, animasiTvDecor, animasiBtnDaftar)
            start()
        }
    }

    //Login
    private fun login(x: String, y: String) {
        binding.progressBar.visibility = View.VISIBLE

        LoginRequest(binding.editTextInputEmail.text.toString(), binding.editTextInputPassword.text.toString())
        viewModel.login(x, y).observe(this) {
            when (it) {
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE

                    val response = it.data
                    saveUserData(
                        UserData(
                            response.loginResult.name,
                            response.loginResult.token,
                            true
                        )
                    )

                    sharedPref.saveToken(response.loginResult.token)
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                is Result.Loading -> binding.progressBar.visibility = View.VISIBLE
                is Result.Error -> {
                    binding.tvNotif.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }

    private fun saveUserData(userData: UserData) {
        viewModel.saveUser(userData)
    }
}