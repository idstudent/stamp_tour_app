package com.ljystamp.stamp_tour_app.view.adapter

import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.ljystamp.stamp_tour_app.R
import com.ljystamp.stamp_tour_app.api.model.SavedLocation
import com.ljystamp.stamp_tour_app.databinding.ItemTodayStampBinding
import com.ljystamp.stamp_tour_app.util.setOnSingleClickListener
import com.ljystamp.stamp_tour_app.view.MyTourDetailActivity

class SavedLocationsViewHolder(
    private val binding: ItemTodayStampBinding
) : RecyclerView.ViewHolder(binding.root) {
    init {
        binding.root.setOnSingleClickListener {
            val intent = Intent(binding.root.context, MyTourDetailActivity::class.java)
            binding.root.context.startActivity(intent)
        }
    }
    fun bind(item: SavedLocation) {
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