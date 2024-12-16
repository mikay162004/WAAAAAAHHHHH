package com.example.slambook_mundas

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.slambook_mundas.databinding.ActivityNewSlamBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class NewSlam : AppCompatActivity() {

    private lateinit var binding: ActivityNewSlamBinding
    private var slamPosition: Int? = null
    private var selectedImageUri: Uri? = null
    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewSlamBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Spinner (You already have this in your UI setup)
        setupGenderSpinner()

        // Initialize Image Picker
        imagePickerLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                    selectedImageUri = result.data?.data
                    binding.imageViewProfile.setImageURI(selectedImageUri)
                }
            }

        // Set Button Actions
        binding.buttonUploadImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK).apply { type = "image/*" }
            imagePickerLauncher.launch(intent)
        }

        // Retrieve existing slam data for editing (if available)
        slamPosition = intent.getIntExtra("slamPosition", -1)
        binding.editName.setText(intent.getStringExtra("slamName"))
        binding.editNickname.setText(intent.getStringExtra("slamNickname"))
        binding.editHobbies.setText(intent.getStringExtra("slamHobbies"))
        binding.editBirthday.setText(intent.getStringExtra("slamBirthday"))

        intent.getStringExtra("slamImageUri")?.let {
            selectedImageUri = Uri.parse(it)
            binding.imageViewProfile.setImageURI(selectedImageUri)
        }

        // Setup Birthday Field with DatePicker
        binding.editBirthday.setOnClickListener {
            openDatePicker()
        }

        // Save Button
        binding.saveButton.setOnClickListener {
            saveNewSlam()
        }

        // Cancel Button
        binding.cancelButton.setOnClickListener { finish() }
    }

    /**
     * Set up the Spinner for gender selection.
     */
    private fun setupGenderSpinner() {
        // No additional logic required if the Spinner is already initialized in XML.
        // Assuming the first item is "Select Gender" as a placeholder.
    }

    /**
     * Opens a DatePickerDialog to select a date.
     */
    private fun openDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val formattedDate = "${selectedDay}/${selectedMonth + 1}/${selectedYear}"
            binding.editBirthday.setText(formattedDate)
        }, year, month, day)

        datePicker.show()
    }

    /**
     * Save or update a slam and store it in SharedPreferences.
     */
    private fun saveNewSlam() {
        val name = binding.editName.text.toString().trim()
        val nickname = binding.editNickname.text.toString().trim()
        val hobbies = binding.editHobbies.text.toString().trim()
        val birthday = binding.editBirthday.text.toString().trim()
        val gender = binding.genderSpinner.selectedItem.toString()
        val imageUriString = selectedImageUri?.toString()

        // Validate input fields
        if (name.isBlank() || nickname.isBlank() || gender == "Select Gender" || hobbies.isBlank()) {
            Toast.makeText(this, "Please fill in all required fields.", Toast.LENGTH_SHORT).show()
            return
        }

        // Create new Slam object
        val newSlam = Slam(
            name = name,
            nickname = nickname,
            age = null,
            birthday = birthday,
            zodiacSign = null,
            hobbies = hobbies,
            interests = null,
            sports = null,
            favorites = null,
            message = null,
            gender = gender,
            isFriend = true,
            imageUri = imageUriString
        )

        // Load the existing slam list from SharedPreferences
        val sharedPreferences = getSharedPreferences("SlambookData", MODE_PRIVATE)
        val gson = Gson()
        val slamListType = object : TypeToken<ArrayList<Slam>>() {}.type
        val slamList: ArrayList<Slam> = gson.fromJson(
            sharedPreferences.getString("slamList", "[]"),
            slamListType
        )

        // Save or update the slam in the list
        if (slamPosition == -1) {
            slamList.add(newSlam) // Add the new slam to the list
        } else {
            slamList[slamPosition!!] = newSlam // Update the existing slam at slamPosition
        }

        // Store the updated slam list back to SharedPreferences
        sharedPreferences.edit().putString("slamList", gson.toJson(slamList)).apply()

        // Provide feedback to the user
        Toast.makeText(this, "Slam saved!", Toast.LENGTH_SHORT).show()

        // Return to the fragment or previous activity
        setResult(Activity.RESULT_OK, Intent())
        finish()
    }
}
