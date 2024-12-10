package com.ljystamp.stamp_tour_app.view.adapter

import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.ljystamp.stamp_tour_app.R
import com.ljystamp.stamp_tour_app.api.model.SavedLocation
import com.ljystamp.stamp_tour_app.api.model.TourMapper
import com.ljystamp.stamp_tour_app.databinding.ItemMyTourBinding
import com.ljystamp.stamp_tour_app.databinding.ItemNearTourBinding
import com.ljystamp.stamp_tour_app.util.SaveResult
import com.ljystamp.stamp_tour_app.util.setOnSingleClickListener
import com.ljystamp.stamp_tour_app.view.LoginActivity
import com.ljystamp.stamp_tour_app.viewmodel.LocationTourListViewModel

class MyTourListViewHolder(
    private val binding: ItemMyTourBinding,
    private val viewModel: LocationTourListViewModel
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.run {
            btnComplete.setOnSingleClickListener {
                val currentItem = bindingAdapterPosition.takeIf { it != RecyclerView.NO_POSITION }?.let { position ->
                    (binding.root.parent as? RecyclerView)?.adapter?.let { adapter ->
                        (adapter as? MyTourListAdapter)?.currentList?.get(position)
                    }
                }

            }
        }
    }

    fun onBind(item: SavedLocation) {
        binding.run {
            Glide.with(binding.root.context)
                .load(item.image)
                .transform(MultiTransformation(CenterCrop(), RoundedCorners(12)))
                .into(ivPlaceImg)

            tvPlace.text = item.title
            tvAddr.text = item.address
        }
    }
}