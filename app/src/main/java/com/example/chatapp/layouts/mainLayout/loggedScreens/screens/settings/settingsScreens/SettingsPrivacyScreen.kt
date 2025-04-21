package com.example.chatapp.layouts.mainLayout.loggedScreens.screens.settings.settingsScreens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import com.example.chatapp.LocalUser
import com.example.chatapp.differentScreensSupport.sdp
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.settings.SettingActionType
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.settings.SettingsScreenActionButtons
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.settings.components.SettingSelectionActionButtonCard
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.settings.components.SettingsTopBar
import com.example.chatapp.layouts.mainLayout.loggedScreens.screens.settings.settingsViewModel.SettingsViewModelEvent
import com.example.chatapp.ui.theme.ChatAppTheme

@Composable
fun SettingsPrivacyScreen(
    turnBack: () -> Unit,
    dispatchEvent: (SettingsViewModelEvent) -> Unit,
    ) {
    val mainUser = LocalUser.current
    val privacySettings by remember(mainUser.settings.privacySettings) {
        mutableStateOf(mainUser.settings.privacySettings)
    }

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            SettingsTopBar(
                header = "Privacy",
                turnBack = turnBack,
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(
                    top = innerPadding.calculateTopPadding() + 12.sdp,
                )
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(10.sdp)
        ) {
            SettingsScreenActionButtons.SettingsPrivacyActionButtons.entries.forEach { setting ->
                if(setting.settingType == SettingActionType.SELECTION && setting.selectionVariants != null) {
                    val selectedVariant = when(setting) {
                        SettingsScreenActionButtons.SettingsPrivacyActionButtons.WHO_CAN_SEE_MY_IMAGE -> privacySettings.whoCanSeeMyImage
                        SettingsScreenActionButtons.SettingsPrivacyActionButtons.WHO_CAN_SEE_MY_NAME -> privacySettings.whoCanSeeMyName
                        SettingsScreenActionButtons.SettingsPrivacyActionButtons.WHO_CAN_MESSAGE_ME -> privacySettings.whoCanMessageMe
                        SettingsScreenActionButtons.SettingsPrivacyActionButtons.WHO_CAN_SEE_MY_ACTIVITY -> privacySettings.whoCanSeeMyActivity
                        SettingsScreenActionButtons.SettingsPrivacyActionButtons.WHO_CAN_ADD_ME_TO_GROUPS -> privacySettings.whoCanAddMeToGroups
                        SettingsScreenActionButtons.SettingsPrivacyActionButtons.WHO_CAN_SEND_FRIENDS_REQUESTS -> privacySettings.whoCanSendFriendsRequests
                    }

                    SettingSelectionActionButtonCard(
                        modifier = Modifier
                            .padding(horizontal = 6.sdp)
                            .fillMaxWidth()
                            .shadow(2.sdp, RoundedCornerShape(10.sdp))
                            .clip(RoundedCornerShape(10.sdp))
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(horizontal = 12.sdp, vertical = 8.sdp),
                        settingName = setting.settingName,
                        description = setting.description,
                        selectionVariants = setting.selectionVariants,
                        selectedVariant = selectedVariant,
                        onSelect = { newSettingValue ->
                            when(setting) {
                                SettingsScreenActionButtons.SettingsPrivacyActionButtons.WHO_CAN_SEE_MY_IMAGE -> {
                                    val newPrivacySettings = privacySettings.copy(whoCanSeeMyImage = newSettingValue)
                                    dispatchEvent(SettingsViewModelEvent.UpdateUserSettings(mainUser.id,mainUser.settings.copy(privacySettings = newPrivacySettings)))
                                }
                                SettingsScreenActionButtons.SettingsPrivacyActionButtons.WHO_CAN_SEE_MY_NAME -> {
                                    val newPrivacySettings = privacySettings.copy(whoCanSeeMyName = newSettingValue)
                                    dispatchEvent(SettingsViewModelEvent.UpdateUserSettings(mainUser.id,mainUser.settings.copy(privacySettings = newPrivacySettings)))
                                }
                                SettingsScreenActionButtons.SettingsPrivacyActionButtons.WHO_CAN_MESSAGE_ME -> {
                                    val newPrivacySettings = privacySettings.copy(whoCanMessageMe = newSettingValue)
                                    dispatchEvent(SettingsViewModelEvent.UpdateUserSettings(mainUser.id,mainUser.settings.copy(privacySettings = newPrivacySettings)))
                                }
                                SettingsScreenActionButtons.SettingsPrivacyActionButtons.WHO_CAN_SEE_MY_ACTIVITY -> {
                                    val newPrivacySettings = privacySettings.copy(whoCanSeeMyActivity = newSettingValue)
                                    dispatchEvent(SettingsViewModelEvent.UpdateUserSettings(mainUser.id,mainUser.settings.copy(privacySettings = newPrivacySettings)))
                                }
                                SettingsScreenActionButtons.SettingsPrivacyActionButtons.WHO_CAN_ADD_ME_TO_GROUPS -> {
                                    val newPrivacySettings = privacySettings.copy(whoCanAddMeToGroups = newSettingValue)
                                    dispatchEvent(SettingsViewModelEvent.UpdateUserSettings(mainUser.id,mainUser.settings.copy(privacySettings = newPrivacySettings)))
                                }
                                SettingsScreenActionButtons.SettingsPrivacyActionButtons.WHO_CAN_SEND_FRIENDS_REQUESTS -> {
                                    val newPrivacySettings = privacySettings.copy(whoCanSendFriendsRequests = newSettingValue)
                                    dispatchEvent(SettingsViewModelEvent.UpdateUserSettings(mainUser.id,mainUser.settings.copy(privacySettings = newPrivacySettings)))
                                }
                            }
                        }
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.sdp))
        }
    }
}

@Preview
@Composable
private fun SettingsPrivacyPrev () {
    ChatAppTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            SettingsPrivacyScreen(
                turnBack = {},
                dispatchEvent = {}
            )
        }
    }
}