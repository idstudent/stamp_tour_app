package com.ljystamp.stamp_tour_app.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.ljystamp.stamp_tour_app.databinding.ActivityMyTourDetailBinding
import com.ljystamp.stamp_tour_app.util.removeHtmlTags
import com.ljystamp.stamp_tour_app.viewmodel.TourDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyTourDetailActivity: BaseActivity<ActivityMyTourDetailBinding>() {
    private val tourDetailViewModel: TourDetailViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = intent
        val title = intent.getStringExtra("title") ?: ""
        val addr = intent.getStringExtra("addr") ?: ""
        val imgUrl = intent.getStringExtra("url") ?: ""
        val contentId = intent.getIntExtra("contentId", -1)
        val contentTypeId = intent.getIntExtra("contentTypeId", -1)

        if(contentId != -1 && contentTypeId != -1) {
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    tourDetailViewModel.getTourDetail(contentId, contentTypeId).collect {
                        binding.run {
                            Glide.with(binding.root.context)
                                .load(imgUrl)
                                .into(ivThumb)

                            if(it.isNotEmpty()) {
                                it[0].run {
                                    tvTitle.text = title
                                    tvAddr.text = addr

                                    tvOpenDate.isVisible = this.openDate != ""
                                    tvOpenDate.text = "개장일: ${this.openDate}".removeHtmlTags()

                                    tvRestDate.isVisible = this.restDate != ""
                                    tvRestDate.text = "휴무일: ${this.restDate}".removeHtmlTags()

                                    tvUseTime.isVisible = this.useTime != ""
                                    tvUseTime.text = "이용 가능 시간: ${this.useTime}".removeHtmlTags()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun getViewBinding(): ActivityMyTourDetailBinding {
        return ActivityMyTourDetailBinding.inflate(layoutInflater)
    }
}