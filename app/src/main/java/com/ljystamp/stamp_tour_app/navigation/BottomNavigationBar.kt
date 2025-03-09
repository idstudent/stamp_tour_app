package com.ljystamp.stamp_tour_app.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.ljystamp.core_ui.theme.AppColors

@Composable
fun BottomNavigationBar(
    navController: NavController
) {
    val items = listOf(
        NaviItem.Home,
        NaviItem.Search,
        NaviItem.My
    )

    NavigationBar(
        containerColor = AppColors.Black,
        contentColor = AppColors.ColorFF8C00
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                  navController.navigate(item.route) {
                      launchSingleTop = true
                  }
                },
                icon = {
                    item.iconRes?.let {
                        painterResource(id = it)
                    }?.let {
                        Icon(
                            painter = it, contentDescription = item.title)
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = AppColors.ColorFF8C00,
                    selectedTextColor = AppColors.ColorFF8C00,
                    unselectedIconColor = AppColors.White,
                    unselectedTextColor = AppColors.White,
                    indicatorColor = AppColors.Black, // 설정 안하면 select된 아이템에 둥그런 박스가 보임
                ),
                label = { Text(text = item.title ?: "") },
                alwaysShowLabel = true
            )
        }
    }
}