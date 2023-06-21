package com.dicoding.submissionone.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.dicoding.submissionone.databinding.ActivityMainBinding
import com.dicoding.submissionone.ui.login.LoginActivity
import com.dicoding.submissionone.utils.SharedPref
import com.dicoding.submissionone.utils.ViewModelFactory

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var viewModelFactory: ViewModelFactory
    private lateinit var sharedPref: SharedPref
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = SharedPref(this)

        tokenCheck()
    }

    private fun tokenCheck() {
        if (sharedPref.getToken() == null) Intent(this, LoginActivity::class.java).apply {
            startActivity(this)
            finish()
        } else{
            Toast.makeText(this, sharedPref.getToken().toString(), Toast.LENGTH_SHORT).show()
        }
    }
}