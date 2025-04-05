package com.example.chatapp.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.chatapp.layouts.mainLayout.loggedScreens.loggedNavGraph
import com.example.chatapp.layouts.mainLayout.starterScreens.starterNavGraph

@Composable
fun MainNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    isLogged: Boolean,
    updateBottomBarState: (Boolean) -> Unit,
) {
   // Log.d("isLogged",isLogged.toString())

    NavHost(
        modifier = modifier
            .fillMaxSize(),
        navController = navController,
        startDestination = if(isLogged) ScreenRoutes.LoggedScreens else ScreenRoutes.StarterScreens,
    ) {
        starterNavGraph(navController)

        loggedNavGraph(
            navController = navController,
            updateBottomBarState = updateBottomBarState,
        )
    }
}