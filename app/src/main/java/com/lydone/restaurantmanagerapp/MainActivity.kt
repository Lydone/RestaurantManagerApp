package com.lydone.restaurantmanagerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.lydone.restaurantmanagerapp.ui.RestaurantCookApp
import com.lydone.restaurantmanagerapp.ui.theme.RestaurantManagerAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RestaurantManagerAppTheme {
                RestaurantCookApp()
            }
        }
    }
}