package com.ljystamp.feature_search.presentation.adapter

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.ljystamp.common.presentation.viewmodel.LocationTourListViewModel
import com.ljystamp.core_ui.R
import com.ljystamp.feature_search.databinding.ItemNearBinding
import com.ljystamp.feature_tour_detail.presentation.view.TourDetailActivity
import com.ljystamp.stamp_tour_app.model.SaveResult
import com.ljystamp.stamp_tour_app.model.TourMapper
import com.ljystamp.utils.setOnSingleClickListener

class SearchListViewHolder(
    private val binding: ItemNearBinding,
    private val viewModel: LocationTourListViewModel,
    private val onLoginRequired: (() -> Unit)? = null
) : RecyclerView.ViewHolder(binding.root) {
    private var item: TourMapper? = null
    init {
        binding.run {
            binding.root.setOnSingleClickListener {
                item?.let {
                    Log.e("ljy", "Info $item")
                    val intent = Intent(binding.root.context, TourDetailActivity::class.java)
                    intent.putExtra("info",  item)
                    intent.putExtra("search", true)
                    binding.root.context.startActivity(intent)
                }
            }

//            btnAdd.setOnSingleClickListener { view ->
//                val currentItem = bindingAdapterPosition.takeIf { it != RecyclerView.NO_POSITION }?.let { position ->
//                    (binding.root.parent as? RecyclerView)?.adapter?.let { adapter ->
//                        (adapter as? SearchListAdapter)?.currentList?.get(position)
//                    }
//                }
//
//                currentItem?.let { item ->
//                    viewModel.checkIfLocationSaved(item.contentId) { isSaved ->
//                        if (isSaved) {
//                            btnAdd.background = ContextCompat.getDrawable(binding.root.context, R.drawable.radius_12_3d3d3d)
//                        } else {
//                            btnAdd.background = ContextCompat.getDrawable(binding.root.context, R.drawable.radius_12_ff8c00)
//                            viewModel.saveTourLocation(item) { result ->
//                                when(result) {
//                                    is SaveResult.Success -> {
//                                        btnAdd.isEnabled = false
//                                        btnAdd.background = ContextCompat.getDrawable(binding.root.context, R.drawable.radius_12_3d3d3d)
//                                        Toast.makeText(view.context, result.message, Toast.LENGTH_SHORT).show()
//                                    }
//                                    is SaveResult.Failure -> {
//                                        Toast.makeText(view.context, result.message, Toast.LENGTH_SHORT).show()
//                                    }
//                                    is SaveResult.MaxLimitReached -> {
//                                        Toast.makeText(view.context, result.message, Toast.LENGTH_SHORT).show()
//                                    }
//                                    is SaveResult.LoginRequired -> {
//                                        Toast.makeText(view.context, result.message, Toast.LENGTH_SHORT).show()
//                                        onLoginRequired?.let { it() }
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
            }
        }
    }

    fun onBind(item: TourMapper) {
//        this.item = item
//
//        binding.run {
//            Glide.with(binding.root.context)
//                .load(item.firstImage)
//                .transform(MultiTransformation(CenterCrop(), RoundedCorners(12)))
//                .into(ivPlaceImg)
//
//            tvPlace.text = item.title
//            tvAddr.text = item.addr1
//
//
//            viewModel.checkIfLocationSaved(item.contentId) { isSaved ->
//                btnAdd.isEnabled = !isSaved
//                btnAdd.background = if (isSaved) {
//                    ContextCompat.getDrawable(binding.root.context, R.drawable.radius_12_3d3d3d)
//                } else {
//                    ContextCompat.getDrawable(binding.root.context, R.drawable.radius_12_ff8c00)
//                }
//            }
//        }
//    }
}