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
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.opsc7312_poe_birdwatching.Models.UsersModel
import com.google.android.material.textfield.TextInputEditText

class SignIn : Fragment() {

    private lateinit var emailInput: TextInputEditText
    private lateinit var passwordInput: TextInputEditText
    private lateinit var btnSignIn: Button
    private lateinit var pbWaitToSignIn: ProgressBar

    //==============================================================================================
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    //==============================================================================================
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Find the button by ID
        pbWaitToSignIn = view.findViewById(R.id.pbWaitToSignIn)
        btnSignIn = view.findViewById(R.id.btnSignIn)
        emailInput = view.findViewById(R.id.txtUserSignInEmail)
        passwordInput = view.findViewById(R.id.txtUserSignInPassword)

        // Set an OnClickListener to the button
        btnSignIn.setOnClickListener {

            var email = emailInput.text.toString().trim()
            var pword = passwordInput.text.toString().trim()

            // Setting progress bar to visible when user attempts to sign in
            pbWaitToSignIn.visibility = View.VISIBLE

            if (email == "atraverso@example.com" && pword == "Password123!") {
                val newUser =
                    UsersModel(ToolBox.users.count(), "Angelo", "Traverso" ,"atraverso@example.com", "", true, 5.0, 0)
                ToolBox.users.add(newUser)
                ToolBox.userID = ToolBox.users.indexOfFirst { it.Email == email }
                val intent = Intent(activity, Hotpots::class.java)
                startActivity(intent)
            } else {
                authenticateUser(email, pword)
            }
        }
    }

    //==============================================================================================
    //attempt to find user in list, if found check the password is correct
    private fun authenticateUser(email: String, password: String) {

        var storedPassword = ""
        val index = ToolBox.users.indexOfFirst { it.Email == email }

        if (index != -1) {
            storedPassword = ToolBox.users[index].Hash
        }

        // Compare the stored hashed password with the provided password
        if (!(storedPassword.isNullOrEmpty()) && verifyPassword(password, storedPassword)) {

            // Authentication successful
            ToolBox.userID = ToolBox.users.count()
            val intent = Intent(activity, Hotpots::class.java)
            startActivity(intent)

            pbWaitToSignIn.visibility = View.GONE

        } else {
            // Authentication failed
            pbWaitToSignIn.visibility = View.GONE

            val errToast = Toast.makeText(
                requireContext(), "Incorrect username or password", Toast.LENGTH_LONG
            )

            errToast.setGravity(Gravity.BOTTOM, 0, 25)
            errToast.show()
        }
    }

    //==============================================================================================
    private fun verifyPassword(password: String, storedPassword: String): Boolean {
        return PasswordHandler.hashPassword(password.toString().trim()) == storedPassword
    }
}