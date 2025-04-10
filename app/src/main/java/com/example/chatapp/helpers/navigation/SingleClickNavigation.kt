package com.example.chatapp.helpers.navigation

import android.util.Log
import androidx.navigation.NavController

fun NavController.singleClickNavigate(route: Any) {
    Log.d("current destionation",currentDestination?.route.toString())
    Log.d("route",route.toString())
    if(currentDestination?.route?.contains(route.toString()) == false) {
        this.navigate(route)
    }
}

fun NavController.singleClickNavigate(route: String) {
    Log.d("current destionation",currentDestination?.route.toString())
    Log.d("route",route)
    if(currentDestination?.route?.contains(route) == false) {
        this.navigate(route) {
            launchSingleTop = true
        }
    }
}