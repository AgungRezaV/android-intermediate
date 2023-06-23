package com.dicoding.submissionone.ui.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.dicoding.submissionone.utils.Result.*
import com.dicoding.submissionone.databinding.ActivityRegisterBinding
import com.dicoding.submissionone.utils.ViewModelFactory

class RegisterActivity : AppCompatActivity() {
    lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel
    private lateinit var viewModelFactory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        viewModelFactory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[RegisterViewModel::class.java]

        binding.progressBar.visibility = View.GONE

        binding.btnRegister.setOnClickListener {
            register()
        }
    }

    private fun register() {
        binding.progressBar.visibility = View.VISIBLE

        val name = binding.editTextInputNama.text.toString()
        val email = binding.editTextInputEmailDaftar.text.toString()
        val password = binding.editTextInputPasswordDaftar.text.toString()

        when {
            name.isEmpty() -> {
                binding.editTextInputNama.error = "Masukkan Nama"
                binding.editTextInputNama.requestFocus()
            }
            email.isEmpty() -> {
                binding.editTextInputEmailDaftar.error = "Masukkan Email"
                binding.editTextInputEmailDaftar.requestFocus()
            }

            password.isEmpty() -> {
                binding.editTextInputPasswordDaftar.error = "Masukkan Password"
                binding.editTextInputPasswordDaftar.requestFocus()
            }

            else -> {
                viewModel.register(name, email, password).observe(this) {
                    when (it) {
                        is Success -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(this, it.data.message, Toast.LENGTH_SHORT).show()
                            finish()
                        }

                        is Loading -> binding.progressBar.visibility = View.VISIBLE
                        is Error -> {
                            Toast.makeText(this, it.error, Toast.LENGTH_SHORT).show()
                            binding.progressBar.visibility = View.GONE
                        }
                    }
                }
            }

        }
    }
}