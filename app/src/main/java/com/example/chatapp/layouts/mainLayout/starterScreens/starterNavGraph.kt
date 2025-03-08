package com.example.chatapp.layouts.mainLayout.starterScreens

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.chatapp.layouts.mainLayout.starterScreens.logInScreen.LogInScreen
import com.example.chatapp.layouts.mainLayout.starterScreens.logInScreen.LogInViewModel
import com.example.chatapp.layouts.mainLayout.starterScreens.signUpScreen.SignUpScreen
import com.example.chatapp.layouts.mainLayout.starterScreens.signUpScreen.SignUpViewModel
import com.example.chatapp.navigation.ScreenRoutes
import com.example.chatapp.others.Resource

fun NavGraphBuilder.starterNavGraph(navController: NavController) {

    navigation<ScreenRoutes.StarterScreens>(
        startDestination = ScreenRoutes.StarterScreens.WelcomeRoute
    ) {
        composable<ScreenRoutes.StarterScreens.WelcomeRoute> {

            WelcomeScreen(
                onLoginClick = {
                    navController.navigate(ScreenRoutes.StarterScreens.LogInRoute) {
                        launchSingleTop = true
                    }

                },
                onSignUpClick = {
                    navController.navigate(ScreenRoutes.StarterScreens.SignUpRoute) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable<ScreenRoutes.StarterScreens.SignUpRoute> {
            val signUpViewModel: SignUpViewModel = hiltViewModel()

            val signUpUiState by signUpViewModel.signUpUiState.collectAsStateWithLifecycle()

            LaunchedEffect(signUpUiState.signUpResult) {
                when(signUpUiState.signUpResult) {
                    is Resource.Loading -> {

                    }
                    is Resource.Success -> {
                        signUpViewModel.addUserToDb(
                            user = signUpUiState.user.copy(
                                id = signUpUiState.signUpResult.data?.user?.uid.toString(),
                                isCustomProviderUsed = true
                            ),
                            onSuccess =  {
                                navController.navigate(ScreenRoutes.LoggedScreens)
                            }
                        )
                    }
                    is Resource.Error -> {

                    }
                }
            }

            SignUpScreen(
                navController = navController,
                signUpUiState = signUpUiState,
                validateName = signUpViewModel::updateName,
                validateEmail = signUpViewModel::updateEmail,
                validatePassword = signUpViewModel::updatePassword,
                validateConfirmPassword = signUpViewModel::updateConfirmPassword,
                signUp = signUpViewModel::signUp,
            )
        }

        composable<ScreenRoutes.StarterScreens.LogInRoute> {
            val logInViewModel: LogInViewModel = hiltViewModel()

            val logInUiState by logInViewModel.logInUiState.collectAsStateWithLifecycle()

            LaunchedEffect(logInUiState.logInResult) {
                when(logInUiState.logInResult) {
                    is Resource.Loading -> {

                    }
                    is Resource.Success -> {
                        navController.navigate(ScreenRoutes.LoggedScreens)
                    }
                    is Resource.Error -> {

                    }
                }
            }

            LogInScreen(
                navController = navController,
                logInUiState = logInUiState,
                validateEmail = logInViewModel::updateEmail,
                validatePassword = logInViewModel::updatePassword,
                logIn = logInViewModel::logIn
            )
        }
    }
}