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

        // Retrieve nickname from SharedPreferences
        val sharedPreferences: SharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        val nickname = sharedPreferences.getString("name", "Guest")

        // Set greeting text with the nickname
        binding.greeting.text = if (nickname != null) {
            "Hi, $nickname!"
        } else {
            "Hi, User!"
        }

        // Initialize RecyclerView
        setupRecyclerView()

        // Load saved slams
        loadSlams()

        // Handle "Create Slam" button
        binding.createSlam.setOnClickListener {
            val intent = Intent(this, NewSlam::class.java)
            intent.putExtra("isFriend", false) // Default to Personal Slam
            startActivity(intent)
        }

        // Handle "Friends" button click
        binding.friendsButton.setOnClickListener {
            val intent = Intent(this, FriendSlam::class.java)
            startActivity(intent)
        }

        // Handle "My Slam" button click
        binding.mySlamButton.setOnClickListener {
            showPersonalSlams()
        }
    }

    private fun setupRecyclerView() {
        slamAdapter = SlamAdapter(slamList) { position ->
            deleteSlam(position)
        }

        binding.slamRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@Home)
            adapter = slamAdapter
        }
    }

    private fun loadSlams() {
        val sharedPreferences: SharedPreferences = getSharedPreferences("SlambookData", MODE_PRIVATE)
        val gson = Gson()
        val slamListType = object : TypeToken<ArrayList<Slam>>() {}.type
        val savedSlams: ArrayList<Slam>? = gson.fromJson(sharedPreferences.getString("slamList", "[]"), slamListType)

        if (savedSlams != null) {
            slamList.addAll(savedSlams)
            slamAdapter.notifyDataSetChanged()
        }
    }

    private fun saveSlams() {
        val sharedPreferences: SharedPreferences = getSharedPreferences("SlambookData", MODE_PRIVATE)
        val gson = Gson()
        val editor = sharedPreferences.edit()
        editor.putString("slamList", gson.toJson(slamList))
        editor.apply()
    }

    private fun deleteSlam(position: Int) {
        slamList.removeAt(position)
        saveSlams()
        slamAdapter.notifyItemRemoved(position)
        Toast.makeText(this, "Slam deleted!", Toast.LENGTH_SHORT).show()
    }

    private fun showPersonalSlams() {
        // Show only personal slams
        val personalSlams = slamList.filter { !it.isFriend }
        slamList.clear()
        slamList.addAll(personalSlams)
        slamAdapter.notifyDataSetChanged()

        // Optionally, show a message to indicate the list is now filtered
        Toast.makeText(this, "Displaying My Slams", Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        slamList.clear()
        loadSlams()
    }
}
