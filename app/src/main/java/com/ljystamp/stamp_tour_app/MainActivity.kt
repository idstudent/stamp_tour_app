package com.ljystamp.stamp_tour_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.ljystamp.core_ui.theme.StampTourAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setWindowAnimations(android.R.style.Animation_Activity)

        setContent {
            StampTourAppTheme {
                MainScreen(navController = rememberNavController())
            }
        }
    }
}