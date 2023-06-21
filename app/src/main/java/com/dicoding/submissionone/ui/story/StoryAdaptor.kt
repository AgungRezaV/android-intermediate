package com.dicoding.submissionone.ui.story


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.submissionone.databinding.ListStoryBinding


class StoryAdaptor : PagingDataAdapter<ListStory, StoryAdaptor.StoryViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val view = ListStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    class StoryViewHolder(private val binding: ListStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(story: ListStory) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(story.photoUrl)
                    .into(imgHero)
                tvName.text = story.name
                tvCreated.text = story.createdAt?.removeRange(16, story.createdAt!!.length)
                tvDesc.text = story.description
            }
            itemView.setOnClickListener {
//                val intent = Intent(itemView.context, DetilStoryActivity::class.java).apply {
//                    putExtra(DetilStoryActivity.EXTRA_NAME, story)
//                }
//                val optionsCompat: ActivityOptionsCompat =
//                    ActivityOptionsCompat.makeSceneTransitionAnimation(
//                        itemView.context as Activity,
//                        Pair(binding.imgHero, "image"),
//                        Pair(binding.tvName, "name"),
//                        Pair(binding.tvDesc, "deskripsi"),
//                        Pair(binding.tvCreated, "created")
//                    )
//                it.context.startActivity(intent, optionsCompat.toBundle())
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStory>() {
            override fun areItemsTheSame(oldItem: ListStory, newItem: ListStory): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListStory, newItem: ListStory): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}