package com.example.chatapp.layouts.landscapeLayout.starterScreens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavController
import com.example.chatapp.Dtos.user.User
import com.example.chatapp.layouts.mainLayout.starterScreens.logInScreen.LogInUiState
import com.example.chatapp.layouts.sharedComponents.validation.ValidatedTextField
import com.example.chatapp.layouts.sharedComponents.validation.ValidationResult
import com.example.chatapp.navigation.ScreenRoutes

@Composable
fun LandscapeLoginScreen(
    navController: NavController,
    logInUiState: LogInUiState,
    validateEmail: (String) -> Unit,
    validatePassword: (String) -> Unit,
    logIn: (User) -> Unit,
) {
    val focusManger = LocalFocusManager.current

    var email by remember { mutableStateOf(logInUiState.user.email) }
    var password by remember { mutableStateOf(logInUiState.user.password) }

    val isSuccess by remember(email,password) {
        mutableStateOf(
            logInUiState.emailValidationResult is ValidationResult.Success &&
                    logInUiState.passwordValidationResult is ValidationResult.Success
        )
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Column(
            modifier = Modifier
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Spacer(modifier = Modifier.weight(3f))

            Text(
                text = "Log In!",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.weight(3f))

            Button(
                modifier = Modifier
                    .weight(1.8f)
                    .fillMaxWidth(0.8f),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White
                ),
                onClick = {
                    if(isSuccess) {
                        val user = User(
                            email = email,
                            password = password,
                        )

                        logIn(user)
                    }
                },
                enabled = isSuccess,
            ) {
                Text(
                    text = "Log In",
                    style = MaterialTheme.typography.labelLarge
                )
            }

            Spacer(modifier = Modifier.weight(0.2f))

            TextButton(
                onClick = {
                    navController.navigate(ScreenRoutes.StarterScreens.SignUpRoute) {
                        popUpTo(ScreenRoutes.StarterScreens.WelcomeRoute)
                    }
                },
                modifier = Modifier.alpha(0.7f)
            ) {
                Text(
                    text = "Already Have Account? Log in!",
                    style = MaterialTheme.typography.labelLarge
                )
            }

            Spacer(modifier = Modifier.weight(1f))

        }

        Column(
            modifier = Modifier
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1.1f))

            ValidatedTextField(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .weight(1f),
                text = email,
                onTextChange = {
                    email = it
                    validateEmail(it)
                },
                validationResult = logInUiState.emailValidationResult,
                placeHolderText = "Enter your email",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                onDone = { focusManger.moveFocus(FocusDirection.Next) },
            )

            ValidatedTextField(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .weight(1f),
                text = password,
                onTextChange = {
                    password = it
                    validatePassword(password)
                },
                validationResult = logInUiState.passwordValidationResult,
                placeHolderText = "Enter your password",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                onDone = { focusManger.moveFocus(FocusDirection.Next) },
            )

            Spacer(modifier = Modifier.weight(1.1f))
        }
    }
}