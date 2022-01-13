package com.example.scrolly.feature_timeline

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.scrolly.R
import com.example.scrolly.databinding.FragmentTimelineBinding
import com.example.scrolly.databinding.TimelineListItemBinding
import com.example.scrolly.models.Post

class TimelineFragment : Fragment() {

    private val timeLineViewModel by lazy {
        ViewModelProvider(this)[TimelineViewModel::class.java]
    }

    private lateinit var binding: FragmentTimelineBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTimelineBinding.inflate(layoutInflater)

        binding.timelineRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = TimeLineAdapter(timeLineViewModel.dummyPosts)
        }
        return binding.root
    }


    var t = false
    private inner class TimeLineViewHolder(val binding: TimelineListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(post: Post) {
            binding.postMsgxtView.text = post.postMessage.toString()
            binding.usernameTxtView.text = "test"
            binding.periodTxtView.text = "2 days ago"
            binding.likeCountTxtView.text = "22"
            binding.likeImageBtn.setOnClickListener{
                findNavController().navigate(R.id.action_timelineFragment_to_loginFragment)

            }
            if (t) binding.postImgImgView.visibility = View.GONE

            else binding.postImgImgView.visibility = View.VISIBLE
            t = !t
        }
    }

    private inner class TimeLineAdapter(val posts: List<Post>) :
        RecyclerView.Adapter<TimeLineViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeLineViewHolder {
            val binding = TimelineListItemBinding.inflate(
                layoutInflater,
                parent,
                false
            )
            return TimeLineViewHolder(binding)
        }

        override fun onBindViewHolder(holder: TimeLineViewHolder, position: Int) {
            val post = posts[position]
            holder.bind(post)
        }

        override fun getItemCount(): Int = posts.size

    }
}