package com.example.slambook_mundas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FriendSlamAdapter(
    private val friendSlams: List<HashMap<String, String>>
) : RecyclerView.Adapter<FriendSlamAdapter.FriendSlamViewHolder>() {

    // Create the ViewHolder by inflating the slam_item layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendSlamViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.slam_item, parent, false)
        return FriendSlamViewHolder(view)
    }

    // Bind data to the ViewHolder
    override fun onBindViewHolder(holder: FriendSlamViewHolder, position: Int) {
        val slam = friendSlams[position]
        holder.bind(slam)
    }

    // Return the number of items in the list
    override fun getItemCount(): Int = friendSlams.size

    // ViewHolder class to bind views from slam_item.xml
    class FriendSlamViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.slamName) // Make sure these are in slam_item.xml
        private val nicknameTextView: TextView = itemView.findViewById(R.id.slamNickname)
        private val messageTextView: TextView = itemView.findViewById(R.id.slamMessage)

        // Bind data from the HashMap to the views
        fun bind(slam: HashMap<String, String>) {
            nameTextView.text = slam["name"] ?: "No Name"
            nicknameTextView.text = slam["nickname"] ?: "No Nickname"
            messageTextView.text = slam["message"] ?: "No Message"
        }
    }
}
