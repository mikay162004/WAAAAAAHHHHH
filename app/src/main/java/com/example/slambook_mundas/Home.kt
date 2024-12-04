package com.example.slambook_mundas

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.slambook_mundas.databinding.ActivityHomeBinding

class Home : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

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

        // Load MySlamFragment by default
        loadFragment(MySlamFragment.newInstance())

        // Handle button clicks to switch fragments
        binding.myslamButton.setOnClickListener {
            loadFragment(MySlamFragment.newInstance())
        }

        binding.friendsButton.setOnClickListener {
            loadFragment(FriendFragment.newInstance())
        }

        // Handle ImageView click to go to NewSlam
        // Access the create_slam ImageView through binding
        binding.fragmentContainer.setOnClickListener {
            // Start the NewSlam activity when the ImageView is clicked
            val intent = Intent(this, NewSlam::class.java)
            intent.putExtra("isFriend", false) // Optional: Pass data if needed
            startActivity(intent)
        }

    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    fun openDrawer(view: View) {}
}
