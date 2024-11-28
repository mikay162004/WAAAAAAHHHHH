package com.example.slambook_mundas

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.slambook_mundas.databinding.ActivityHomeBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Home : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var slamAdapter: SlamAdapter
    private val slamList = ArrayList<Slam>() // List to store Slam objects

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences: SharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        val nickname = sharedPreferences.getString("name", "Guest")
        // Get the nickname passed from OnboardActivity


        // Set the greeting text with the nickname if it's available
        binding.greeting.text = if (nickname != null) {
            "Hi, $nickname!"  // Update greeting with nickname
        } else {
            "Hi, User!"  // Fallback if no nickname is provided
        }

        // Initialize RecyclerView
        setupRecyclerView()

        // Load saved slams from SharedPreferences
        loadSlams()

        // Set up "Create Slam" button
        binding.createSlam.setOnClickListener {
            val intent = Intent(this, NewSlam::class.java)
            startActivity(intent)
        }

        // Optional: Set up "Friends" button
        binding.friendsButton.setOnClickListener {
            Toast.makeText(this, "Friends feature coming soon!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupRecyclerView() {
        // Initialize SlamAdapter with delete functionality
        slamAdapter = SlamAdapter(slamList) { position ->
            deleteSlam(position)
        }

        // Set up RecyclerView
        binding.slamRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@Home)
            adapter = slamAdapter
        }
    }

    private fun loadSlams() {
        // Get SharedPreferences
        val sharedPreferences: SharedPreferences =
            getSharedPreferences("SlambookData", MODE_PRIVATE)

        // Retrieve the slam list using Gson
        val gson = Gson()
        val slamListType = object : TypeToken<ArrayList<Slam>>() {}.type
        val savedSlams: ArrayList<Slam>? =
            gson.fromJson(sharedPreferences.getString("slamList", "[]"), slamListType)

        // Add saved slams to the current list
        if (savedSlams != null) {
            slamList.addAll(savedSlams)
            slamAdapter.notifyDataSetChanged()
        }
    }

    private fun saveSlams() {
        // Save the updated slam list back to SharedPreferences
        val sharedPreferences: SharedPreferences =
            getSharedPreferences("SlambookData", MODE_PRIVATE)
        val gson = Gson()
        val editor = sharedPreferences.edit()
        editor.putString("slamList", gson.toJson(slamList))
        editor.apply()
    }

    private fun deleteSlam(position: Int) {
        // Remove item from the list
        slamList.removeAt(position)

        // Save updated list to SharedPreferences
        saveSlams()

        // Notify adapter of item removal
        slamAdapter.notifyItemRemoved(position)

        Toast.makeText(this, "Slam deleted!", Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        // Reload slams in case a new one was added
        slamList.clear()
        loadSlams()
    }
}
