package com.example.chatapp.layouts.mainLayout.loggedScreens.screens.settings.components

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.example.chatapp.Dtos.user.userSettings.SettingsSelectionValueVariants
import com.example.chatapp.differentScreensSupport.sdp

@Composable
fun SettingSelectionActionButtonCard(
    modifier: Modifier = Modifier,
    settingName: String,
    description: String,
    selectedVariant: SettingsSelectionValueVariants,
    selectionVariants: List<SettingsSelectionValueVariants>,
    onSelect: (newVariant: SettingsSelectionValueVariants) -> Unit,
) {
    var isExpanded by remember {
        mutableStateOf(false)
    }

    Row(
        modifier = modifier
            .clickable {
                isExpanded = !isExpanded
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column (
            modifier = Modifier
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.sdp)
        ) {
            Text(
                text = settingName,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
            )

            Text(
                text = description
            )
        }

        Box(

        ) {
            Text(
                modifier = Modifier
                    .padding(horizontal = 6.sdp)
                    .width(85.sdp)
                    .border(
                        border = BorderStroke(1.sdp, MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(8.sdp)
                    )
                    .padding(8.sdp),
                textAlign = TextAlign.Center,
                text = selectedVariant.text,
                style = MaterialTheme.typography.bodyMedium,
            )

            SelectionVariantsDropDownMenu(
                isExpanded = isExpanded,
                variants = selectionVariants,
                onDismiss = { isExpanded = false },
                onSelect = { newSelectionVariant ->
                    isExpanded = false
                    onSelect(newSelectionVariant)
                },
            )
        }
    }
}

@Composable
private fun SelectionVariantsDropDownMenu(
    isExpanded: Boolean,
    variants: List<SettingsSelectionValueVariants>,
    onDismiss: () -> Unit,
    onSelect: (newVariant: SettingsSelectionValueVariants) -> Unit,
) {
    DropdownMenu(
        expanded = isExpanded,
        onDismissRequest = onDismiss
    ) {
        variants.forEach { variant ->
            DropdownMenuItem(
                text = {
                    Text(
                        text = variant.text
                    )
                }, onClick = {
                    Log.d("variants",variants.toString())
                    onSelect(variant)
                }
            )
        }
    }
}
