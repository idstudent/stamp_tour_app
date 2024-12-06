package com.ljystamp.stamp_tour_app.view.adapter

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.ljystamp.stamp_tour_app.api.model.TourMapper
import com.ljystamp.stamp_tour_app.databinding.ItemNearTourBinding

class NearTourListViewHolder(
    private val binding : ItemNearTourBinding
) : RecyclerView.ViewHolder(binding.root){

    init {
        binding.run {
//            root.setOnSingleClickListener {
//                item?.let {
//                    val intent = Intent(binding.root.context, BookDetailActivity::class.java)
//                    intent.putExtra("isbn", it.isbn)
//                    if(it.categoryId == "200") {
//                        intent.putExtra("searchType", "foreign")
//                    }
//                    this.root.context.startActivity(intent)
//                }
//            }
        }
    }

    fun onBind(item : TourMapper) {
        Log.e("ljy", "아이템뭐냐 $item")
        binding.run {
            Glide.with(binding.root.context)
                .load(item.firstimage)
                .transform(MultiTransformation(CenterCrop(), RoundedCorners(12)))
                .into(ivPlaceImg)

            tvPlace.text = item.title
            tvAddr.text = item.addr1
        }
    }
}