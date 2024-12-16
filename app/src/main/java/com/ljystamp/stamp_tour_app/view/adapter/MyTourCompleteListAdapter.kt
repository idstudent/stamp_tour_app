package com.ljystamp.stamp_tour_app.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.ljystamp.stamp_tour_app.api.model.SavedLocation
import com.ljystamp.stamp_tour_app.databinding.ItemCompleteMyTourBinding


class MyTourCompleteListAdapter : ListAdapter<SavedLocation, MyTourCompleteListViewHolder>(
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
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyTourCompleteListViewHolder {
        return MyTourCompleteListViewHolder(
            ItemCompleteMyTourBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyTourCompleteListViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }
}