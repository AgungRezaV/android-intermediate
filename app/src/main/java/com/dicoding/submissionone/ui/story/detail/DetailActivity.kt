package com.dicoding.submissionone.ui.story.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.dicoding.submissionone.databinding.ActivityDetailBinding
import com.dicoding.submissionone.ui.story.ListStory

class DetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val detailActivity = intent.getParcelableExtra<ListStory>(NAME) as ListStory

        supportActionBar?.title = "Detail ${detailActivity.name}"

        binding.apply {
            tvDetailNama.text = detailActivity.name
            tvDetailDeskripsi.text = detailActivity.description
            Glide.with(this@DetailActivity)
                .load(detailActivity.photoUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(ivDetailFoto)
        }
    }

    companion object {
        const val NAME = "name"
    }
}