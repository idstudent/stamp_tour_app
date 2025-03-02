package com.ljystamp.core_ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.KeyEventDispatcher.Component
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<BINDING : ViewBinding> : ComponentActivity() {
    protected lateinit var binding: BINDING

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewBinding()
        setContentView(binding.root)
    }


    abstract fun getViewBinding(): BINDING

}