package com.example.slambook_mundas // Replace with your actual package name

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.slambook_mundas.databinding.ActivityNewSlamBinding
class NewSlam : AppCompatActivity() {

    private lateinit var binding: ActivityNewSlamBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize binding
        binding = ActivityNewSlamBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set onClickListener for the Save button
        binding.saveButton.setOnClickListener {
            saveData()
        }
    }

    private fun saveData() {
        // Get values from the input fields
        val name = binding.editName.text.toString()
        val nickname = binding.editNickname.text.toString()
        val age = binding.editAge.text.toString()
        val birthday = binding.editBirthday.text.toString()
        val zodiacSign = binding.editZodiacSign.text.toString()
        val hobbies = binding.editHobbies.text.toString()
        val favorites = binding.editFavorites.text.toString()

        // Save data to SharedPreferences
        val sharedPreferences: SharedPreferences =
            getSharedPreferences("SlambookData", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("name", name)
        editor.putString("nickname", nickname)
        editor.putString("age", age)
        editor.putString("birthday", birthday)
        editor.putString("zodiacSign", zodiacSign)
        editor.putString("hobbies", hobbies)
        editor.putString("favorites", favorites)
        editor.apply()

        // Show a confirmation message
        Toast.makeText(this, "Data Saved!", Toast.LENGTH_SHORT).show()

        // Redirect back to the Home screen
        val intent = Intent(this, Home::class.java)
        startActivity(intent)
        finish()
    }
}