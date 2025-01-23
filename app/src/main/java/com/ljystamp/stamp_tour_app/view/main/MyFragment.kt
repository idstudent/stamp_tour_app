package com.ljystamp.stamp_tour_app.view.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.ljystamp.stamp_tour_app.api.model.SavedLocation
import com.ljystamp.stamp_tour_app.databinding.FragmentMyBinding
import com.ljystamp.stamp_tour_app.util.setOnSingleClickListener
import com.ljystamp.stamp_tour_app.view.BaseFragment
import com.ljystamp.stamp_tour_app.view.home.MyTourListActivity
import com.ljystamp.stamp_tour_app.view.my.MyCertificationActivity
import com.ljystamp.stamp_tour_app.view.my.MyCompleteListActivity
import com.ljystamp.stamp_tour_app.view.my.SettingActivity
import com.ljystamp.stamp_tour_app.view.user.LoginActivity
import com.ljystamp.stamp_tour_app.view.user.model.CategoryLevel
import com.ljystamp.stamp_tour_app.view.user.model.LevelInfo
import com.ljystamp.stamp_tour_app.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyFragment : BaseFragment<FragmentMyBinding>() {
    private val userViewModel: UserViewModel by viewModels()

    private var saveTourList = ArrayList<SavedLocation>()
    private var saveCultureList = ArrayList<SavedLocation>()
    private var saveEventList = ArrayList<SavedLocation>()
    private var saveActivityList = ArrayList<SavedLocation>()
    private var saveFoodList = ArrayList<SavedLocation>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initListener()
    }

    override fun onResume() {
        super.onResume()

        userViewModel.getUserProfileAndSavedLocations()
    }

    private fun initView() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                // 프로필 수집
                launch {
                    userViewModel.userProfile.collectLatest { profile ->
                        profile?.let {
                            binding.tvNickName.text = it["nickname"] as? String
                            binding.tvLogout.visibility = View.VISIBLE
                        }
                    }
                }

                launch {
                    userViewModel.allList.collectLatest {
                        binding.tvCompleteCount.text = it.filter { it.isVisited }.size.toString()
                        binding.tvNotCompleteCount.text =
                            it.filter { !it.isVisited }.size.toString()
                    }
                }
                // 관광 리스트 수집
                launch {
                    userViewModel.tourPlaceList.collectLatest { list ->
                        val visitedCount = list.count { it.isVisited }
                        val levelInfo = calculateLevel(visitedCount, CategoryLevel.TOUR)

                        saveTourList = ArrayList(list)

                        binding.apply {
                            tvTourLevelName.text = levelInfo.level
                            tvTourCount.text = levelInfo.currentCount.toString()
                            tvTourTotalCount.text = "/ ${levelInfo.targetCount}"
                            tourProgress.progress = levelInfo.progress
                            tvTourLevelSubTitle.text = CategoryLevel.TOUR.subTitleFormat.format(
                                if (levelInfo.level == CategoryLevel.TOUR.beginnerLevel) 10
                                else if (levelInfo.level == CategoryLevel.TOUR.intermediateLevel) 30
                                else if (levelInfo.level == CategoryLevel.TOUR.intermediateLevel) 50
                                else 100
                            )
                        }
                    }
                }

                // 문화 리스트 수집
                launch {
                    userViewModel.cultureList.collectLatest { list ->
                        val visitedCount = list.count { it.isVisited }
                        val levelInfo = calculateLevel(visitedCount, CategoryLevel.CULTURE)

                        saveCultureList = ArrayList(list)

                        binding.apply {
                            tvCultureLevelName.text = levelInfo.level
                            tvCultureCount.text = levelInfo.currentCount.toString()
                            tvCultureTotalCount.text = "/ ${levelInfo.targetCount}"
                            cultureProgress.progress = levelInfo.progress
                            tvCultureLevelSubTitle.text =
                                CategoryLevel.CULTURE.subTitleFormat.format(
                                    if (levelInfo.level == CategoryLevel.CULTURE.beginnerLevel) 10
                                    else if (levelInfo.level == CategoryLevel.CULTURE.intermediateLevel) 30
                                    else if (levelInfo.level == CategoryLevel.CULTURE.intermediateLevel) 50
                                    else 100
                                )
                        }
                    }
                }

                // 축제 리스트 수집
                launch {
                    userViewModel.eventList.collectLatest { list ->
                        val visitedCount = list.count { it.isVisited }
                        val levelInfo = calculateLevel(visitedCount, CategoryLevel.EVENT)

                        saveEventList = ArrayList(list)

                        binding.apply {
                            tvEventLevelName.text = levelInfo.level
                            tvEventCount.text = levelInfo.currentCount.toString()
                            tvEventTotalCount.text = "/ ${levelInfo.targetCount}"
                            eventProgress.progress = levelInfo.progress
                            tvEventLevelSubTitle.text = CategoryLevel.EVENT.subTitleFormat.format(
                                if (levelInfo.level == CategoryLevel.EVENT.beginnerLevel) 10
                                else if (levelInfo.level == CategoryLevel.EVENT.intermediateLevel) 30
                                else if (levelInfo.level == CategoryLevel.EVENT.intermediateLevel) 50
                                else 100
                            )
                        }
                    }
                }

                // 액티비티 리스트 수집
                launch {
                    userViewModel.activityList.collectLatest { list ->
                        val visitedCount = list.count { it.isVisited }
                        val levelInfo = calculateLevel(visitedCount, CategoryLevel.ACTIVITY)

                        saveActivityList = ArrayList(list)

                        binding.apply {
                            tvActivityLevelName.text = levelInfo.level
                            tvActivityCount.text = levelInfo.currentCount.toString()
                            tvActivityTotalCount.text = "/ ${levelInfo.targetCount}"
                            activityProgress.progress = levelInfo.progress
                            tvActivityLevelSubTitle.text =
                                CategoryLevel.ACTIVITY.subTitleFormat.format(
                                    if (levelInfo.level == CategoryLevel.ACTIVITY.beginnerLevel) 10
                                    else if (levelInfo.level == CategoryLevel.ACTIVITY.intermediateLevel) 30
                                    else if (levelInfo.level == CategoryLevel.ACTIVITY.intermediateLevel) 50
                                    else 100
                                )
                        }
                    }
                }

                // 음식 리스트 수집
                launch {
                    userViewModel.foodList.collectLatest { list ->
                        val visitedCount = list.count { it.isVisited }
                        val levelInfo = calculateLevel(visitedCount, CategoryLevel.FOOD)

                        saveFoodList = ArrayList(list)

                        binding.apply {
                            tvFoodLevelName.text = levelInfo.level
                            tvFoodCount.text = levelInfo.currentCount.toString()
                            tvFoodTotalCount.text = "/ ${levelInfo.targetCount}"
                            foodProgress.progress = levelInfo.progress
                            tvFoodLevelSubTitle.text = CategoryLevel.FOOD.subTitleFormat.format(
                                if (levelInfo.level == CategoryLevel.FOOD.beginnerLevel) 10
                                else if (levelInfo.level == CategoryLevel.FOOD.intermediateLevel) 30
                                else if (levelInfo.level == CategoryLevel.FOOD.intermediateLevel) 50
                                else 100
                            )
                        }
                    }
                }

                launch {
                    userViewModel.certificationCount.collectLatest { count ->
                        binding.tvCertificationCount.text = count.toString()
                    }
                }
            }
        }
    }

    private fun initListener() {
        binding.run {
            llComplete.setOnSingleClickListener {
                val intent = Intent(requireActivity(), MyCompleteListActivity::class.java)
                intent.putExtra("tourList", saveTourList)
                intent.putExtra("cultureList", saveCultureList)
                intent.putExtra("eventList", saveEventList)
                intent.putExtra("activityList", saveActivityList)
                intent.putExtra("foodList", saveFoodList)
                startActivity(intent)
            }

            llNotComplete.setOnSingleClickListener {
                val intent = Intent(requireActivity(), MyTourListActivity::class.java)
                intent.putExtra("tourList", saveTourList)
                startActivity(intent)
            }

            llCertification.setOnSingleClickListener {
                val intent = Intent(requireActivity(), MyCertificationActivity::class.java)
                intent.putExtra("tourList", saveTourList)
                intent.putExtra("cultureList", saveCultureList)
                intent.putExtra("eventList", saveEventList)
                intent.putExtra("activityList", saveActivityList)
                intent.putExtra("foodList", saveFoodList)
                startActivity(intent)
            }

            tvNickName.setOnSingleClickListener {
                if (tvNickName.text.toString() == "로그인이 필요해요") {
                    val intent = Intent(requireActivity(), LoginActivity::class.java)
                    startActivity(intent)
                }
            }


            ivSetting.setOnSingleClickListener {
                val intent = Intent(requireActivity(), SettingActivity::class.java)
                startActivity(intent)
            }

            tvLogout.setOnSingleClickListener {
                userViewModel.logout { success ->
                    if (success) {
                        tvLogout.visibility = View.INVISIBLE
                        Toast.makeText(requireActivity(), "로그아웃 되었어요.", Toast.LENGTH_SHORT).show()
                        saveTourList.clear()
                        saveCultureList.clear()
                        saveEventList.clear()
                        saveActivityList.clear()
                        saveFoodList.clear()

                        // UI 초기화
                        binding.apply {
                            // 상단 프로필
                            tvNickName.text = "로그인이 필요해요"

                            // 상단 카운트 초기화
                            tvCompleteCount.text = "0"
                            tvNotCompleteCount.text = "0"
                            tvCertificationCount.text = "0"

                            // 관광 레벨 초기화
                            tvTourLevelName.text = "여행 병아리"
                            tvTourCount.text = "0"
                            tvTourTotalCount.text = "/ 10"
                            tourProgress.progress = 0

                            // 문화 레벨 초기화
                            tvCultureLevelName.text = "관람객"
                            tvCultureCount.text = "0"
                            tvCultureTotalCount.text = "/ 10"
                            cultureProgress.progress = 0

                            // 축제 레벨 초기화
                            tvEventLevelName.text = "가볍게 즐기는 자"
                            tvEventCount.text = "0"
                            tvEventTotalCount.text = "/ 10"
                            eventProgress.progress = 0

                            // 액티비티 레벨 초기화
                            tvActivityLevelName.text = "뉴비"
                            tvActivityCount.text = "0"
                            tvActivityTotalCount.text = "/ 10"
                            activityProgress.progress = 0

                            // 음식 레벨 초기화
                            tvFoodLevelName.text = "맛집 찾아 헤메는 자"
                            tvFoodCount.text = "0"
                            tvFoodTotalCount.text = "/ 10"
                            foodProgress.progress = 0
                        }
                    } else {
                        Toast.makeText(requireActivity(), "로그아웃이 실패했어요", Toast.LENGTH_SHORT).show()
                        tvLogout.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun calculateLevel(visitedCount: Int, category: CategoryLevel): LevelInfo {
        return when {
            visitedCount < 10 -> LevelInfo(
                level = category.beginnerLevel,
                currentCount = visitedCount,
                targetCount = 10,
                progress = (visitedCount * 100) / 10
            )

            visitedCount < 30 -> LevelInfo(
                level = category.intermediateLevel,
                currentCount = visitedCount,
                targetCount = 30,
                progress = (visitedCount * 100) / 30
            )

            else -> LevelInfo(
                level = category.advancedLevel,
                currentCount = visitedCount,
                targetCount = 50,
                progress = (visitedCount * 100) / 50
            )
        }
    }


    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMyBinding {
        return FragmentMyBinding.inflate(layoutInflater)
    }
}