package com.ljystamp.stamp_tour_app.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.ljystamp.stamp_tour_app.api.model.TourMapper
import com.ljystamp.stamp_tour_app.databinding.ItemNearTourBinding

class NearTourListAdapter : ListAdapter<TourMapper, NearTourListViewHolder>(
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NearTourListViewHolder{
        return NearTourListViewHolder(
            ItemNearTourBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: NearTourListViewHolder, position: Int) {

        val item = getItem(position)

        holder.onBind(item)
    }
}