package com.example.slambook_mundas

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.slambook_mundas.databinding.ActivityMySlamInfoBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MySlamInfo : AppCompatActivity() {

    private lateinit var binding: ActivityMySlamInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize ViewBinding
        binding = ActivityMySlamInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve slam position from intent
        val position = intent.getIntExtra("slam_position", -1)
        Log.d("SlamInfo", "Position received: $position")

        // Load slam data from SharedPreferences
        val sharedPreferences = getSharedPreferences("SlambookData", MODE_PRIVATE)
        val gson = Gson()
        val slamListType = object : TypeToken<ArrayList<Slam>>() {}.type
        val slamList: ArrayList<Slam> = gson.fromJson(
            sharedPreferences.getString("slamList", "[]"), slamListType
        ) ?: arrayListOf()

        Log.d("SlamInfo", "Slam List Size: ${slamList.size}")

        // Check if position is valid
        if (position >= 0 && position < slamList.size) {
            val slam = slamList[position]
            Log.d("SlamInfo", "Displaying Slam: ${slam.nickname}")

            // Populate views with slam data using binding
            binding.textViewNickname.text = slam.nickname
            binding.editName.text = slam.name
            binding.textViewAge.text = slam.age?.toString() ?: "N/A"
            binding.textViewZodiaSign.text = slam.zodiacSign ?: "N/A"
            binding.textViewBirthday.text = slam.birthday ?: "N/A"
            binding.textViewGender.text = slam.gender ?: "N/A"
            binding.textViewHobbies.text = slam.hobbies ?: "N/A"
            binding.textViewInterest.text = slam.interests ?: "N/A"
            binding.textViewSports.text = slam.sports ?: "N/A"

            // Load profile image if available
            slam.imageUri?.let {
                binding.imageViewProfile.setImageURI(Uri.parse(it))
            } ?: binding.imageViewProfile.setImageResource(R.drawable.user) // Default image
        } else {
            // Handle invalid position (if necessary)
            Log.e("SlamInfo", "Invalid position: $position")
            binding.textViewNickname.text = "Invalid Slam"
            binding.editName.text = ""
            binding.textViewAge.text = ""
            binding.textViewZodiaSign.text = ""
            binding.textViewBirthday.text = ""
            binding.textViewGender.text = ""
            binding.textViewHobbies.text = ""
            binding.textViewInterest.text = ""
            binding.textViewSports.text = ""
            binding.imageViewProfile.setImageResource(R.drawable.user) // Default image
        }
    }
}
