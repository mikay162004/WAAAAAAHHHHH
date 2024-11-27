package com.example.slambook_mundas

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.slambook_mundas.databinding.ActivityOnboardingBinding

class Onboarding : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout using view binding
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up the click listener for the button
        binding.button.setOnClickListener {
            val intent = Intent(this, Nickname::class.java)
            startActivity(intent)
        }
    }
}
