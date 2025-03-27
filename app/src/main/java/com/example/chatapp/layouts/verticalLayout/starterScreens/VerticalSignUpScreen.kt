package com.example.chatapp.layouts.verticalLayout.starterScreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.chatapp.Dtos.user.User
import com.example.chatapp.layouts.mainLayout.starterScreens.signUpScreen.SignUpUiState
import com.example.chatapp.layouts.sharedComponents.validation.ValidatedTextField
import com.example.chatapp.layouts.sharedComponents.validation.ValidationResult
import com.example.chatapp.navigation.ScreenRoutes

@Composable
fun VerticalSignUpScreen(
    navController: NavController,
    signUpUiState: SignUpUiState,
    validateName: (name: String) -> Unit,
    validateEmail: (email: String) -> Unit,
    validatePassword: (password: String) -> Unit,
    validateConfirmPassword: (password: String,confirmPassword: String) -> Unit,
    signUp: (User) -> Unit,
) {
    val focusManger = LocalFocusManager.current

    var name by remember { mutableStateOf(signUpUiState.user.name) }
    var email by remember { mutableStateOf(signUpUiState.user.email) }
    var password by remember { mutableStateOf(signUpUiState.user.password) }
    var confirmPassword by remember { mutableStateOf(signUpUiState.passwordConfirm) }

    val isSuccess by remember(name,email,password,confirmPassword) {
        mutableStateOf(
            signUpUiState.nameValidationResult is ValidationResult.Success &&
                    signUpUiState.emailValidationResult is ValidationResult.Success &&
                    signUpUiState.passwordValidationResult is ValidationResult.Success &&
                    signUpUiState.passwordConfirmValidationResult is ValidationResult.Success,
        )
    }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Spacer(modifier = Modifier.weight(3f))

        Text(
            text = "Sign Up!",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        
        Spacer(modifier = Modifier.weight(3f))

        Column(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .weight(18f),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ValidatedTextField(
                modifier = Modifier
                    .weight(1f),
                text = name,
                onTextChange = {
                    name = it
                    validateName(it)

                },
                validationResult = signUpUiState.nameValidationResult,
                placeHolderText = "Enter your name",
                onDone = { focusManger.moveFocus(FocusDirection.Next) }
            )

            ValidatedTextField(
                modifier = Modifier
                    .weight(1f),
                text = email,
                onTextChange = {
                    email = it
                    validateEmail(it)
                },
                validationResult = signUpUiState.emailValidationResult,
                placeHolderText = "Enter your email",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                onDone = { focusManger.moveFocus(FocusDirection.Next) },
            )

            ValidatedTextField(
                modifier = Modifier
                    .weight(1f),
                text = password,
                onTextChange = {
                    password = it
                    validatePassword(password)
                },
                validationResult = signUpUiState.passwordValidationResult,
                placeHolderText = "Enter your password",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                onDone = { focusManger.moveFocus(FocusDirection.Next) },
            )

            ValidatedTextField(
                modifier = Modifier
                    .weight(1f),
                text = confirmPassword,
                onTextChange = {
                    confirmPassword = it
                    validateConfirmPassword(password,it)
                },
                validationResult = signUpUiState.passwordConfirmValidationResult,
                placeHolderText = "Confirm your password",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                onDone = { focusManger.clearFocus() },
            )
        }

        Spacer(modifier = Modifier.weight(5f))

        Button(
            modifier = Modifier
                .weight(3f)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.White
            ),
            onClick = {
                if(isSuccess) {
                    val user = User(
                        name = name,
                        email = email,
                        password = password,
                        isCustomProviderUsed = true
                    )

                    signUp(user)
                }
            },
            enabled = isSuccess,
        ) {
            Text(
                text = "Sign Up",
                style = MaterialTheme.typography.labelLarge
            )
        }

        Spacer(modifier = Modifier.weight(0.4f))

        TextButton(
            onClick = {
                navController.navigate(ScreenRoutes.StarterScreens.LogInRoute) {
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

        Spacer(modifier = Modifier.weight(0.5f))
    }
}