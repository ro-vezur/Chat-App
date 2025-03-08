package com.example.chatapp.helpers.navigation

import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController

fun NavController.navigateBack() {
    if(currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
        popBackStack()
    }
}