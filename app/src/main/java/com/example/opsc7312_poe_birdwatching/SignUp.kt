//Project:
//Open Source Coding (Intermediate)
//Portfolio of evidence
//Task 2
//Authors:
//Jonathan Polakow, ST10081881
//Angelo Traverso, ST10081927

package com.example.opsc7312_poe_birdwatching

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import com.example.opsc7312_poe_birdwatching.Models.UsersModel
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class SignUp : Fragment() {

    private lateinit var nameInput: TextInputEditText
    private lateinit var surnameInput: TextInputEditText
    private lateinit var passwordInput: TextInputEditText
    private lateinit var confirmPasswordInput: TextInputEditText
    private lateinit var btnSignUp: Button
    private lateinit var emailInput: TextInputEditText

    //==============================================================================================
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    //==============================================================================================
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        FirebaseApp.initializeApp(requireContext())

        nameInput = view.findViewById(R.id.txtUserName)
        surnameInput = view.findViewById(R.id.tvSurname)
        passwordInput = view.findViewById(R.id.txtUserPassword)
        confirmPasswordInput = view.findViewById(R.id.txtUserConfirmPassword)
        btnSignUp = view.findViewById(R.id.btnSignUp)
        emailInput = view.findViewById(R.id.txtUserEmail)

        btnSignUp.setOnClickListener {
            if (validateForm()) {
                val auth = FirebaseAuth.getInstance()
                auth.createUserWithEmailAndPassword(
                    emailInput.text.toString(),
                    passwordInput.text.toString()
                )
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            storeUserDataInFireStore()
                            intentToSignIn()
                        } else {
                            // Registration failed, handle the error
                            Toast.makeText(
                                requireContext(),
                                "Registration failed: ${task.exception?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }
    }

    //==============================================================================================
    // Storing new user data in database
    private fun storeUserDataInFireStore() {
        // Check if the user is authenticated
        val currentUser = Firebase.auth.currentUser
        if (currentUser != null) {
            try {
                val db = FirebaseFirestore.getInstance()
                val usersCollection = db.collection("users")

                val newUser = UsersModel(
                    UserID = currentUser.uid,
                    Name = nameInput.text.toString().trim(),
                    Surname = surnameInput.text.toString().trim(),
                    isUnitKM = true,
                    MaxDistance = 5.0,
                    ChallengePoints = 0,
                    mapStyleIsDark = false
                )

                // Use the UID of the currently authenticated user for the document ID
                usersCollection.document(currentUser.uid)
                    .set(newUser)
                    .addOnSuccessListener { documentReference ->
                        // User data added to Firestore successfully
                        Toast.makeText(
                            requireContext(),
                            "Successfully signed up",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    .addOnFailureListener { e ->
                        // Handle the error if adding data to Firestore fails
                        Toast.makeText(
                            requireContext(),
                            "Failed to store user data in Firestore: ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            } catch (ex: Exception) {
                Log.e("log", "Error storing user data in Firestore: ${ex.message}")
                ex.printStackTrace()
            }
        } else {
            // Handle the case where the user is not authenticated
            Toast.makeText(
                requireContext(),
                "User is not authenticated. Please sign in or register.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    //==============================================================================================
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

    //==============================================================================================
    // Ensure user has inputted valid data
    private fun validateForm(): Boolean {
        var valid = true
        try {

            // A custom error message to prevent it from overlapping with the view password eye icon
            val customError = CustomErrorDrawable(requireContext())

            val name: String = nameInput.text.toString().trim()
            val surname: String = surnameInput.text.toString().trim()
            val email: String = emailInput.text.toString().trim()
            val password: String = passwordInput.text.toString().trim()
            val confirmPassword: String = confirmPasswordInput.text.toString().trim()

            // Validation constraints
            val minLength = 8
            val maxLength = 50
            val hasUpperCase = "[A-Z]".toRegex().containsMatchIn(password)
            val hasLowerCase = "[a-z]".toRegex().containsMatchIn(password)
            val hasDigit = "\\d".toRegex().containsMatchIn(password)
            val hasSpecialChar = "[^A-Za-z0-9]".toRegex().containsMatchIn(password)
            val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

            if (TextUtils.isEmpty(name)) {
                nameInput.setError("Name is required", customError)
                valid = false
            }
            if (TextUtils.isEmpty(surname)) {
                surnameInput.setError("Surname is required", customError)
            }
            if (TextUtils.isEmpty(email) || !emailRegex.matches(email)) {
                emailInput.setError("Please enter a valid email", customError)
                valid = false
            }
            if (TextUtils.isEmpty(password)) {
                passwordInput.setError("Password is required", customError)
                valid = false
            }
            if (TextUtils.isEmpty(confirmPassword)) {
                confirmPasswordInput.setError("Confirm password is required", customError)
                valid = false
            }
            if (!TextUtils.equals(password, confirmPassword)) {
                confirmPasswordInput.setError("Passwords must match", customError)
                valid = false
            }
            if (!(password.length in minLength..maxLength && hasUpperCase && hasLowerCase && hasDigit && hasSpecialChar)) {
                passwordInput.setError("Password is not strong enough", customError)
                valid = false
            }

            return valid
        } catch (ex: java.lang.Exception) {
            Log.w("log", ex.toString())
            ex.printStackTrace()
            return false
        }
    }
}