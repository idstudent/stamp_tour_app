package com.ljystamp.stamp_tour_app.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.ljystamp.stamp_tour_app.R
import com.ljystamp.stamp_tour_app.api.model.SavedLocation
import com.ljystamp.stamp_tour_app.databinding.ItemTodayStampBinding

class SavedLocationsAdapter: ListAdapter<SavedLocation, SavedLocationsViewHolder>(
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
        return SavedLocationsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SavedLocationsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}