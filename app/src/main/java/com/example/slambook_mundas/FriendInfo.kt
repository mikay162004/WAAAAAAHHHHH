package com.example.slambook_mundas

import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FriendInfo : AppCompatActivity() {

    private lateinit var nicknameTextView: TextView
    private lateinit var imageViewProfile: ImageView
    private lateinit var nameTextView: TextView
    private lateinit var messageTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend_info) // Ensure this is the correct layout file

        // Initialize views
        nicknameTextView = findViewById(R.id.friend_nickname)
        imageViewProfile = findViewById(R.id.imageViewProfile)
        nameTextView = findViewById(R.id.friend_name)
        messageTextView = findViewById(R.id.friend_message)

        // Retrieve slam position from intent
        val position = intent.getIntExtra("slam_position", -1)

        // Load slam data from SharedPreferences
        val sharedPreferences = getSharedPreferences("SlambookData1", MODE_PRIVATE) // Correct key
        val gson = Gson()
        val slamListType = object : TypeToken<ArrayList<FriendSlamDataClass>>() {}.type
        val slamList: ArrayList<FriendSlamDataClass> = gson.fromJson(
            sharedPreferences.getString("slamList", "[]"), slamListType
        ) ?: arrayListOf()

        // Populate views if position is valid
        if (position >= 0 && position < slamList.size) {
            val slam = slamList[position]

            nicknameTextView.text = slam.nickname ?: "No Nickname"
            nameTextView.text = slam.name ?: "No Name"
            messageTextView.text = slam.message ?: "No Message"

            // Load profile image with fallback
            slam.imageUri?.let {
                imageViewProfile.setImageURI(Uri.parse(it))
            } ?: imageViewProfile.setImageResource(R.drawable.user) // Default image
        } else {
            // Handle invalid position
            nicknameTextView.text = "Invalid Slam"
            nameTextView.text = ""
            messageTextView.text = ""
            imageViewProfile.setImageResource(R.drawable.user)
        }
    }
}
