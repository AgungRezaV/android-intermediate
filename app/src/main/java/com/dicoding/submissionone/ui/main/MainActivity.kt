package com.dicoding.submissionone.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.submissionone.R
import com.dicoding.submissionone.databinding.ActivityMainBinding
import com.dicoding.submissionone.ui.login.LoginActivity
import com.dicoding.submissionone.ui.story.LoadingStoryAdapter
import com.dicoding.submissionone.ui.story.StoryAdaptor
import com.dicoding.submissionone.ui.story.add.AddStoryActivity
import com.dicoding.submissionone.utils.SharedPref
import com.dicoding.submissionone.utils.VerticalSpaceItemDecoration
import com.dicoding.submissionone.utils.ViewModelFactory
import com.dicoding.submissionone.utils.dp

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var viewModelFactory: ViewModelFactory
    private lateinit var sharedPref: SharedPref
    private lateinit var adaptor: StoryAdaptor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Story Applications"

        viewModelFactory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[MainActivityViewModel::class.java]
        sharedPref = SharedPref(this)

        adaptor = StoryAdaptor()
        binding.apply {
            rvStory.layoutManager = LinearLayoutManager(this@MainActivity)
            rvStory.setHasFixedSize(true)
            rvStory.addItemDecoration(VerticalSpaceItemDecoration(16.dp))
        }

        tokenCheck()
    }

    private fun tokenCheck() {
        if (sharedPref.getToken() == null) Intent(this, LoginActivity::class.java).apply {
            startActivity(this)
            finish()
        } else{
            binding.rvStory.adapter = adaptor.withLoadStateFooter(
                footer = LoadingStoryAdapter { adaptor.retry() }
            )
            viewModel.getStory().observe(this@MainActivity) {
                adaptor.submitData(lifecycle, it)
            }

            binding.fabAddStory.setOnClickListener {
                startActivity(Intent(this, AddStoryActivity::class.java))
                finish()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_logout -> {
                sharedPref.clearToken()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
        return true
    }
}