package com.example.slambook_mundas

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.slambook_mundas.databinding.ActivityNewSlamBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class NewSlam : AppCompatActivity() {

    private lateinit var binding: ActivityNewSlamBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize binding
        binding = ActivityNewSlamBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Show DatePickerDialog when the birthday field is clicked
        binding.editBirthday.setOnClickListener {
            showDatePickerDialog(binding.editBirthday)
        }

        // Save button logic
        binding.saveButton.setOnClickListener {
            saveData()
        }
    }

    private fun showDatePickerDialog(editText: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                // Format the selected date as mm/dd/yyyy
                val formattedDate = "${selectedMonth + 1}/$selectedDay/$selectedYear"
                editText.setText(formattedDate)
            },
            year, month, day
        )
        datePickerDialog.show()
    }

    private fun saveData() {
        // Retrieve data from input fields
        val isFriend = intent.getBooleanExtra("isFriend", false)  // Get from intent
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

        // Create a new Friend Slam object
        val newSlam = Slam(name, nickname, age, birthday, zodiacSign, hobbies, favorites, isFriend = true)

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