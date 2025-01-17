package com.ljystamp.stamp_tour_app.view.adapter

import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.gms.location.FusedLocationProviderClient
import com.ljystamp.stamp_tour_app.R
import com.ljystamp.stamp_tour_app.api.model.SavedLocation
import com.ljystamp.stamp_tour_app.databinding.ItemTodayStampBinding
import com.ljystamp.stamp_tour_app.util.setOnSingleClickListener
import com.ljystamp.stamp_tour_app.view.MyTourDetailActivity
import com.ljystamp.stamp_tour_app.viewmodel.LocationTourListViewModel

class SavedLocationsViewHolder(
    private val binding: ItemTodayStampBinding,
    private val viewModel: LocationTourListViewModel,
    private val onStampClick: (SavedLocation) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    private var item: SavedLocation? = null

    init {
        binding.run {

            root.setOnSingleClickListener {
                item?.let {
                    val intent = Intent(root.context, MyTourDetailActivity::class.java)
                    intent.putExtra("title", it.title)
                    intent.putExtra("addr", it.address)
                    intent.putExtra("url", it.image)
                    intent.putExtra("contentId", it.contentId)
                    intent.putExtra("contentTypeId", it.contentTypeId)
                    binding.root.context.startActivity(intent)
                }
            }
            btnComplete.setOnSingleClickListener {
                item?.let {
                    onStampClick(it)
                }
            }
        }
    }
    fun bind(item: SavedLocation) {
        this.item = item

        binding.run {
            Glide.with(root.context)
                .load(item.image)
                .transform(MultiTransformation(CenterCrop(), RoundedCorners(12)))
                .into(ivPlace)

            tvTitle.text = item.title
            tvAddr.text = item.address
            if(item.isVisited) {
                btnComplete.isEnabled = false
                btnComplete.background = ContextCompat.getDrawable(root.context, R.drawable.radius_12_2a2a2a)
            }else {
                btnComplete.isEnabled = true
                btnComplete.background = ContextCompat.getDrawable(root.context, R.drawable.radius_12_ff8c00)
            }
        }
    }
}