package com.example.slambook_mundas

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.slambook_mundas.databinding.SlamItemBinding

class FriendSlamAdapter(
    private var slamList: MutableList<FriendSlamDataClass>,
    private val onEdit: (Int) -> Unit,
    private val onDelete: (Int) -> Unit,
    private val onView: (Int) -> Unit // New listener for viewing slam details
) : RecyclerView.Adapter<FriendSlamAdapter.SlamViewHolder>() {

    // ViewHolder class
    inner class SlamViewHolder(private val itemBinding: SlamItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(slam: FriendSlamDataClass, position: Int) {
            itemBinding.slamNickname.text = slam.nickname

            // Load image using Coil
            slam.imageUri?.let { uri ->
                itemBinding.slamImage.load(uri) {
                    placeholder(R.drawable.user) // Placeholder image
                    error(R.drawable.ic_launcher_background) // Fallback image
                }
            } ?: run {
                itemBinding.slamImage.setImageResource(R.drawable.ic_launcher_background)
            }

            // Set click listeners for edit and delete actions
            itemBinding.editButton.setOnClickListener { onEdit(position) }
            itemBinding.deleteButton.setOnClickListener { onDelete(position) }

            // Set click listener for viewing slam details
            itemBinding.root.setOnClickListener { onView(position) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlamViewHolder {
        val itemBinding =
            SlamItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SlamViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: SlamViewHolder, position: Int) {
        holder.bind(slamList[position], position)
    }

    override fun getItemCount(): Int = slamList.size

    // Method to remove an item from the list
    fun removeItem(position: Int) {
        slamList.removeAt(position)
        notifyItemRemoved(position)
    }
}
