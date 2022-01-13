package com.example.scrolly.feature_timeline

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.scrolly.databinding.FragmentTimelineBinding
import com.example.scrolly.databinding.TimelineListItemBinding
import com.example.scrolly.models.Post
import com.example.scrolly.utils.getTimeAgo

private const val TAG = "TimelineFragment"
class TimelineFragment : Fragment() {

    private val timeLineViewModel: TimelineViewModel by activityViewModels()

    private lateinit var binding: FragmentTimelineBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTimelineBinding.inflate(layoutInflater)

        setLayoutManger()
        observePosts()
        return binding.root
    }

    private fun setLayoutManger(){
        binding.timelineRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observePosts(){
        timeLineViewModel.getPosts().observe(viewLifecycleOwner, {
            binding.timelineRecyclerView.adapter = TimeLineAdapter(it)
            Log.d(TAG, "observePosts: $it")
        })
    }

    var t = false
    private inner class TimeLineViewHolder(val binding: TimelineListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(post: Post) {
            binding.postMsgxtView.text = post.postMessage.toString()
            binding.usernameTxtView.text = post.postMessage
            binding.periodTxtView.text = post.timestamp?.getTimeAgo()
            binding.likeCountTxtView.text = post.likes.toString()
            if (post.postImageUrl.isNullOrBlank()){
                binding.postImgImgView.visibility = View.GONE
            }else{
                binding.postImgImgView.load(post.postImageUrl)
            }
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