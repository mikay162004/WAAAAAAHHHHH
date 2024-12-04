package com.example.slambook_mundas

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.slambook_mundas.databinding.SlamItemBinding

class SlamAdapter(
    private val slamList: MutableList<Slam>,
    private val deleteSlam: (Int) -> Unit,
    private val editSlam: (Int) -> Unit
) : RecyclerView.Adapter<SlamAdapter.SlamViewHolder>() {

    inner class SlamViewHolder(binding: SlamItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val slamText = binding.slamText
        val deleteButton = binding.deleteButton
        val editButton = binding.editButton
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
            Gender: ${slam.gender}
            Birthday: ${slam.birthday}
            Zodiac Sign: ${slam.zodiacSign}
            Hobbies: ${slam.hobbies}
            Favorites: ${slam.favorites}
        """.trimIndent()

        holder.deleteButton.setOnClickListener {
            deleteSlam(position)  // Handle delete
        }

        // Handle edit button click
        holder.editButton.setOnClickListener {
            // Send slam data and position to the FriendSlam activity
            val context = holder.itemView.context
            val intent = Intent(context, FriendSlam::class.java)
            intent.putExtra("slamName", slam.name)
            intent.putExtra("slamNickname", slam.nickname)
            intent.putExtra("slamAge", slam.age)
            intent.putExtra("slamGender", slam.gender)
            intent.putExtra("slamBirthday", slam.birthday)
            intent.putExtra("slamZodiac Sign", slam.zodiacSign)
            intent.putExtra("slamHobbies", slam.hobbies)
            intent.putExtra("slamFavorites", slam.favorites)
            intent.putExtra("slamPosition", position) // Pass position for editing
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = slamList.size
}
