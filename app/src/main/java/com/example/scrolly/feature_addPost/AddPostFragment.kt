package com.example.scrolly.feature_addPost

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import com.example.scrolly.R
import com.example.scrolly.databinding.FragmentAddPostBinding
import com.example.scrolly.utils.Constants
import java.io.FileNotFoundException
import java.io.InputStream

private const val TAG = "AddPostFragment"
class AddPostFragment : Fragment(), View.OnClickListener {

    private val addPostViewModel by lazy {
        ViewModelProvider(this)[AddPostViewModel::class.java]
    }

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
        savedInstanceState?.getString(Constants.IMAGE_URI_KEY)?.let { uri ->
            addPostViewModel.imageUri = uri.toUri()
            updatePostImgUI()
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        setOnClickListeners()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (addPostViewModel.imageUri != null){
            outState.putString(Constants.IMAGE_URI_KEY, addPostViewModel.imageUri.toString())
        }
    }

    private fun setOnClickListeners(){
        binding.uploadImgImgBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v){
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