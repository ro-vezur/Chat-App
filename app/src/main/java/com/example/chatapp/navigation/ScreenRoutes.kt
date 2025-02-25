package com.example.chatapp.navigation

import kotlinx.serialization.Serializable

object ScreenRoutes {
    @Serializable
    object StarterScreens {

        @Serializable
        object WelcomeRoute

        @Serializable
        object LogInRoute

        @Serializable
        object SignUpRoute
    }

    @Serializable
    object LoggedScreens {

        @Serializable
        data object ChatsRoute

        @Serializable
        data object FriendRequestsRoute

        @Serializable
        data object FriendsRoute

        @Serializable
        data object ChatRoute

        @Serializable
        data object SettingsRoute
    }
}