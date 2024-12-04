package com.example.slambook_mundas

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.slambook_mundas.databinding.ActivityFriendSlamBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FriendSlam : AppCompatActivity() {

    private lateinit var binding: ActivityFriendSlamBinding
    private var slamPosition: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFriendSlamBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the slam data and position from Intent
        val slamName = intent.getStringExtra("slamName") ?: ""
        val slamNickname = intent.getStringExtra("slamNickname") ?: ""
        val slamMessage = intent.getStringExtra("slamMessage") ?: ""
        slamPosition = intent.getIntExtra("slamPosition", -1)

        // Populate the fields if editing an existing slam
        binding.editFriendName.setText(slamName)
        binding.editFriendNickname.setText(slamNickname)
        binding.editFriendMessage.setText(slamMessage)

        // Handle the Save button click
        binding.saveFriendSlamButton.setOnClickListener {
            saveFriendSlam()
        }
    }

    private fun saveFriendSlam() {
        val name = binding.editFriendName.text.toString().trim()
        val nickname = binding.editFriendNickname.text.toString().trim()
        val message = binding.editFriendMessage.text.toString().trim()

        // Validation
        if (name.isBlank() || nickname.isBlank()) {
            Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
            return
        }

        // If slamPosition is -1, it's a new slam. Otherwise, it's an edit.
        val sharedPreferences = getSharedPreferences("SlambookData1", MODE_PRIVATE)
        val gson = Gson()
        val slamListType = object : TypeToken<ArrayList<FriendSlamDataClass>>() {}.type
        val existingSlamsJson = sharedPreferences.getString("slamList", "[]")
        val slamList: ArrayList<FriendSlamDataClass> = gson.fromJson(existingSlamsJson, slamListType)

        if (slamPosition == -1) {
            // Add new slam
            val newSlam = FriendSlamDataClass(name, nickname, message)
            slamList.add(newSlam)
        } else {
            // Update existing slam
            slamList[slamPosition!!] = FriendSlamDataClass(name, nickname, message)
        }

        // Save the updated slam list to SharedPreferences
        val editor = sharedPreferences.edit()
        editor.putString("slamList", gson.toJson(slamList))
        editor.apply()

        // Show success message
        Toast.makeText(this, "Slam saved!", Toast.LENGTH_SHORT).show()

        // Close the activity
        finish()
    }
}
