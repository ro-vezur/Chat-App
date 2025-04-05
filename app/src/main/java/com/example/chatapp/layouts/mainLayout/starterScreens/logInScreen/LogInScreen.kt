package com.example.chatapp.layouts.mainLayout.starterScreens.logInScreen

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.chatapp.Dtos.user.User
import com.example.chatapp.layouts.landscapeLayout.starterScreens.LandscapeLoginScreen
import com.example.chatapp.layouts.sharedComponents.validation.ValidationResult
import com.example.chatapp.layouts.verticalLayout.starterScreens.VerticalLoginScreen
import com.example.chatapp.ui.theme.ChatAppTheme

@Composable
fun LogInScreen(
    navController: NavController,
    logInUiState: LogInUiState,
    validateEmail: (String) -> Unit,
    validatePassword: (String) -> Unit,
    logIn: (User) -> Unit,
) {
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    if(isPortrait) {
        VerticalLoginScreen(
            navController = navController,
            logInUiState = logInUiState,
            validateEmail = validateEmail,
            validatePassword = validatePassword,
            logIn = logIn,
        )
    } else {
        LandscapeLoginScreen(
            navController = navController,
            logInUiState = logInUiState,
            validateEmail = validateEmail,
            validatePassword = validatePassword,
            logIn = logIn,
        )
    }
}

@Preview(device = "spec:width=400dp,height=700dp,orientation=landscape")
@Preview(device = "spec:width=700dp,height=1000dp,orientation=portrait")
@Composable
private fun SignUpPreview() {
    ChatAppTheme(
        darkTheme = true
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            LogInScreen(
                navController = rememberNavController(),
                logInUiState = LogInUiState(
                    emailValidationResult = ValidationResult.Success(),
                    passwordValidationResult = ValidationResult.Success(),
                ),
                validateEmail = {},
                validatePassword = {},
                logIn = {}
            )
        }
    }
}