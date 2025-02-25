package com.example.chatapp.layouts.sharedComponents.bottomNavigationBar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.chatapp.LocalUser
import com.example.chatapp.differentScreensSupport.sdp
import com.example.chatapp.ui.theme.ChatAppTheme

@Composable
fun BottomNavigationBar(
    navController: NavController,
) {
    val user = LocalUser.current

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    var selectedItem by remember { mutableStateOf(BottomNavigationBarItems.CHATS) }

    LaunchedEffect(key1 = navBackStackEntry) {
        val currentRoute = navBackStackEntry?.destination?.route.toString()

        BottomNavigationBarItems.entries.forEach { item ->
            if (currentRoute.contains(item.route.toString())) {
                selectedItem = item
            }
        }
    }

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        BottomNavigationBarItems.entries.forEach { item ->

            val isSelected = item == selectedItem

            NavigationBarItem(
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedTextColor = MaterialTheme.colorScheme.onPrimary,
                    unselectedIconColor = MaterialTheme.colorScheme.onPrimary,
                    indicatorColor = Color.Transparent
                ),
                selected = isSelected,
                onClick = { navController.navigate(item.route) },
                alwaysShowLabel = false,
                icon = {
                    BadgedBox(
                        badge = {
                            var messageChatsCounter by remember { mutableStateOf(0) }

                            LaunchedEffect(user.requests) {
                                messageChatsCounter =  when(item) {
                                    BottomNavigationBarItems.CHATS -> 0
                                    BottomNavigationBarItems.FRIENDS -> 0
                                    BottomNavigationBarItems.FRIENDS_REQUESTS -> user.requests.size
                                    BottomNavigationBarItems.SETTINGS -> 0
                                }
                            }

                            if(messageChatsCounter != 0) {
                                Box(
                                    modifier = Modifier
                                        .size(15.sdp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.error),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Text(
                                        text = messageChatsCounter.toString(),
                                        color = Color.White,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }

                        }
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(32.sdp),
                            imageVector = item.icon,
                            contentDescription = "bottom nav item icon",
                        )
                    }
                },
                label = {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            )

        }
    }
}

@Preview
@Composable
private fun previewBottomNavBar() {
    ChatAppTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                BottomNavigationBar(
                    navController = rememberNavController()
                )
            }
        }
    }

}