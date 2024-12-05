package com.ljystamp.stamp_tour_app.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<BINDING : ViewBinding> : Fragment() {
    private var _binding: BINDING? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflateBinding(inflater, container)
        return binding.root
    }




    abstract fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): BINDING

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}