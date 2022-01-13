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
import com.example.scrolly.databinding.FragmentAddPostBinding
import com.example.scrolly.models.Post
import com.example.scrolly.utils.showSnackBar
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
                val postMessage = binding.postMsgEditTxt.text.toString()
                if (postMessage.isNotBlank()) {
                    var post = Post()
                    post.postMessage = postMessage
                    binding.progressBar.visibility = View.VISIBLE
                    lifecycleScope.launch {
                        val isSuccessful = addPostViewModel.uploadPost(post, addPostViewModel.imageUri)
                        if (isSuccessful){
                            binding.progressBar.visibility = View.GONE
                            showSnackBar(requireView(), "Success", true)
                        }else{
                            binding.progressBar.visibility = View.GONE
                            showSnackBar(requireView(), "Unable to upload post.", true)
                        }
                    }
                }
            }
        }
    }

    private fun updatePostImgUI() {
        try {
            val imageStream: InputStream =
                requireContext().contentResolver.openInputStream(addPostViewModel.imageUri!!)!!
            val selectedImageBitmap = BitmapFactory.decodeStream(imageStream)
            binding.postImgImgView.setImageBitmap(selectedImageBitmap)
        } catch (e: FileNotFoundException) {
            Log.e(TAG, "updateVideoUI: ${e.printStackTrace()}")
        }
    }
}