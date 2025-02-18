package com.ljystamp.feature_my.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.ljystamp.feature_my.databinding.ItemCompleteMyTourBinding
import com.ljystamp.stamp_tour_app.model.SavedLocation


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