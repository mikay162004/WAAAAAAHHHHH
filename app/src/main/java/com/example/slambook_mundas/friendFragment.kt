package com.example.slambook_mundas

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.slambook_mundas.databinding.FragmentFriendBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FriendFragment : Fragment() {

    private var _binding: FragmentFriendBinding? = null
    private val binding get() = _binding!!

    private lateinit var friendSlamAdapter: FriendSlamAdapter
    private var slamList = mutableListOf<FriendSlamDataClass>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFriendBinding.inflate(inflater, container, false)

        // Load saved slams from SharedPreferences
        loadSavedSlams()

        setupRecyclerView()
        setupCreateButton()

        return binding.root
    }

    private fun setupRecyclerView() {
        friendSlamAdapter = FriendSlamAdapter(
            slamList,
            { position -> editSlam(position) },
            { position -> deleteSlam(position) },
            { position -> viewSlam(position) }
        )

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = friendSlamAdapter
        }
    }

    private fun setupCreateButton() {
        binding.create.setOnClickListener {
            val intent = Intent(activity, FriendSlam::class.java).apply {
                putExtra("isFriend", true)
            }
            startActivity(intent)
        }
    }

    private fun loadSavedSlams() {
        val sharedPreferences =
            activity?.getSharedPreferences("SlambookData1", AppCompatActivity.MODE_PRIVATE)
        val gson = Gson()
        val slamListType = object : TypeToken<ArrayList<FriendSlamDataClass>>() {}.type
        val savedSlamsJson = sharedPreferences?.getString("slamList", "[]")
        slamList = gson.fromJson(savedSlamsJson, slamListType) ?: ArrayList()

        // Show or hide "No Slam" message
        binding.noSlamMessage.visibility = if (slamList.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun viewSlam(position: Int) {
        val intent = Intent(activity, FriendInfo::class.java).apply {
            putExtra("slam_position", position) // Pass position to FriendInfo
        }
        startActivity(intent)
    }

    private fun editSlam(position: Int) {
        val slam = slamList[position]
        val intent = Intent(activity, FriendSlam::class.java).apply {
            putExtra("slamName", slam.name)
            putExtra("slamNickname", slam.nickname)
            putExtra("slamMessage", slam.message)
            putExtra("slamImageUri", slam.imageUri)
            putExtra("slamPosition", position) // Pass the position for editing
        }
        startActivity(intent)
    }

    private fun deleteSlam(position: Int) {
        if (position < 0 || position >= slamList.size) return

        val builder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
        builder.setTitle("Delete Slam")
        builder.setMessage("Are you sure you want to delete this slam?")

        builder.setPositiveButton("Yes") { _, _ ->
            slamList.removeAt(position)
            updateSharedPreferences()
            friendSlamAdapter.notifyItemRemoved(position)
            binding.noSlamMessage.visibility = if (slamList.isEmpty()) View.VISIBLE else View.GONE
        }

        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }

        builder.create().show()
    }

    private fun updateSharedPreferences() {
        val sharedPreferences =
            activity?.getSharedPreferences("SlambookData1", AppCompatActivity.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        val gson = Gson()
        editor?.putString("slamList", gson.toJson(slamList))
        editor?.apply()
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
