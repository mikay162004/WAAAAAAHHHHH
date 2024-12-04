package com.example.slambook_mundas

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.slambook_mundas.databinding.FragmentFriendBinding
import com.google.gson.reflect.TypeToken

class FriendFragment : Fragment() {

    private var _binding: FragmentFriendBinding? = null
    private val binding get() = _binding!!

    private lateinit var slamAdapter: FriendSlamAdapter
    private var slamList: ArrayList<Slam> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFriendBinding.inflate(inflater, container, false)

        // Load saved slams (from SharedPreferences)
        loadSavedSlams()

        // Setup RecyclerView with FriendSlamAdapter
        slamAdapter = FriendSlamAdapter(slamList) { position ->
            // Handle slam deletion
            deleteSlam(position)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = slamAdapter

        // Handle ImageView click to navigate to FriendSlam activity
        val createSlamButton: ImageView = binding.create
        createSlamButton.setOnClickListener {
            // Start FriendSlam activity
            val intent = Intent(activity, FriendSlam::class.java)
            intent.putExtra("isFriend", true)  // Pass data indicating it's a friend slam
            startActivity(intent)
        }

        return binding.root
    }

    // Function to load saved slams from SharedPreferences
    private fun loadSavedSlams() {
        val sharedPreferences = activity?.getSharedPreferences("SlambookData", AppCompatActivity.MODE_PRIVATE)
        val gson = Gson()
        val slamListType = object : TypeToken<ArrayList<Slam>>() {}.type
        val savedSlamsJson = sharedPreferences?.getString("slamList", "[]")
        slamList = gson.fromJson(savedSlamsJson, slamListType) ?: ArrayList()

        if (slamList.isEmpty()) {
            binding.noSlamMessage.visibility = View.VISIBLE
        } else {
            binding.noSlamMessage.visibility = View.GONE
        }
    }

    // Function to delete a slam
    private fun deleteSlam(position: Int) {
        slamList.removeAt(position)  // Remove the slam at the specified position
        updateSharedPreferences()  // Update SharedPreferences after deletion
        slamAdapter.notifyItemRemoved(position)  // Notify adapter to update the RecyclerView
    }

    // Function to update SharedPreferences after deletion
    private fun updateSharedPreferences() {
        val sharedPreferences = activity?.getSharedPreferences("SlambookData", AppCompatActivity.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        val gson = Gson()
        val updatedSlamsJson = gson.toJson(slamList)  // Convert the updated slam list to JSON
        editor?.putString("slamList", updatedSlamsJson)  // Save updated slam list in SharedPreferences
        editor?.apply()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // Clear the binding reference when the view is destroyed
    }

    companion object {
        // Simplified newInstance method for creating the fragment
        fun newInstance(): FriendFragment {
            return FriendFragment()
        }
    }
}
