package com.example.slambook_mundas

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.slambook_mundas.databinding.SlamItemBinding

class SlamAdapter(
    private val slamList: ArrayList<Slam>,
    private val deleteSlam: (Int) -> Unit
) : RecyclerView.Adapter<SlamAdapter.SlamViewHolder>() {

    inner class SlamViewHolder(binding: SlamItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val slamText = binding.slamText
        val deleteButton = binding.deleteButton
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlamViewHolder {
        val binding = SlamItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SlamViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SlamViewHolder, position: Int) {
        val slam = slamList[position]
        holder.slamText.text = """
            Name: ${slam.name}
            Nickname: ${slam.nickname}
            Age: ${slam.age}
            Birthday: ${slam.birthday}
            Zodiac Sign: ${slam.zodiacSign}
            Hobbies: ${slam.hobbies}
            Favorites: ${slam.favorites}
        """.trimIndent()

        holder.deleteButton.setOnClickListener {
            deleteSlam(position)
        }
    }

    override fun getItemCount(): Int = slamList.size
}
