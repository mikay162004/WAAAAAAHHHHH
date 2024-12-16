package com.example.slambook_mundas


import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.slambook_mundas.databinding.SlamItemBinding

class SlamAdapter(
    private var slamList: MutableList<Slam>,
    private val context: Context, // Context to start activities
    private val onEdit: (Int) -> Unit, // Edit action callback
    private val onDelete: (Int) -> Unit,
) : RecyclerView.Adapter<SlamAdapter.SlamViewHolder>() {

    // ViewHolder class
    inner class SlamViewHolder(private val itemBinding: SlamItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(slam: Slam, position: Int) {
            // Bind nickname to TextView
            itemBinding.slamNickname.text = slam.nickname

            // Load image using Coil (with placeholders and error fallback)
            slam.imageUri?.let { uri ->
                itemBinding.slamImage.load(uri) {
                    placeholder(R.drawable.user) // Placeholder image
                    error(R.drawable.ic_launcher_background) // Fallback image
                }
            } ?: run {
                itemBinding.slamImage.setImageResource(R.drawable.ic_launcher_background)
            }

            // Edit button action
            itemBinding.editButton.setOnClickListener {
                onEdit(position)
            }

            // Delete button action
            itemBinding.deleteButton.setOnClickListener {
                onDelete(position)
            }

            // CardView click action to navigate to MySlamInfo
            itemBinding.root.setOnClickListener {
                val intent = Intent(context, MySlamInfo::class.java).apply {
                    putExtra("slam_info", slam) // Pass the Slam object
                }
                context.startActivity(intent)
            }
        }

        private fun putExtra(s: String, slam: Slam) {

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

    fun updateList(newSlamList: List<Slam>) {
        slamList.clear()
        slamList.addAll(newSlamList)
        notifyDataSetChanged()
        // Log the new list for debugging
        Log.d("SlamAdapter", "Updated list: $newSlamList")
    }
}
