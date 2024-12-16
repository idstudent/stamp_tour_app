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
    private var myCultureCompleteListAdapter = MyTourCompleteListAdapter()
    private var myEventCompleteListAdapter = MyTourCompleteListAdapter()
    private var myActivityCompleteListAdapter = MyTourCompleteListAdapter()
    private var myFoodCompleteListAdapter = MyTourCompleteListAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
    }
    private fun initView() {
        val intent = intent
        val completeTourList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13 (API 33) 이상
            intent.getParcelableArrayListExtra("tourList", SavedLocation::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableArrayListExtra("tourList")
        } ?: arrayListOf()

        val completeCultureList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13 (API 33) 이상
            intent.getParcelableArrayListExtra("cultureList", SavedLocation::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableArrayListExtra("cultureList")
        } ?: arrayListOf()

        val completeEventList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13 (API 33) 이상
            intent.getParcelableArrayListExtra("eventList", SavedLocation::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableArrayListExtra("eventList")
        } ?: arrayListOf()

        Log.e("ljy", "뭐야 이벤트 $completeEventList")

        val completeActivityList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13 (API 33) 이상
            intent.getParcelableArrayListExtra("activityList", SavedLocation::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableArrayListExtra("activityList")
        } ?: arrayListOf()

        val completeFoodList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13 (API 33) 이상
            intent.getParcelableArrayListExtra("foodList", SavedLocation::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableArrayListExtra("foodList")
        } ?: arrayListOf()

        binding.run {
            rvTourComplete.layoutManager = GridLayoutManager(this@MyCompleteListActivity, 2)
            rvTourComplete.adapter = myTourCompleteListAdapter
            myTourCompleteListAdapter.submitList(completeTourList.filter { it.isVisited })

            rvCultureComplete.layoutManager = GridLayoutManager(this@MyCompleteListActivity, 2)
            rvCultureComplete.adapter = myCultureCompleteListAdapter
            myCultureCompleteListAdapter.submitList(completeCultureList.filter { it.isVisited })

            rvEventComplete.layoutManager = GridLayoutManager(this@MyCompleteListActivity, 2)
            rvEventComplete.adapter = myEventCompleteListAdapter
            myEventCompleteListAdapter.submitList(completeEventList.filter { it.isVisited })

            rvActivityComplete.layoutManager = GridLayoutManager(this@MyCompleteListActivity, 2)
            rvActivityComplete.adapter = myActivityCompleteListAdapter
            myActivityCompleteListAdapter.submitList(completeActivityList.filter { it.isVisited })

            rvFoodComplete.layoutManager = GridLayoutManager(this@MyCompleteListActivity, 2)
            rvFoodComplete.adapter = myFoodCompleteListAdapter
            myFoodCompleteListAdapter.submitList(completeFoodList.filter { it.isVisited })
        }

    }

    override fun getViewBinding(): ActivityMyCompleteListBinding {
        return ActivityMyCompleteListBinding.inflate(layoutInflater)
    }
}