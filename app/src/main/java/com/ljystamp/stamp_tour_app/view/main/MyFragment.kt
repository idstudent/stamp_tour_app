package com.ljystamp.stamp_tour_app.view.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ljystamp.stamp_tour_app.databinding.FragmentMyBinding
import com.ljystamp.stamp_tour_app.view.BaseFragment

class MyFragment: BaseFragment<FragmentMyBinding>() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentMyBinding {
        return FragmentMyBinding.inflate(layoutInflater)
    }
}