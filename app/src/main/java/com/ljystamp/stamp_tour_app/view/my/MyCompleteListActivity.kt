package com.ljystamp.stamp_tour_app.view.my

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import com.ljystamp.stamp_tour_app.api.model.SavedLocation
import com.ljystamp.stamp_tour_app.databinding.ActivityMyCompleteListBinding
import com.ljystamp.stamp_tour_app.view.BaseActivity
import com.ljystamp.stamp_tour_app.view.adapter.MyTourCompleteListAdapter

class MyCompleteListActivity: BaseActivity<ActivityMyCompleteListBinding>() {
    private var myTourCompleteListAdapter = MyTourCompleteListAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = intent
        val completeList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13 (API 33) 이상
            intent.getParcelableArrayListExtra("list", SavedLocation::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableArrayListExtra<SavedLocation>("list")
        } ?: arrayListOf()

        binding.run {
            rvComplete.layoutManager = GridLayoutManager(this@MyCompleteListActivity, 2)
            rvComplete.adapter = myTourCompleteListAdapter
            myTourCompleteListAdapter.submitList(completeList.filter { it.isVisited })
        }
    }

    override fun getViewBinding(): ActivityMyCompleteListBinding {
        return ActivityMyCompleteListBinding.inflate(layoutInflater)
    }
}