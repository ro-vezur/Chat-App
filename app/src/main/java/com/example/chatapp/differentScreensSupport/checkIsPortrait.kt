package com.example.chatapp.differentScreensSupport

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration

@Composable
fun checkIsPortrait(): Boolean {
    return LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT
}