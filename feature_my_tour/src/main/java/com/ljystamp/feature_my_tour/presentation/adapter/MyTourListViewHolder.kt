package com.ljystamp.feature_my_tour.presentation.adapter

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.ljystamp.common.presentation.viewmodel.LocationTourListViewModel
import com.ljystamp.feature_my_tour.databinding.ItemMyTourBinding
import com.ljystamp.feature_my_tour_detail.presentation.view.MyTourDetailActivity
import com.ljystamp.stamp_tour_app.model.SavedLocation
import com.ljystamp.utils.setOnSingleClickListener

class MyTourListViewHolder(
    private val binding: ItemMyTourBinding,
    private val viewModel: LocationTourListViewModel,
    private val onStampClick: (SavedLocation) -> Unit
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
                    intent.putExtra("latitude", it.latitude)
                    intent.putExtra("longitude", it.longitude)
                    intent.putExtra("contentId", it.contentId)
                    intent.putExtra("contentTypeId", it.contentTypeId)
                    intent.putExtra("complete",false)
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