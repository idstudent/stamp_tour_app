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
import com.ljystamp.stamp_tour_app.databinding.ItemMyTourBinding
import com.ljystamp.stamp_tour_app.util.setOnSingleClickListener
import com.ljystamp.stamp_tour_app.view.MyTourDetailActivity
import com.ljystamp.stamp_tour_app.viewmodel.LocationTourListViewModel

class MyTourListViewHolder(
    private val binding: ItemMyTourBinding,
    private val viewModel: LocationTourListViewModel
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
            btnComplete.setOnSingleClickListener {
                val currentItem = bindingAdapterPosition.takeIf { it != RecyclerView.NO_POSITION }?.let { position ->
                    (binding.root.parent as? RecyclerView)?.adapter?.let { adapter ->
                        (adapter as? MyTourListAdapter)?.currentList?.get(position)
                    }
                }

                currentItem?.let { item ->
                    viewModel.updateVisitStatus(item.contentId) { success, message ->
                        Toast.makeText(binding.root.context, message, Toast.LENGTH_SHORT).show()
                        if (success) {
                            btnComplete.isEnabled = false
                            btnComplete.background = ContextCompat.getDrawable(
                                binding.root.context,
                                R.drawable.radius_12_2a2a2a
                            )
                        }
                    }
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