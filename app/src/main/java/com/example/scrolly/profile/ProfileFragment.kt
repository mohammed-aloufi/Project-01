package com.example.scrolly.profile

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.akexorcist.localizationactivity.core.LocalizationActivityDelegate
import com.example.scrolly.R
import com.example.scrolly.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth

private const val TAG = "ProfileFragment"
class ProfileFragment : Fragment() {
    private lateinit var binding:FragmentProfileBinding




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

        FirebaseAuth.getInstance().currentUser?.let {

            Log.d(TAG, it.displayName.toString())
            binding.userIdTextView.text = it.displayName // username
            binding.emailTextView.text = it.email  // email address
        }?: findNavController().navigate(R.id.action_profileFragment_to_loginFragment)


        binding.logoutButton.setOnClickListener {
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




    }
}