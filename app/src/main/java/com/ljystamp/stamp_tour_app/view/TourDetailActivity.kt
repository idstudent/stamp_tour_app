package com.ljystamp.stamp_tour_app.view

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.ljystamp.stamp_tour_app.R
import com.ljystamp.stamp_tour_app.api.model.TourMapper
import com.ljystamp.stamp_tour_app.databinding.ActivityTourDetailBinding
import com.ljystamp.stamp_tour_app.util.SaveResult
import com.ljystamp.stamp_tour_app.util.removeHtmlTags
import com.ljystamp.stamp_tour_app.util.setOnSingleClickListener
import com.ljystamp.stamp_tour_app.view.user.LoginActivity
import com.ljystamp.stamp_tour_app.viewmodel.LocationTourListViewModel
import com.ljystamp.stamp_tour_app.viewmodel.TourDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TourDetailActivity: BaseActivity<ActivityTourDetailBinding>() {
    private val tourDetailViewModel: TourDetailViewModel by viewModels()
    private val locationTourListViewModel: LocationTourListViewModel by viewModels()

    private var tourInfo: TourMapper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        initListener()
    }

    private fun initView() {
        val intent = intent
        tourInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("info", TourMapper::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("info")
        }

        tourInfo?.let {
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    tourDetailViewModel.getTourDetail(it.contentid, it.contenttypeid).collect { detailInfo ->
                        binding.run {
                            Glide.with(binding.root.context)
                                .load(it.firstimage)
                                .into(ivThumb)

                            tvTitle.text = it.title
                            tvAddr.text = it.addr1

                            detailInfo[0].run {
                                tvOpenDate.isVisible = this.openDate != ""
                                tvOpenDate.text = "개장일: ${this.openDate}".removeHtmlTags()

                                tvRestDate.isVisible = this.restDate != ""
                                tvRestDate.text = "휴무일: ${this.restDate}".removeHtmlTags()

                                tvUseTime.isVisible = this.useTime != ""
                                tvUseTime.text = "이용 가능 시간: ${this.useTime}".removeHtmlTags()
                            }

                            isSavedCheck()
                        }
                    }
                }
            }
        }
    }

    private fun initListener() {
        binding.btnComplete.setOnSingleClickListener {
            tourInfo?.let { info ->
                locationTourListViewModel.saveTourLocation(info) { result ->
                    when(result) {
                        is SaveResult.Success -> {
                            Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                            finish()
                        }

                        is SaveResult.Failure -> {
                            Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                        }

                        is SaveResult.MaxLimitReached -> {
                            Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                        }

                        is SaveResult.LoginRequired -> {
                            Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                            handleLoginRequest()
                        }
                    }
                }
            }
        }
    }

    private fun isSavedCheck() {
        tourInfo?.contentid?.let {
            locationTourListViewModel.checkIfLocationSaved(it) { isSaved ->
                if(isSaved) {
                    binding.btnComplete.background = ContextCompat.getDrawable(binding.root.context, R.drawable.radius_12_3d3d3d)
                } else {
                    binding.btnComplete.background  = ContextCompat.getDrawable(binding.root.context, R.drawable.radius_12_ff8c00)
                }
            }
        }
    }
    private fun handleLoginRequest() {
        val intent = Intent(this, LoginActivity::class.java)
        activityResultLauncher.launch(intent)
    }

    private val activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            isSavedCheck()
        }
    }
    override fun getViewBinding(): ActivityTourDetailBinding {
        return ActivityTourDetailBinding.inflate(layoutInflater)
    }
}