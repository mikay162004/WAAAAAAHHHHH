package com.example.slambook_mundas

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.slambook_mundas.databinding.ActivityNewSlamBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

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
        val name = binding.editName.text.toString().trim()
        val nickname = binding.editNickname.text.toString().trim()
        val ageInput = binding.editAge.text.toString().trim()
        val birthday = binding.editBirthday.text.toString().trim()
        val zodiacSign = binding.editZodiacSign.text.toString().trim()
        val hobbies = binding.editHobbies.text.toString().trim()
        val favorites = binding.editFavorites.text.toString().trim()

        // Validate age input
        if (ageInput.isBlank()) {
            binding.editAge.error = "Please enter your age."
            return
        }

        val age = ageInput.toIntOrNull()
        if (age == null || age <= 0) {
            binding.editAge.error = "Please enter a valid number for your age."
            return
        }

        // Validate other fields
        if (name.isBlank() || nickname.isBlank()) {
            Toast.makeText(this, "Please fill in all required fields.", Toast.LENGTH_SHORT).show()
            return
        }

        // Create a new Slam object
        val newSlam = Slam(name, nickname, age, birthday, zodiacSign, hobbies, favorites)

        // Retrieve existing slams from SharedPreferences
        val sharedPreferences = getSharedPreferences("SlambookData", MODE_PRIVATE)
        val gson = Gson()
        val slamListType = object : TypeToken<ArrayList<Slam>>() {}.type
        val existingSlams: ArrayList<Slam> =
            gson.fromJson(sharedPreferences.getString("slamList", "[]"), slamListType)
                ?: arrayListOf()

        // Add the new slam to the list
        existingSlams.add(newSlam)

        // Save the updated list back to SharedPreferences
        val editor = sharedPreferences.edit()
        editor.putString("slamList", gson.toJson(existingSlams))
        editor.apply()

        // Show confirmation
        Toast.makeText(this, "Slam saved!", Toast.LENGTH_SHORT).show()

        // Redirect to Home
        val intent = Intent(this, Home::class.java)
        startActivity(intent)
        finish()
    }
}