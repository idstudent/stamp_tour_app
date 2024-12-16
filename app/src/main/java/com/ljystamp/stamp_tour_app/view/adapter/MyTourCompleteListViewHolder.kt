package com.ljystamp.stamp_tour_app.view.adapter

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.ljystamp.stamp_tour_app.api.model.SavedLocation
import com.ljystamp.stamp_tour_app.databinding.ItemCompleteMyTourBinding
import com.ljystamp.stamp_tour_app.util.setOnSingleClickListener
import com.ljystamp.stamp_tour_app.view.MyTourDetailActivity

class MyTourCompleteListViewHolder(
    private val binding: ItemCompleteMyTourBinding
) : RecyclerView.ViewHolder(binding.root) {
    private var item: SavedLocation? = null

    init {
        binding.run {
            binding.root.setOnSingleClickListener {
                item?.let {
                    val intent = Intent(binding.root.context, MyTourDetailActivity::class.java)
                    intent.putExtra("title", it.title)
                    intent.putExtra("addr", it.address)
                    intent.putExtra("url", it.image)
                    intent.putExtra("contentId", it.contentId)
                    intent.putExtra("contentTypeId", it.contentTypeId)
                    binding.root.context.startActivity(intent)
                }
            }
        }
    }

    fun onBind(item: SavedLocation) {
        this.item = item

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