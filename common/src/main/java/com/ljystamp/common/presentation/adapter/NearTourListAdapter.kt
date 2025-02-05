package com.ljystamp.common.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.ljystamp.common.databinding.ItemNearTourBinding
import com.ljystamp.common.presentation.viewmodel.LocationTourListViewModel
import com.ljystamp.stamp_tour_app.model.TourMapper


class NearTourListAdapter(
    private val viewModel: LocationTourListViewModel,
    private val onLoginRequired: (() -> Unit)? = null
) : ListAdapter<TourMapper, NearTourListViewHolder>(
    object: DiffUtil.ItemCallback<TourMapper>() {
        override fun areItemsTheSame(oldItem: TourMapper, newItem: TourMapper): Boolean {
            return oldItem == newItem
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: TourMapper, newItem: TourMapper): Boolean {
            return oldItem == newItem
        }
    }
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NearTourListViewHolder {
        return NearTourListViewHolder(
            ItemNearTourBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            viewModel,
            onLoginRequired
        )
    }

    override fun onBindViewHolder(holder: NearTourListViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }
}