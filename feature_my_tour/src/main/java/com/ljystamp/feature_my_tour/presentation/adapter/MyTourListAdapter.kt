package com.ljystamp.feature_my_tour.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.ljystamp.common.presentation.viewmodel.LocationTourListViewModel
import com.ljystamp.feature_my_tour.databinding.ItemMyTourBinding
import com.ljystamp.stamp_tour_app.model.SavedLocation

class MyTourListAdapter(
    private val viewModel: LocationTourListViewModel,
    private val onStampClick: (SavedLocation) -> Unit
) : ListAdapter<SavedLocation, MyTourListViewHolder>(
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
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyTourListViewHolder {
        return MyTourListViewHolder(
            ItemMyTourBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            viewModel,
            onStampClick
        )
    }

    override fun onBindViewHolder(holder: MyTourListViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }
}