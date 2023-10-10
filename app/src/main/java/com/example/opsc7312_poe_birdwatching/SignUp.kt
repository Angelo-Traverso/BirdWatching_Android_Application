package com.example.opsc7312_poe_birdwatching

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import com.example.opsc7312_poe_birdwatching.Models.UsersModel
import com.google.android.material.textfield.TextInputEditText

class SignUp : Fragment() {

    private lateinit var nameInput: TextInputEditText
    //private lateinit var usernameInput: EditText
    private lateinit var passwordInput: TextInputEditText
    private lateinit var confirmPasswordInput: TextInputEditText
    private lateinit var btnSignUp: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nameInput = view.findViewById(R.id.txtUserName)
        passwordInput = view.findViewById(R.id.txtUserPassword)
        confirmPasswordInput = view.findViewById(R.id.txtUserConfirmPassword)
        btnSignUp = view.findViewById(R.id.btnSignUp)

        btnSignUp.setOnClickListener() {
            if (validateForm()) {
                RegisterUser()
                intentToSignIn()
            }
        }
    }

    //============================================================================
    // Take user inputs and create new user instance
    private fun RegisterUser() {
        try {

            val newUser = UsersModel(Name = nameInput.text.toString().trim(), Hash = PasswordHandler.hashPassword(passwordInput.text.toString().trim()))

            // Add user to database
            ToolBox.users.add(newUser)

            val toast = Toast.makeText(requireContext(), "Account created", Toast.LENGTH_SHORT)
            toast.show()
        } catch (ex: java.lang.Exception) {
            Log.w("log", ex.toString())
            ex.printStackTrace()
        }
    }

    //============================================================================
    // Method to start intent activity to sign in
    private fun intentToSignIn() {
        try {
            val intent = Intent(requireContext(), MainActivity::class.java)
            val options = ActivityOptionsCompat.makeCustomAnimation(requireContext(), 0, 0)
            ActivityCompat.startActivity(requireContext(), intent, options.toBundle())
        } catch (ex: java.lang.Exception) {
            Log.w("log", ex.toString())
            ex.printStackTrace()
        }
    }

    //============================================================================
    // Ensure user has inputted valid data
    private fun validateForm(): Boolean {
        var valid = true
        try {
            val name: String = nameInput.text.toString().trim()
//            val surname: String = surnameInput.text.toString().trim()
//            val username: String = usernameInput.text.toString().trim()
            val password: String = passwordInput.text.toString().trim()
            val confirmPassword: String = confirmPasswordInput.text.toString().trim()

            val minLength = 8
            val maxLength = 50
            val hasUpperCase = "[A-Z]".toRegex().containsMatchIn(password)
            val hasLowerCase = "[a-z]".toRegex().containsMatchIn(password)
            val hasDigit = "\\d".toRegex().containsMatchIn(password)
            val hasSpecialChar = "[^A-Za-z0-9]".toRegex().containsMatchIn(password)

            if (TextUtils.isEmpty(name)) {
                nameInput.error = "Name is required"
                valid = false
            }
//            if (TextUtils.isEmpty(surname)) {
//                surnameInput.error = "Surname is required"
//                valid = false
//            }
//            if (TextUtils.isEmpty(username)) {
//                usernameInput.error = ("Username is required")
//                valid = false
//            }
//            if (doesUsernameExist((username))) {
//                usernameInput.error = ("Username already exists")
//                valid = false
//            }

            if (TextUtils.isEmpty(password)) {
                passwordInput.error = ("Password is required")
                valid = false
            }
            if (TextUtils.isEmpty(confirmPassword)) {
                confirmPasswordInput.error = ("Confirm password is required")
                valid = false
            }
            if (!TextUtils.equals(password, confirmPassword)) {
                confirmPasswordInput.error = ("Passwords must match")
                valid = false
            }
            if (!(password.length in minLength..maxLength &&
                        hasUpperCase &&
                        hasLowerCase &&
                        hasDigit &&
                        hasSpecialChar)
            ) {
                passwordInput.error = ("Password is not strong enough.")
                valid = false
            }


            return valid
        } catch (ex: java.lang.Exception) {
            Log.w("log", ex.toString())
            ex.printStackTrace()
            return false
        }
    }

//    //============================================================================
//    // Try find if the existing username exists
//    private fun doesUsernameExist(NameToFind: String): Boolean {
//        try {
//            val person = ToolBox.users.find { it.UserUsername == NameToFind }
//            return person != null
//        } catch (ex: java.lang.Exception) {
//            Log.w("log", ex.toString())
//            ex.printStackTrace()
//            return true
//        }
//    }
}