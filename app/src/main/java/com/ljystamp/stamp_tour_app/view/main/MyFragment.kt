package com.ljystamp.stamp_tour_app.view.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ljystamp.stamp_tour_app.R
import com.ljystamp.stamp_tour_app.api.model.SavedLocation
import com.ljystamp.stamp_tour_app.databinding.FragmentMyBinding
import com.ljystamp.stamp_tour_app.util.setOnSingleClickListener
import com.ljystamp.stamp_tour_app.view.BaseFragment
import com.ljystamp.stamp_tour_app.view.my.MyCompleteListActivity
import com.ljystamp.stamp_tour_app.view.my.MyPlanListActivity
import com.ljystamp.stamp_tour_app.view.user.SignUpActivity
import com.ljystamp.stamp_tour_app.view.user.model.CategoryLevel
import com.ljystamp.stamp_tour_app.view.user.model.LevelInfo
import com.ljystamp.stamp_tour_app.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyFragment: BaseFragment<FragmentMyBinding>() {
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
//        binding.btnLogout.setOnSingleClickListener {
//            userViewModel.logout { success ->
//                if(success) {
//                    Toast.makeText(requireActivity(), "로그아웃 성공", Toast.LENGTH_SHORT).show()
//                }else {
//                    Toast.makeText(requireActivity(), "로그아웃 실패", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
    }

    private fun initView() {
        userViewModel.getUserProfileAndSavedLocations()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                // 프로필 수집
                launch {
                    userViewModel.userProfile.collectLatest { profile ->
                        profile?.let {
                            binding.tvNickName.text = it["nickname"] as? String
                        }
                    }
                }

                launch {
                    userViewModel.allList.collectLatest {
                        Log.e("ljy", "alllist ${it.size}")
                        binding.tvCompleteCount.text = it.filter { it.isVisited }.size.toString()
                        binding.tvNotCompleteCount.text = it.filter { !it.isVisited }.size.toString()
                    }
                }
                // 관광 리스트 수집
                launch {
                    userViewModel.tourPlaceList.collectLatest { list ->
                        val visitedCount = list.count { it.isVisited }
                        val levelInfo = calculateLevel(visitedCount, CategoryLevel.TOUR)

                        saveTourList.addAll(list)

                        binding.apply {
                            tvTourLevelName.text = levelInfo.level
                            tvNowTourCount.text = levelInfo.currentCount.toString()
                            tvTourTotalCount.text = "/ ${levelInfo.targetCount}"
                            tourProgress.progress = levelInfo.progress
                            tvTourLevelSubTitle.text = CategoryLevel.TOUR.subTitleFormat.format(
                                if (levelInfo.level == CategoryLevel.TOUR.beginnerLevel) 5
                                else if(levelInfo.level == CategoryLevel.TOUR.intermediateLevel) 30
                                else 100
                            )
                        }
                    }
                }

                // 문화 리스트 수집
                launch {
                    userViewModel.cultureList.collectLatest { list ->
                        saveCultureList.addAll(list.filter { it.isVisited })
                        Log.e("ljy", "문화 $list")
                    }
                }

                // 축제 리스트 수집
                launch {
                    userViewModel.eventList.collectLatest { list ->
                        saveEventList.addAll(list.filter { it.isVisited })
                        Log.e("ljy", "축제 $list")
                    }
                }

                // 액티비티 리스트 수집
                launch {
                    userViewModel.activityList.collectLatest { list ->
                        saveActivityList.addAll(list.filter { it.isVisited })
                        Log.e("ljy", "액티비티 $list")
                    }
                }

                // 음식 리스트 수집
                launch {
                    userViewModel.foodList.collectLatest { list ->
                        saveFoodList.addAll(list.filter { it.isVisited })
                        Log.e("ljy", "음식 $list")
                    }
                }
            }
        }
    }

    private fun initListener() {
        binding.run {
            ivSetting.setOnSingleClickListener {
                showDeleteAccountDialog()
            }
            llComplete.setOnSingleClickListener {
                val intent = Intent(requireActivity(), MyCompleteListActivity::class.java)
                intent.putExtra("list", saveTourList)
                startActivity(intent)
            }
            llNotComplete.setOnSingleClickListener {
                val intent = Intent(requireActivity(), MyPlanListActivity::class.java)
                intent.putExtra("list", saveTourList)
                startActivity(intent)
            }
            llNotComplete.setOnSingleClickListener {

            }
        }

    }
    private fun showDeleteAccountDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_delete_account, null)
        val etPassword = dialogView.findViewById<EditText>(R.id.etPassword)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("회원 탈퇴")
            .setView(dialogView)
            .setMessage("정말 탈퇴하시겠습니까? 모든 데이터가 삭제되며 복구할 수 없습니다.")
            .setPositiveButton("탈퇴") { dialog, _ ->
                val password = etPassword.text.toString()
                if (password.isEmpty()) {
                    Toast.makeText(requireContext(), "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                userViewModel.deleteAccount(password) { success, message ->
                    if (success) {
                        Toast.makeText(requireContext(), "회원 탈퇴가 완료되었습니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("취소", null)
            .show()
    }

    private fun calculateLevel(visitedCount: Int, category: CategoryLevel): LevelInfo {
        return when {
            visitedCount < 5 -> LevelInfo(
                level = category.beginnerLevel,
                currentCount = visitedCount,
                targetCount = 5,
                progress = (visitedCount * 100) / 5
            )
            visitedCount < 30 -> LevelInfo(  // < 를 <= 로 변경
                level = category.intermediateLevel,
                currentCount = visitedCount,
                targetCount = 30,
                progress = (visitedCount * 100) / 30
            )
            else -> LevelInfo(
                level = category.advancedLevel,
                currentCount = visitedCount,
                targetCount = 50,
                progress = (visitedCount * 100) / 50  // 실제 진행률 계산
            )
        }
    }


    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentMyBinding {
        return FragmentMyBinding.inflate(layoutInflater)
    }
}