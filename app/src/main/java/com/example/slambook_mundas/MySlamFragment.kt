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
import com.example.slambook_mundas.databinding.FragmentMySlamBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MySlamFragment : Fragment() {

    private var _binding: FragmentMySlamBinding? = null
    private val binding get() = _binding!!

    private lateinit var slamAdapter: SlamAdapter
    private var slamList: ArrayList<Slam> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMySlamBinding.inflate(inflater, container, false)

        // Load saved slams
        loadSavedSlams()

        // Setup RecyclerView with SlamAdapter
        slamAdapter = SlamAdapter(slamList) { position ->
            // Handle slam deletion (if needed)
            deleteSlam(position)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = slamAdapter

        // Handle ImageView click to navigate to NewSlam activity
        val createSlamButton: ImageView = binding.createSlam
        createSlamButton.setOnClickListener {
            // Start NewSlam activity
            val intent = Intent(activity, NewSlam::class.java)
            intent.putExtra("isFriend", false)  // Optionally pass data like this
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

    // Function to delete a slam (if needed)
    private fun deleteSlam(position: Int) {
        slamList.removeAt(position)
        updateSharedPreferences()
        slamAdapter.notifyItemRemoved(position)
    }

    // Update SharedPreferences after deleting a slam
    private fun updateSharedPreferences() {
        val sharedPreferences = activity?.getSharedPreferences("SlambookData", AppCompatActivity.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        val gson = Gson()
        val updatedSlamsJson = gson.toJson(slamList)
        editor?.putString("slamList", updatedSlamsJson)
        editor?.apply()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): MySlamFragment {
            return MySlamFragment()
        }
    }
}
