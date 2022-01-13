package com.example.scrolly.feature_identity.login

import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.scrolly.R
import com.example.scrolly.databinding.FragmentLoginBinding
import com.example.scrolly.util.RegisterValidation
import com.google.firebase.auth.FirebaseAuth


class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val loginViewModel: LoginViewMode by activityViewModels()
    //private lateinit var progressDialog: ProgressDialog

    private lateinit var firebaseAuth: FirebaseAuth
//


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        firebaseAuth = FirebaseAuth.getInstance()



        binding= FragmentLoginBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()

        binding.goToRegisTextView.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.loginButton.setOnClickListener {
            val dialog = setProgressDialog(requireContext(), "Loading..")
            val emailAddress = binding.emailTextfield.editText?.text.toString()
            val password = binding.passwordTextfield.editText?.text.toString()

            if(emailAddress.isNotBlank() && password.isNotBlank()){
                dialog.show()
                loginViewModel.login(emailAddress,password)
            }
            else{
                checkFields(emailAddress,password)

            }
        }





        binding.goToRegisTextView2.setOnClickListener {

           showdialog()
        }

        loginViewModel.loginLiveData.observe(viewLifecycleOwner, { email ->
            email?.let {

                //progressDialog.dismiss()
                Toast.makeText(requireActivity(), "login successfully", Toast.LENGTH_SHORT).show()


                loginViewModel.loginLiveData.postValue(null)
                //checkLoggedInState()
                findNavController().navigate(R.id.action_loginFragment_to_timelineFragment)

            }
        })

        loginViewModel.loginErrorLiveData.observe(viewLifecycleOwner, {
            val dialog = setProgressDialog(requireContext(), "Loading..")
            it?.let {
                dialog.hide()
                Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
                loginViewModel.loginErrorLiveData.postValue(null)
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
        email: String,
        password: String,
    ): Boolean {
        var state = true
        val emailLayout = binding.emailTextfield
        val passwordLayout = binding.passwordTextfield

        emailLayout.error = null

        passwordLayout.error = null

        // Get needed string messages from strings.xml resource
        val require = "required!"

        if (email.isBlank()) {
            emailLayout.error = require
            state = false
        }

        if (password.isBlank()) {
            passwordLayout.error = require
            state = false
        }


        return state
    }

    fun showdialog(){
        val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(requireActivity())
        builder.setTitle("Title")

// Set up the input
        val input = EditText(requireContext())
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setHint("Enter you email")
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

// Set up the buttons
        builder.setPositiveButton("Send Password", DialogInterface.OnClickListener { dialog, which ->
            // Here you get get input text from the Edittext
            FirebaseAuth.getInstance().sendPasswordResetEmail(input.text.toString()).addOnCompleteListener {
                if (it.isSuccessful)
                {
                    Toast.makeText(requireActivity(), "Password Sent Successfully", Toast.LENGTH_SHORT).show()
                }
                else
                {
                    Toast.makeText(requireActivity(), it.exception.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        })
        builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })

        builder.show()
    }
}