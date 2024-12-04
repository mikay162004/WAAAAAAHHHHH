package com.example.slambook_mundas

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.slambook_mundas.databinding.SlamItemBinding

class FriendSlamAdapter(
    private val slamList: MutableList<FriendSlamDataClass>,
    private val deleteSlam: (Int) -> Unit,
    private val editSlam: (Int) -> Unit
) : RecyclerView.Adapter<FriendSlamAdapter.FriendSlamViewHolder>() {

    inner class FriendSlamViewHolder(binding: SlamItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val slamText = binding.slamText
        val deleteButton = binding.deleteButton
        val editButton = binding.editButton
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
            Message: ${slam.message}
        """.trimIndent()

        // Handle delete button click
        holder.deleteButton.setOnClickListener {
            deleteSlam(position) // Pass position to delete function
        }

        // Handle edit button click
        holder.editButton.setOnClickListener {
            // Send slam data and position to the FriendSlam activity
            val context = holder.itemView.context
            val intent = Intent(context, FriendSlam::class.java)
            intent.putExtra("slamName", slam.name)
            intent.putExtra("slamNickname", slam.nickname)
            intent.putExtra("slamMessage", slam.message)
            intent.putExtra("slamPosition", position) // Pass position for editing
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = slamList.size
}
