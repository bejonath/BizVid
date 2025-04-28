package com.example.project

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var registerButton: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)

        auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser
        if (currentUser != null) {
            // User is already signed in, go directly to MainActivity
            Log.d("LoginActivity", "User already logged in: ${currentUser.uid}")
            navigateToMain()
            return
        }

        // If user is not logged in, set the content view for login
        setContentView(R.layout.activity_login)

        emailEditText = findViewById(R.id.usernameEditText) // Treat as email
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)
        registerButton = findViewById(R.id.registerButton)

        loginButton.setOnClickListener {
            handleLogin()
        }

        registerButton.setOnClickListener {
            handleSignupNavigation()
        }
    }

    private fun handleLogin() {
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        // --- Input Validation ---
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter email and password.", Toast.LENGTH_SHORT).show()
            return
        }

        // Disable button while processing
        loginButton.isEnabled = false
        Toast.makeText(this, "Logging in...", Toast.LENGTH_SHORT).show()

        // Firebase Login Logic
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                 loginButton.isEnabled = true
                if (task.isSuccessful) {
                    // Sign in success, navigate to the main activity
                    Log.d("LoginActivity", "signInWithEmail:success")
                    navigateToMain()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("LoginActivity", "signInWithEmail:failure", task.exception)
                    val errorMessage = task.exception?.message ?: "Authentication failed."
                    Toast.makeText(baseContext, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        //End Firebase Logic
    }

     // Navigate to MainActivity after successful login
    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun handleSignupNavigation() {
        val intent = Intent(this, SignupActivity::class.java)
        startActivity(intent)
    }
} 