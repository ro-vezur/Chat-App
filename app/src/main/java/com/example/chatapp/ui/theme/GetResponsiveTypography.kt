package com.example.chatapp.ui.theme

import android.content.res.Configuration
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import com.example.chatapp.differentScreensSupport.WindowInfo.WindowType
import com.example.chatapp.differentScreensSupport.rememberWindowsSizeClass

@Composable
fun getResponsiveTypography(): Typography {
    val configuration = LocalConfiguration.current
    val rememberWindowSize = rememberWindowsSizeClass()

    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    val screenWidthInfo = rememberWindowSize.screenWidthInfo

    return when {
        screenWidthInfo is WindowType.Compact -> { SmallScreenTypography }
        screenWidthInfo is WindowType.Medium && !isPortrait -> { SmallScreenTypography}
        screenWidthInfo is WindowType.Medium -> { MediumScreenTypography }
        screenWidthInfo is WindowType.Expanded -> { LargeScreenTypography }
        else -> { LargeScreenTypography }
    }
}