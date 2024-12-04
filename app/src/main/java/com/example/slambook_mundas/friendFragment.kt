package com.example.slambook_mundas

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.slambook_mundas.databinding.FragmentFriendBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FriendFragment : Fragment() {

    private var _binding: FragmentFriendBinding? = null
    private val binding get() = _binding!!

    private lateinit var slamAdapter: FriendSlamAdapter
    private var slamList = mutableListOf<FriendSlamDataClass>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFriendBinding.inflate(inflater, container, false)

        // Load saved slams from SharedPreferences
        loadSavedSlams()

        // Initialize adapter with edit and delete handlers
        slamAdapter = FriendSlamAdapter(slamList, { position ->
            editSlam(position) // Handle editing
        }, { position ->
            deleteSlam(position) // Handle deletion
        })

        // Set up RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = slamAdapter

        // Handle ImageView click to navigate to FriendSlam activity
        val createSlamButton: ImageView = binding.create
        createSlamButton.setOnClickListener {
            val intent = Intent(activity, FriendSlam::class.java)
            intent.putExtra("isFriend", true) // Indicate it's a friend slam
            startActivity(intent)
        }

        return binding.root
    }

    // Load saved slams from SharedPreferences
    private fun loadSavedSlams() {
        val sharedPreferences =
            activity?.getSharedPreferences("SlambookData1", AppCompatActivity.MODE_PRIVATE)
        val gson = Gson()
        val slamListType = object : TypeToken<ArrayList<FriendSlamDataClass>>() {}.type
        val savedSlamsJson = sharedPreferences?.getString("slamList", "[]")
        slamList = gson.fromJson(savedSlamsJson, slamListType) ?: ArrayList()

        // Show or hide "No Slam" message
        if (slamList.isEmpty()) {
            binding.noSlamMessage.visibility = View.VISIBLE
        } else {
            binding.noSlamMessage.visibility = View.GONE
        }
    }

    // Delete a slam
    private fun deleteSlam(position: Int) {
        slamList.removeAt(position)
        updateSharedPreferences() // Update SharedPreferences after deletion
        slamAdapter.notifyItemRemoved(position)
    }

    // Edit a slam message
    private fun editSlam(position: Int) {
        val slam = slamList[position]

        // Pass slam data (including position) to FriendSlam activity
        val intent = Intent(activity, FriendSlam::class.java)
        intent.putExtra("slamName", slam.name)
        intent.putExtra("slamNickname", slam.nickname)
        intent.putExtra("slamMessage", slam.message)
        intent.putExtra("slamPosition", position) // Pass position for editing
        startActivity(intent)
    }

    // Update SharedPreferences after modifying the slam list
    private fun updateSharedPreferences() {
        val sharedPreferences =
            activity?.getSharedPreferences("SlambookData1", AppCompatActivity.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        val gson = Gson()
        val updatedSlamsJson = gson.toJson(slamList)
        editor?.putString("slamList", updatedSlamsJson)
        editor?.apply() // Save changes
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): FriendFragment {
            return FriendFragment()
        }
    }
}
