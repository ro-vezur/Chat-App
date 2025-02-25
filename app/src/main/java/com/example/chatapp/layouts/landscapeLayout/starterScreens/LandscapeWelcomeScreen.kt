package com.example.chatapp.layouts.landscapeLayout.starterScreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AllInclusive
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale

@Composable
fun LandscapeWelcomeScreen(
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(0.9f))

            Image(
                imageVector = Icons.Filled.AllInclusive,
                contentDescription = "App Logo",
                contentScale = ContentScale.Crop,
                colorFilter = ColorFilter.tint(colorScheme.onBackground),
                modifier = Modifier.weight(1f)
            )

            Text(
                text = "Welcome to ChatBot!",
                style = typography.displayLarge,
                color = colorScheme.onBackground
            )

            Spacer(modifier = Modifier.weight(1f))
        }

        Column(
            modifier = Modifier
                .weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(2.5f))

            Button(
                onClick = onSignUpClick,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .weight(1f),
                shape = RoundedCornerShape(20)
            ) {
                Text(
                    text = "Sign Up",
                    style = typography.labelLarge,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.weight(0.5f))

            OutlinedButton(
                onClick = onLoginClick,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .weight(1f),
                shape = RoundedCornerShape(20)
            ) {
                Text(
                    text = "Log In",
                    style = typography.labelLarge
                )
            }

            Spacer(modifier = Modifier.weight(0.3f))

            TextButton(
                onClick = {  },
                modifier = Modifier.alpha(0.7f)
            ) {
                Text(
                    text = "Continue as Guest",
                    style = typography.labelLarge
                )
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}
