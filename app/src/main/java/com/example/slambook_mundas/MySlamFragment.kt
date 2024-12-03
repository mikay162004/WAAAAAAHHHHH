package com.example.slambook_mundas

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment

class MySlamFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_my_slam, container, false)

        // Find the ImageView by ID (the one that should trigger the NewSlam activity)
        val createSlamButton: ImageView = view.findViewById(R.id.create_slam)

        // Set OnClickListener for the ImageView
        createSlamButton.setOnClickListener {
            // Start the NewSlam activity when the ImageView is clicked
            val intent = Intent(activity, NewSlam::class.java)
            intent.putExtra("isFriend", false)  // Optionally pass data like this
            startActivity(intent)
        }

        return view
    }

    companion object {
        // Simplified newInstance method
        fun newInstance(): MySlamFragment {
            return MySlamFragment()
        }
    }
}
