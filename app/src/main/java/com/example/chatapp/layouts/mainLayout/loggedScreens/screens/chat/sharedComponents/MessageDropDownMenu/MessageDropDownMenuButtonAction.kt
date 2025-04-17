package com.example.chatapp.layouts.mainLayout.loggedScreens.screens.chat.sharedComponents.MessageDropDownMenu

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.ui.graphics.vector.ImageVector

enum class MessageDropDownMenuButtonAction(val title: String,val icon: ImageVector) {
    DELETE("Delete",Icons.Filled.Delete),
    EDIT("Edit",Icons.Filled.Edit);

    companion object {
        val actionsOverMyMessages = listOf(
            DELETE,
            EDIT
        )
        val actionsOverOtherMessages = listOf(
            DELETE
        )
    }
}