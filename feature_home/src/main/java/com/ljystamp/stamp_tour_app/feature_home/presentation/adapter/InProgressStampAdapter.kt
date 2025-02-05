package com.ljystamp.stamp_tour_app.feature_home.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.ljystamp.feature_home.databinding.ItemTodayStampBinding
import com.ljystamp.stamp_tour_app.model.SavedLocation

class InProgressStampAdapter(
    private val onStampClick: (SavedLocation) -> Unit
): ListAdapter<SavedLocation, SavedLocationsViewHolder>(
    object: DiffUtil.ItemCallback<SavedLocation>() {
        override fun areItemsTheSame(oldItem: SavedLocation, newItem: SavedLocation): Boolean {
            return oldItem == newItem
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: SavedLocation, newItem: SavedLocation): Boolean {
            return oldItem == newItem
        }
    }
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedLocationsViewHolder {
        val binding = ItemTodayStampBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return SavedLocationsViewHolder(binding,onStampClick)
    }

    override fun onBindViewHolder(holder: SavedLocationsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}