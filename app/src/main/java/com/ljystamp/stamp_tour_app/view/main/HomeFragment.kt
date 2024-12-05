package com.ljystamp.stamp_tour_app.view.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ljystamp.stamp_tour_app.databinding.FragmentHomeBinding
import com.ljystamp.stamp_tour_app.view.BaseFragment

class HomeFragment: BaseFragment<FragmentHomeBinding>() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(layoutInflater)
    }
}