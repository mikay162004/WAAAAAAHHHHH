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

    private var slamList = mutableListOf<Slam>()
    private lateinit var slamAdapter: SlamAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMySlamBinding.inflate(inflater, container, false)

        slamAdapter = SlamAdapter(
            slamList,
            requireContext(),
            { position -> editSlam(position) },
            { position -> deleteSlam(position) }
        )

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = slamAdapter

        loadSavedSlams()

        return binding.root
    }

    private fun loadSavedSlams() {
        val sharedPreferences =
            activity?.getSharedPreferences("SlambookData", AppCompatActivity.MODE_PRIVATE)

        sharedPreferences?.let {
            val gson = Gson()
            val slamListType = object : TypeToken<ArrayList<Slam>>() {}.type
            val savedSlamsJson = it.getString("slamList", "[]")
            val loadedSlams: MutableList<Slam> =
                gson.fromJson(savedSlamsJson, slamListType) ?: ArrayList()

            slamAdapter.updateList(loadedSlams)
            toggleNoSlamsMessage()
        }
    }

    private fun editSlam(position: Int) {
        if (position < 0 || position >= slamList.size) return

        val slam = slamList[position]

        val intent = Intent(activity, NewSlam::class.java).apply {
            putExtra("slam_position", position)
            putExtra("slam_name", slam.name)
            putExtra("slam_nickname", slam.nickname)
            putExtra("slam_age", slam.age)
        }

        editSlamLauncher.launch(intent)
    }

    private val editSlamLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val data = result.data
                val position = data?.getIntExtra("slam_position", -1) ?: -1

                if (position >= 0 && position < slamList.size) {
                    val slam = slamList[position]
                    slam.name = data?.getStringExtra("slam_name") ?: slam.name

                    updateSharedPreferences()
                    slamAdapter.notifyItemChanged(position)
                    toggleNoSlamsMessage()
                }
            }
        }

    private fun deleteSlam(position: Int) {
        if (position < 0 || position >= slamList.size) return

        val builder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
        builder.setTitle("Delete Slam")
        builder.setMessage("Are you sure you want to delete this slam?")

        builder.setPositiveButton("Yes") { _, _ ->
            slamList.removeAt(position)
            updateSharedPreferences()
            slamAdapter.notifyItemRemoved(position)
            toggleNoSlamsMessage()
        }

        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }

        builder.create().show()
    }

    private fun toggleNoSlamsMessage() {
        binding.noSlamMessage.visibility = if (slamList.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun updateSharedPreferences() {
        val sharedPreferences =
            activity?.getSharedPreferences("SlambookData", AppCompatActivity.MODE_PRIVATE)
        sharedPreferences?.edit()?.apply {
            val gson = Gson()
            val updatedSlamsJson = gson.toJson(slamList)
            putString("slamList", updatedSlamsJson)
            apply()
        }
    }

    override fun onResume() {
        super.onResume()
        loadSavedSlams()
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
