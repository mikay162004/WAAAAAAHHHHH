package com.example.slambook_mundas

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.slambook_mundas.databinding.ActivityNicknameBinding

class Nickname : AppCompatActivity() {

    private lateinit var binding: ActivityNicknameBinding  // ViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout using ViewBinding
        binding = ActivityNicknameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up the click listener for the "Next" button
        binding.button2.setOnClickListener {
            // Get the nickname from the EditText
            val nickname = binding.editTextText.text.toString()

            // Create an intent to navigate to the HomeActivity
            val intent = Intent(this, Home::class.java)

            // Pass the nickname as an extra to HomeActivity
            intent.putExtra("nickname", nickname)

            // Start the HomeActivity
            startActivity(intent)
        }
    }
}
