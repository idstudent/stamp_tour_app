package com.ljystamp.stamp_tour_app.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.ljystamp.stamp_tour_app.api.model.TourMapper
import com.ljystamp.stamp_tour_app.databinding.ItemNearTourBinding

class SearchListAdapter(
    private val viewModel: LocationTourListViewModel,
    private val onLoginRequired: (() -> Unit)? = null
) : ListAdapter<TourMapper, SearchListViewHolder>(
    object: DiffUtil.ItemCallback<TourMapper>() {
        override fun areItemsTheSame(oldItem: TourMapper, newItem: TourMapper): Boolean {
            return oldItem.contentid == newItem.contentid
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: TourMapper, newItem: TourMapper): Boolean {
            return oldItem == newItem
        }
    }
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchListViewHolder {
        return SearchListViewHolder(
            ItemNearTourBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            viewModel,
            onLoginRequired
        )
    }

    override fun onBindViewHolder(holder: SearchListViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }
}