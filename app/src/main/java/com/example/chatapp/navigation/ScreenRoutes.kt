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
        data object OneToOneChatRoute {
            const val MAIN_ROUTE_PART = "OneToOneChat"
            const val ROUTE = "$MAIN_ROUTE_PART/{chatId}/{userId}"
        }

        @Serializable
        data object SettingsGraphRoute {
            @Serializable
            data object MainSettingsRoute {
                @Serializable
                data object PrivacyRoute
                @Serializable
                data object NotificationsRoute
                @Serializable
                data object MessagesRoute
                @Serializable
                data object MediaRoute
                @Serializable
                data object AppearanceRoute
            }

            @Serializable
            data object EditProfileRoute
        }
    }
}