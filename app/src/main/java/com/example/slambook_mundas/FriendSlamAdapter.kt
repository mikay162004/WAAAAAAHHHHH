package com.example.slambook_mundas

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.slambook_mundas.databinding.SlamItemBinding

class FriendSlamAdapter(
    private val slamList: ArrayList<Slam>,
    private val deleteSlam: (Int) -> Unit
) : RecyclerView.Adapter<FriendSlamAdapter.FriendSlamViewHolder>() {

    inner class FriendSlamViewHolder(binding: SlamItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val slamText = binding.slamText  // Correctly reference slamText
        val deleteButton = binding.deleteButton  // Correctly reference deleteButton
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendSlamViewHolder {
        val binding = SlamItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FriendSlamViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FriendSlamViewHolder, position: Int) {
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
