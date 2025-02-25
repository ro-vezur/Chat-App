package com.example.chatapp.layouts.mainLayout.starterScreens.signUpScreen

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
import com.example.chatapp.Dtos.User
import com.example.chatapp.layouts.landscapeLayout.starterScreens.LandscapeSignUpScreen
import com.example.chatapp.layouts.verticalLayout.starterScreens.VerticalSignUpScreen
import com.example.chatapp.layouts.sharedComponents.validation.ValidationResult
import com.example.chatapp.ui.theme.ChatAppTheme

@Composable
fun SignUpScreen(
    navController: NavController,
    signUpUiState: SignUpUiState,
    validateName: (name: String) -> Unit,
    validateEmail: (email: String) -> Unit,
    validatePassword: (password: String) -> Unit,
    validateConfirmPassword: (password: String,confirmPassword: String) -> Unit,
    signUp: (User) -> Unit,
) {
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    if(isPortrait) {
        VerticalSignUpScreen(
            navController = navController,
            signUpUiState = signUpUiState,
            validateName = validateName,
            validateEmail = validateEmail,
            validatePassword = validatePassword,
            validateConfirmPassword = validateConfirmPassword,
            signUp = signUp
        )
    } else {
        LandscapeSignUpScreen(
            navController = navController,
            signUpUiState = signUpUiState,
            validateName = validateName,
            validateEmail = validateEmail,
            validatePassword = validatePassword,
            validateConfirmPassword = validateConfirmPassword,
            signUp = signUp
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
            SignUpScreen(
                navController = rememberNavController(),
                signUpUiState = SignUpUiState(
                    nameValidationResult = ValidationResult.Success,
                    emailValidationResult = ValidationResult.Success,
                    passwordValidationResult = ValidationResult.Success,
                    passwordConfirmValidationResult = ValidationResult.Error("")
                ),
                validateName = {},
                validateEmail = {},
                validatePassword = {},
                validateConfirmPassword = {a,b ->},
                signUp = {}
            )
        }
    }
}