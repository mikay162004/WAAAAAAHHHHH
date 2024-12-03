package com.example.slambook_mundas

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.slambook_mundas.databinding.ActivityFriendSlamBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FriendSlam : AppCompatActivity() {

    private lateinit var binding: ActivityFriendSlamBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFriendSlamBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Handle the Save button click
        binding.saveFriendSlamButton.setOnClickListener {
            saveFriendSlam()
        }
    }

    private fun saveFriendSlam() {
        val name = binding.editFriendName.text.toString().trim()
        val nickname = binding.editFriendNickname.text.toString().trim()
        val message = binding.editFriendMessage.text.toString().trim()

        // Validation: Ensure that name and nickname are filled in
        if (name.isBlank() || nickname.isBlank()) {
            Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
            return
        }

        // Create a new Friend Slam map
        val friendSlam = hashMapOf(
            "name" to name,
            "nickname" to nickname,
            "message" to message
        )

        // Retrieve existing slams from SharedPreferences
        val sharedPreferences = getSharedPreferences("SlambookData", MODE_PRIVATE)
        val gson = Gson()
        val slamListType = object : TypeToken<ArrayList<HashMap<String, String>>>() {}.type
        val existingSlams: ArrayList<HashMap<String, String>> =
            gson.fromJson(sharedPreferences.getString("slamList", "[]"), slamListType)
                ?: arrayListOf()

        // Add the new friend slam to the list
        existingSlams.add(friendSlam)

        // Save the updated list back to SharedPreferences
        val editor = sharedPreferences.edit()
        editor.putString("slamList", gson.toJson(existingSlams))
        editor.apply()

        // Show success message
        Toast.makeText(this, "Friend Slam saved!", Toast.LENGTH_SHORT).show()

        // Redirect to the Home activity
        val intent = Intent(this, Home::class.java)
        startActivity(intent)
        finish() // Close the FriendSlam activity
    }
}
