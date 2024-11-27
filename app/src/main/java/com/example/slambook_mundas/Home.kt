package com.example.slambook_mundas

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.slambook_mundas.databinding.ActivityHomeBinding

class Home : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize view binding
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set greeting using a nickname stored in SharedPreferences
        val sharedPreferences: SharedPreferences =
            getSharedPreferences("SlambookData", MODE_PRIVATE)
        val nickname = sharedPreferences.getString("nickname", "Nickname")
        binding.greeting.text = "Hi, $nickname!"

        // Set up "My Slam" button click listener
        binding.myslamButton.setOnClickListener {
            displayDataTextView(sharedPreferences)
        }

        // Optional: Add a click listener for "Friends" (if needed)
        binding.friendsButton.setOnClickListener {
            Toast.makeText(this, "Friends feature coming soon!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun displayDataTextView(sharedPreferences: SharedPreferences) {
        // Retrieve saved slam data from SharedPreferences
        val name = sharedPreferences.getString("name", "N/A")
        val nickname = sharedPreferences.getString("nickname", "N/A")
        val age = sharedPreferences.getString("age", "N/A")
        val birthday = sharedPreferences.getString("birthday", "N/A")
        val zodiacSign = sharedPreferences.getString("zodiacSign", "N/A")
        val hobbies = sharedPreferences.getString("hobbies", "N/A")
        val favorites = sharedPreferences.getString("favorites", "N/A")

        // Locate the TextView at the bottom
        val displayDataTextView: TextView = findViewById(R.id.displayDataTextView)

        // Format the data and display it
        val formattedData = """
        Name: $name
        Nickname: $nickname
        Age: $age
        Birthday: $birthday
        Zodiac Sign: $zodiacSign
        Hobbies: $hobbies
        Favorites: $favorites
    """.trimIndent()

        displayDataTextView.text = formattedData


        // Set OnClickListener for the create_slam button
        binding.createSlam.setOnClickListener {
            // Navigate to NewSlamActivity
            val intent = Intent(this, NewSlam::class.java)
            startActivity(intent)
        }
    }
}