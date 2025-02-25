package com.example.chatapp.layouts.mainLayout.starterScreens

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.chatapp.differentScreensSupport.checkIsPortrait
import com.example.chatapp.layouts.landscapeLayout.starterScreens.LandscapeWelcomeScreen
import com.example.chatapp.layouts.verticalLayout.starterScreens.VerticalWelcomeScreen

@Composable
fun WelcomeScreen(
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit,
) {

    if(checkIsPortrait()) {
        VerticalWelcomeScreen(
            onLoginClick = onLoginClick,
            onSignUpClick = onSignUpClick,
        )
    } else {
        LandscapeWelcomeScreen(
            onLoginClick = onLoginClick,
            onSignUpClick = onSignUpClick,
        )
    }
}

@Composable
@Preview()
private fun WelcomeScreenPreview() {
    WelcomeScreen(
        {},
        {}
    )
}