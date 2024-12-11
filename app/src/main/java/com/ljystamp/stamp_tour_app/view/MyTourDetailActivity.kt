package com.ljystamp.stamp_tour_app.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.ljystamp.stamp_tour_app.R
import com.ljystamp.stamp_tour_app.databinding.ActivityMyTourDetailBinding
import com.ljystamp.stamp_tour_app.util.removeHtmlTags
import com.ljystamp.stamp_tour_app.util.setOnSingleClickListener
import com.ljystamp.stamp_tour_app.viewmodel.LocationTourListViewModel
import com.ljystamp.stamp_tour_app.viewmodel.TourDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyTourDetailActivity: BaseActivity<ActivityMyTourDetailBinding>() {
    private val tourDetailViewModel: TourDetailViewModel by viewModels()
    private val locationTourListViewModel: LocationTourListViewModel by viewModels()

    private var contentId = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        initListener()
    }

    private fun initView() {
        val intent = intent
        val title = intent.getStringExtra("title") ?: ""
        val addr = intent.getStringExtra("addr") ?: ""
        val imgUrl = intent.getStringExtra("url") ?: ""
        contentId = intent.getIntExtra("contentId", -1)
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

    private fun initListener() {
        binding.btnComplete.setOnSingleClickListener {
            locationTourListViewModel.updateVisitStatus(contentId) { success, message ->
                Toast.makeText(binding.root.context, message, Toast.LENGTH_SHORT).show()
                if (success) {
                    finish()
                }
            }
        }
    }
    override fun getViewBinding(): ActivityMyTourDetailBinding {
        return ActivityMyTourDetailBinding.inflate(layoutInflater)
    }
}