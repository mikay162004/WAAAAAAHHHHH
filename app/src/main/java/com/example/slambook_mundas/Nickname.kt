package com.example.slambook_mundas

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.slambook_mundas.databinding.ActivityNicknameBinding

class Nickname : AppCompatActivity() {

    private lateinit var binding: ActivityNicknameBinding  // ViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout using ViewBinding
        binding = ActivityNicknameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize SharedPreferences
        val sharedPreferences: SharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)

        // Check if the user has already seen the home screen
        val isHomeShown = sharedPreferences.getBoolean("isHomeShown", false)
        if (isHomeShown) {
            navigateToHome()
            return
        }

        // Set up the click listener for the "Next" button
        binding.button2.setOnClickListener {
            saveDataAndNavigate(sharedPreferences)
        }
    }

    private fun navigateToHome() {
        val intent = Intent(this, Home::class.java)
        startActivity(intent)
        finish()
    }

    private fun saveDataAndNavigate(sharedPreferences: SharedPreferences) {
        // Get the nickname from the EditText
        val nickname = binding.editTextText.text.toString()

        // Validate input
        if (nickname.isEmpty()) {
            Toast.makeText(this, "Please Enter Your Nickname", Toast.LENGTH_SHORT).show()
            return
        }

        // Save nickname and flag to SharedPreferences
        val editor = sharedPreferences.edit()
        editor.putString("name", nickname)
        editor.putBoolean("isHomeShown", true) // Mark home as shown
        editor.apply()

        // Navigate to Home activity
        Toast.makeText(this, "Welcome, $nickname!", Toast.LENGTH_SHORT).show()
        navigateToHome()
    }
}
