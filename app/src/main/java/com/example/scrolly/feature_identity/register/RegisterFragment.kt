package com.example.scrolly.feature_identity.register

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.scrolly.R
import com.example.scrolly.databinding.FragmentLoginBinding
import com.example.scrolly.databinding.FragmentRegisterBinding


class RegisterFragment : Fragment() {
    private lateinit var binding:FragmentRegisterBinding
    private val registerViewModel:RegisterViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val dialog = setProgressDialog(requireContext(), "Loading..")

        binding= FragmentRegisterBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.goToLoginTextView.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        binding.registerButton.setOnClickListener {
            val registerEmail = binding.rEmailTextfield.editText?.text.toString()

            val registerUsername = binding.rUsernameTextfield.editText?.text.toString()
            val registerPassword = binding.rPasswordTextfield.editText?.text.toString()

            if (registerPassword.isNotBlank()  && registerEmail.isNotBlank() && registerUsername.isNotBlank())  {
                registerViewModel.signUp(registerEmail,registerPassword,registerUsername)

            }else{
                checkFields(registerEmail,registerPassword,registerUsername)
            }

        }

        registerViewModel.signUpLiveData.observe(viewLifecycleOwner,{
            it?.let{
                Toast.makeText(requireActivity(),"success" , Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                registerViewModel.signUpLiveData.postValue(null)
        }
        })
        registerViewModel.signUpErrorLiveData.observe(viewLifecycleOwner,{
            it?.let {
                Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
                registerViewModel.signUpErrorLiveData.postValue(null)
            }

        })




    }

    fun setProgressDialog(context: Context, message:String): AlertDialog {
        val llPadding = 30
        val ll = LinearLayout(context)
        ll.orientation = LinearLayout.HORIZONTAL
        ll.setPadding(llPadding, llPadding, llPadding, llPadding)
        ll.gravity = Gravity.CENTER
        var llParam = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT)
        llParam.gravity = Gravity.CENTER
        ll.layoutParams = llParam

        val progressBar = ProgressBar(context)
        progressBar.isIndeterminate = true
        progressBar.setPadding(0, 0, llPadding, 0)
        progressBar.layoutParams = llParam

        llParam = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)
        llParam.gravity = Gravity.CENTER
        val tvText = TextView(context)
        tvText.text = message
        tvText.setTextColor(Color.parseColor("#000000"))
        tvText.textSize = 20.toFloat()
        tvText.layoutParams = llParam

        ll.addView(progressBar)
        ll.addView(tvText)

        val builder = AlertDialog.Builder(context)
        builder.setCancelable(true)
        builder.setView(ll)

        val dialog = builder.create()
        val window = dialog.window
        if (window != null) {
            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(dialog.window?.attributes)
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
            dialog.window?.attributes = layoutParams
        }
        return dialog
}
    private fun checkFields(
        fullName: String,
        email: String,
        password: String
    ): Boolean {
        var state = true
        val emailLayout = binding.rEmailTextfield
        val fullNameLayout = binding.rUsernameTextfield
        val passwordLayout = binding.rPasswordTextfield

        emailLayout.error = null
        fullNameLayout.error = null
        passwordLayout.error = null


        // Get needed string messages from strings.xml resource
        val require = getString(R.string.require)
        val wrongEmailFormat = getString(R.string.emailcheck)

        if (fullName.isBlank()) {
            fullNameLayout.error = require
            state = false
        }

        if (email.isBlank()) {
            emailLayout.error = require
            state = false}
//        } else if (!validator.emailIsValid(email)) {
//            emailLayout.error = wrongEmailFormat
//            state = false
//        }

        if (password.isBlank()) {
            passwordLayout.error = require
            state = false}
//        } else if (!validator.passIsValid(password)) {
//            passwordLayout.error = passwordConditions
//            state = false
//        }


        return state
    }

}