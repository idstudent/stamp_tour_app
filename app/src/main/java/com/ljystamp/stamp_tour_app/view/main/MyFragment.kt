package com.ljystamp.stamp_tour_app.view.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.ljystamp.stamp_tour_app.databinding.FragmentMyBinding
import com.ljystamp.stamp_tour_app.util.setOnSingleClickListener
import com.ljystamp.stamp_tour_app.view.BaseFragment
import com.ljystamp.stamp_tour_app.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyFragment: BaseFragment<FragmentMyBinding>() {
    private val userViewModel: UserViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogout.setOnSingleClickListener {
            userViewModel.logout { success ->
                if(success) {
                    Toast.makeText(requireActivity(), "로그아웃 성공", Toast.LENGTH_SHORT).show()
                }else {
                    Toast.makeText(requireActivity(), "로그아웃 실패", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentMyBinding {
        return FragmentMyBinding.inflate(layoutInflater)
    }
}