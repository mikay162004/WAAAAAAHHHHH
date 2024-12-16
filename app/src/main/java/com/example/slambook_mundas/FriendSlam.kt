package com.example.slambook_mundas

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


        // Retrieve existing slam data
        slamPosition = intent.getIntExtra("slamPosition", -1)
        binding.editFriendName.setText(intent.getStringExtra("slamName"))
        binding.editFriendNickname.setText(intent.getStringExtra("slamNickname"))
        binding.editFriendMessage.setText(intent.getStringExtra("slamMessage"))

        // Set up button listeners
        binding.saveFriendSlamButton.setOnClickListener { saveFriendSlam() }

    }

    /**
     * Save or update a slam and store it in SharedPreferences.
     */
    private fun saveFriendSlam() {
        val name = binding.editFriendName.text.toString().trim()
        val nickname = binding.editFriendNickname.text.toString().trim()
        val message = binding.editFriendMessage.text.toString().trim()


        // Validate input fields
        if (name.isBlank() || nickname.isBlank()) {
            Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
            return
        }

        // Load the existing slam list from SharedPreferences
        val sharedPreferences = getSharedPreferences("SlambookData1", MODE_PRIVATE)
        val gson = Gson()
        val slamListType = object : TypeToken<ArrayList<FriendSlamDataClass>>() {}.type
        val slamList: ArrayList<FriendSlamDataClass> = gson.fromJson(
            sharedPreferences.getString("slamList", "[]"),
            slamListType
        )

        // Save the updated slam list back to SharedPreferences
        sharedPreferences.edit().putString("slamList", gson.toJson(slamList)).apply()
        Toast.makeText(this, "Slam saved!", Toast.LENGTH_SHORT).show()
        finish()
    }
}
