package com.example.chatapp.layouts.mainLayout.loggedScreens.screens.chat.sharedComponents.SelectedMessageActionsMenu

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Reply
import androidx.compose.material.icons.filled.Share
import androidx.compose.ui.graphics.vector.ImageVector

enum class SelectedMessageButtonsActions(val title: String, val icon: ImageVector) {
    DELETE("Delete",Icons.Filled.Delete),
    EDIT("Edit",Icons.Filled.Edit),
    REPLY("Reply",Icons.Filled.Reply),
    SHARE("Share",Icons.Filled.Share);

    companion object {
        val actionsOverMyMessages = listOf(
            DELETE,
            EDIT,
        )
        val actionsOverOtherMessages = listOf(
            DELETE,
        )
        val actionsOverImageMessage = listOf(
            DELETE
        )
    }
}