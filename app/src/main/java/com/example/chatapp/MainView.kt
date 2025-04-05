package com.example.chatapp

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.chatapp.Dtos.user.User
import com.example.chatapp.layouts.sharedComponents.bottomNavigationBar.BottomNavigationBar
import com.example.chatapp.navigation.MainNavGraph

@Composable
fun MainView(isLogged: Boolean,user: User,unseenMessagesCount: Int) {

    val navHostController = rememberNavController()
    var showBottomBar by rememberSaveable { mutableStateOf(true) }

    CompositionLocalProvider(LocalUser provides user) {
        Scaffold(
            modifier = Modifier
                .systemBarsPadding(),
            bottomBar = {
                if (isLogged && showBottomBar) {
                    BottomNavigationBar(
                        navController = navHostController,
                        unseenMessagesCount = unseenMessagesCount,
                    )
                }
            }
        ) { innerPadding ->
            MainNavGraph(
                modifier = Modifier.padding(innerPadding),
                navController = navHostController,
                isLogged = isLogged,
                updateBottomBarState = { state -> showBottomBar = state}
            )
        }
    }

}