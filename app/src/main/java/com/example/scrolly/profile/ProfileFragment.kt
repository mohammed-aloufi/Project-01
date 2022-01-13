package com.example.scrolly.profile

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
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
import androidx.navigation.fragment.findNavController
import com.akexorcist.localizationactivity.core.LocalizationActivityDelegate
import com.example.scrolly.R
import com.example.scrolly.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import java.io.FileNotFoundException
import java.io.InputStream

private const val TAG = "ProfileFragment"
class ProfileFragment : Fragment() {
    private lateinit var binding:FragmentProfileBinding
    private val profileViewModel by activityViewModels<ProfileViewModel>()

    private val getPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {

    }
    private val openGalleryLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { imageUri ->
        imageUri?.let {
            profileViewModel.imageUri = imageUri
            updatePostImgUI()
            binding.saveChangesBtn.visibility = View.VISIBLE
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentProfileBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        FirebaseAuth.getInstance().currentUser?.let {

        }
        val localizationDelegate = LocalizationActivityDelegate(requireActivity())

        // set the language into Arabic when clicking on it
        binding.acivBotton.setOnClickListener {
            localizationDelegate.setLanguage(requireContext(),"ar")

        }
        // set the language into English when clicking on it
        binding.engButton.setOnClickListener{
            localizationDelegate.setLanguage(requireContext(),"en")

        }

        profileViewModel.getUser()?.let {
            Log.d(TAG, it.displayName.toString())
            binding.userIdTextView.text = it.displayName // username
            binding.emailTextView.text = it.email  // email address
        } ?: findNavController().navigate(R.id.action_profileFragment_to_loginFragment)

        binding.logoutButton.setOnClickListener {
            showAlert()
        }
        binding.profileImgView.setOnClickListener {
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
        binding.saveChangesBtn.setOnClickListener {
            profileViewModel.uploadProfileImage(FirebaseAuth.getInstance().currentUser?.uid!!, profileViewModel.imageUri!!)
        }
    }

    private fun showAlert(){
        /** pop up warning window for confirmation of logging out from account
         * if it's logged out, it will move to login page*/

        val aluilder = AlertDialog.Builder(requireContext())
        aluilder.setTitle(getString(R.string.logot_notice))
        aluilder.setMessage(getString(R.string.delete_message))
        aluilder.setPositiveButton(getString(R.string.yes)) { dialogInterface, which ->
            FirebaseAuth.getInstance().signOut()
            // to reset the shared preference state ti false value so it can be moved to login page
            findNavController().navigate(R.id.action_profileFragment_to_loginFragment)


        }
//            findNavController().popBackStack()

        aluilder.setNegativeButton(getString(R.string.no)) { dialogInterface, which ->

        }
        val theDialog: AlertDialog = aluilder.create()
        theDialog.setCancelable(false)
        theDialog.show()
    }

    private fun updatePostImgUI() {
        try {
            val imageStream: InputStream =
                requireContext().contentResolver.openInputStream(profileViewModel.imageUri!!)!!
            val selectedImageBitmap = BitmapFactory.decodeStream(imageStream)
            binding.profileImgView.setImageBitmap(selectedImageBitmap)
        } catch (e: FileNotFoundException) {
            Log.e(TAG, "updateVideoUI: ${e.printStackTrace()}")
        }
    }
}