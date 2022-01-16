package com.example.scrolly.feature_addPost

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.scrolly.R
import com.example.scrolly.databinding.FragmentAddPostBinding
import com.example.scrolly.models.Post
import com.example.scrolly.utils.showSnackBar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import java.io.FileNotFoundException
import java.io.InputStream

private const val TAG = "AddPostFragment"

class AddPostFragment : Fragment(), View.OnClickListener {

    private val addPostViewModel: AddPostViewModel by activityViewModels()
    private lateinit var binding: FragmentAddPostBinding

    private val getPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {

    }
    private val openGalleryLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { imageUri ->
        imageUri?.let {
            addPostViewModel.imageUri = imageUri
            updatePostImgUI()
//            binding.postImgImgView.requestLayout()
//            binding.postImgImgView.layoutParams.height = 500
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddPostBinding.inflate(layoutInflater)
        binding.cancelImgBtn.visibility = View.GONE
        if (addPostViewModel.isUserLoggedIn()){
            lifecycleScope.launch {
                val user = addPostViewModel.getUserInfo(FirebaseAuth.getInstance().currentUser!!.uid)
                binding.profileImgImgView.load(user?.profileImgUrl){
                    crossfade(200)
                }
            }
        }
        //TODO: save fragment content if user switch to other fragment
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        binding.uploadImgImgBtn.setOnClickListener(this)
        binding.sendPostBtn.setOnClickListener(this)
        binding.cancelImgBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.uploadImgImgBtn -> {
                if (PackageManager.PERMISSION_GRANTED != requireActivity().let {
                        ContextCompat.checkSelfPermission(
                            it,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                    }) {
                    getPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                } else {
                    openGalleryLauncher.launch("image/*")
                }
            }
            binding.sendPostBtn -> {
                if (addPostViewModel.isUserLoggedIn()){
                    val postMessage = binding.postMsgEditTxt.text.toString()
                    if (postMessage.isNotBlank()) {
                        var post = Post()
                        post.postMessage = postMessage
                        binding.progressBar.visibility = View.VISIBLE
                        lifecycleScope.launch {
                            val isSuccessful = addPostViewModel.uploadPost(post, addPostViewModel.imageUri)
                            if (isSuccessful){
                                binding.progressBar.visibility = View.GONE
                                showSnackBar(requireView(), resources.getString(R.string.success), true)
                                findNavController().navigate(R.id.action_addPostFragment_to_timelineFragment)
                            }else{
                                binding.progressBar.visibility = View.GONE
                                showSnackBar(requireView(), resources.getString(R.string.upload_post_error_msg), true)
                            }
                        }
                    }
                }else{
                    showSnackBar(requireView(), resources.getString(R.string.login_to_use_msg), false)
                }
            }
            binding.cancelImgBtn -> {
                binding.postMsgEditTxt.setText("")
                binding.postImgImgView.visibility = View.GONE
            }
        }
    }

    private fun updatePostImgUI() {
        try {
            binding.postImgImgView.visibility = View.VISIBLE
            val imageStream: InputStream =
                requireContext().contentResolver.openInputStream(addPostViewModel.imageUri!!)!!
            val selectedImageBitmap = BitmapFactory.decodeStream(imageStream)
            binding.postImgImgView.setImageBitmap(selectedImageBitmap)
        } catch (e: FileNotFoundException) {
            Log.e(TAG, "updateVideoUI: ${e.printStackTrace()}")
        }
    }
}