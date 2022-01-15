package com.example.scrolly.feature_timeline

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.scrolly.R
import com.example.scrolly.databinding.FragmentTimelineBinding
import com.example.scrolly.databinding.TimelineListItemBinding
import com.example.scrolly.models.Like
import com.example.scrolly.models.Post
import com.example.scrolly.models.Posts
import com.example.scrolly.utils.getTimeAgo
import com.example.scrolly.utils.showSnackBar
import kotlinx.coroutines.launch

private const val TAG = "TimelineFragment"
class TimelineFragment : Fragment() {

    private val timeLineViewModel: TimelineViewModel by activityViewModels()

    private lateinit var binding: FragmentTimelineBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTimelineBinding.inflate(layoutInflater)

//        if (!timeLineViewModel.isUserLoggedIn()){
//            findNavController().navigate(R.id.action_timelineFragment_to_loginFragment)
//        }

        setLayoutManger()
        observePosts()
        return binding.root
    }

    private fun setLayoutManger(){
        binding.timelineRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observePosts(){
        lifecycleScope.launch {
            var posts = mutableListOf<Posts>()
            timeLineViewModel.getPosts().observe(viewLifecycleOwner, { postsList ->
                postsList.forEach{
                    lifecycleScope.launch {
                        val post = Posts()
                        post.post = it
                        post.user = timeLineViewModel.getUserInfo(it.userId)
                        posts.add(post)
                        if (posts.isNotEmpty()){
                            binding.progressBar2.visibility = View.GONE
                        }
                        binding.timelineRecyclerView.adapter = TimeLineAdapter(posts ?: emptyList())
                        Log.d(TAG, "observePosts: $posts")
                    }
                }
            })
        }
    }

    var t = false
    private inner class TimeLineViewHolder(val binding: TimelineListItemBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            binding.likeImageBtn.setOnClickListener(this)
        }
        var post = Post()
        fun bind(post: Posts) {
            this.post = post.post
            binding.postMsgxtView.text = post.post.postMessage.toString()
            binding.usernameTxtView.text = post.user.userName
            binding.profileImgImgView.load(post.user.profileImgUrl)
            binding.periodTxtView.text = post.post.timestamp?.getTimeAgo()
            binding.likeCountTxtView.text = post.post.likes.count().toString()
            if (post.post.postImageUrl.isNullOrBlank()){
                binding.postImgImgView.visibility = View.GONE
            }else{
                binding.postImgImgView.visibility = View.VISIBLE
                binding.postImgImgView.load(post.post.postImageUrl)
            }
            if (post.post.likes.contains(Like(timeLineViewModel.currentUserId() ?: ""))){
                binding.likeImageBtn.isChecked = true
            }
        }

        override fun onClick(v: View?) {
            when(v){
                binding.likeImageBtn -> {
                    if (timeLineViewModel.isUserLoggedIn()){
                        if (binding.likeImageBtn.isChecked){
                            timeLineViewModel.addLike(post.id)
                            binding.likeCountTxtView.text = (post.likes.count() + 1).toString()
                        }else{
                            timeLineViewModel.deleteLike(post.id)
                            post.likes.remove(Like(timeLineViewModel.currentUserId() ?: ""))
                            binding.likeCountTxtView.text = when(post.likes.count()){
                                0 -> "0"
                                else -> post.likes.count().toString()
                            }
                        }
                    }else{
                        binding.likeImageBtn.isChecked = false
                        showSnackBar(requireView(), resources.getString(R.string.login_to_use_msg), false)
                    }
                }
            }
        }
    }

    private inner class TimeLineAdapter(val posts: List<Posts>) :
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