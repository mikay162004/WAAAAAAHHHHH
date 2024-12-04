package com.example.slambook_mundas

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
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
    private var slamList = mutableListOf<Slam>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMySlamBinding.inflate(inflater, container, false)

        // Load saved slams
        loadSavedSlams()

        // Initialize SlamAdapter
        slamAdapter = SlamAdapter(slamList, { position ->
            deleteSlam(position)
        }, { position ->
            editSlam(position)  // Start editing
        })

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = slamAdapter

        return binding.root
    }

    private fun editSlam(position: Int) {
        val slam = slamList[position]

        // Create an intent to navigate to the NewSlam activity
        val intent = Intent(activity, NewSlam::class.java)

        // Pass slam data and position for updating later
        intent.putExtra("slam_text", slam.text)  // Pass the current slam text
        intent.putExtra("slam_position", position)  // Pass the position of the slam to update it

        // Start the NewSlam activity for result
        editSlamLauncher.launch(intent)
    }

    private val editSlamLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val data = result.data
                val updatedText = data?.getStringExtra("updated_text")
                val position = data?.getIntExtra("slam_position", -1)

                if (updatedText != null && position != null && position >= 0) {
                    // Update the slam text with the edited text
                    slamList[position].text = updatedText

                    // Update SharedPreferences with the updated slam list
                    updateSharedPreferences()

                    // Notify the adapter to refresh the UI
                    slamAdapter.notifyItemChanged(position)
                }
            }
        }

    private fun loadSavedSlams() {
        val sharedPreferences =
            activity?.getSharedPreferences("SlambookData", AppCompatActivity.MODE_PRIVATE)
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

    private fun updateSharedPreferences() {
        val sharedPreferences =
            activity?.getSharedPreferences("SlambookData", AppCompatActivity.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        val gson = Gson()
        val updatedSlamsJson = gson.toJson(slamList)
        editor?.putString("slamList", updatedSlamsJson)
        editor?.apply()
    }

    private fun deleteSlam(position: Int) {
        slamList.removeAt(position)
        updateSharedPreferences()
        slamAdapter.notifyItemRemoved(position)
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

