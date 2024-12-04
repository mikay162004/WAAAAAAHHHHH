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

        // Check if it's an edit action
        val slamJson = intent.getStringExtra("slam")
        val position = intent.getIntExtra("position", -1)

        if (slamJson != null && position != -1) {
            val slam = Gson().fromJson(slamJson, Slam::class.java)

            // Populate fields with existing slam data
            binding.editName.setText(slam.name)
            binding.editNickname.setText(slam.nickname)
            binding.editAge.setText(slam.age.toString())
            binding.editGender.setText(slam.gender)
            binding.editBirthday.setText(slam.birthday)
            binding.editZodiacSign.setText(slam.zodiacSign)
            binding.editHobbies.setText(slam.hobbies)
            binding.editFavorites.setText(slam.favorites)
        }

        // Show DatePickerDialog when the birthday field is clicked
        binding.editBirthday.setOnClickListener {
            showDatePickerDialog(binding.editBirthday)
        }

        // Save button logic
        binding.saveButton.setOnClickListener {
            saveData(position)
        }

        // Cancel button logic
        binding.cancelButton.setOnClickListener {
            finish() // Close the activity without saving changes
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

    private fun saveData(position: Int?) {
        // Retrieve data from input fields
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
        val newSlam = Slam(name, nickname, age, birthday, zodiacSign, hobbies, favorites, isFriend = true)

        // Retrieve existing slams from SharedPreferences
        val sharedPreferences = getSharedPreferences("SlambookData", MODE_PRIVATE)
        val gson = Gson()
        val slamListType = object : TypeToken<ArrayList<Slam>>() {}.type
        val slamList: ArrayList<Slam> =
            gson.fromJson(sharedPreferences.getString("slamList", "[]"), slamListType)
                ?: arrayListOf()

        if (position != null && position != -1) {
            // Edit existing slam at the provided position
            slamList[position] = newSlam
        } else {
            // Add new slam if no position was provided
            slamList.add(newSlam)
        }

        // Save the updated list back to SharedPreferences
        val editor = sharedPreferences.edit()
        editor.putString("slamList", gson.toJson(slamList))
        editor.apply()

        // Show confirmation message
        Toast.makeText(this, if (position != null && position != -1) "Slam updated!" else "Slam saved!", Toast.LENGTH_SHORT).show()

        // Set the result to return to MySlamFragment (position for editing)
        val resultIntent = Intent().apply {
            putExtra("updated_text", newSlam.name)  // Return the updated slam name (or other data if needed)
            putExtra("slam_position", position ?: -1)  // If editing, return position
        }
        setResult(RESULT_OK, resultIntent)

        // Redirect back to Home (or any other activity)
        val intent = Intent(this, Home::class.java)
        startActivity(intent)
        finish()
    }
}
