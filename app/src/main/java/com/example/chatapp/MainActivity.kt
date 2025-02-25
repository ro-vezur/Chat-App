package com.example.chatapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.chatapp.Dtos.User
import com.example.chatapp.layouts.sharedComponents.viewmodels.UserViewModel
import com.example.chatapp.ui.theme.ChatAppTheme
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

val LocalUser = compositionLocalOf { User() }

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        setContent {

            var keepSplashScreen = true
            var isLogged by  remember { mutableStateOf(false) }

            splashScreen.setKeepOnScreenCondition{ keepSplashScreen}

            LaunchedEffect(Unit){
                lifecycleScope.launch {
                    isLogged = FirebaseAuth.getInstance().currentUser != null
                    delay(300)
                    keepSplashScreen = false
                }
            }

            val userViewModel: UserViewModel = hiltViewModel()
            val user by userViewModel.user.collectAsStateWithLifecycle()

            FirebaseAuth.getInstance().addAuthStateListener {
                isLogged = it.currentUser != null

                Log.d("isLogged",isLogged.toString())

            }

            enableEdgeToEdge()

            ChatAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainView(
                        isLogged = isLogged,
                        user = user,
                    )
                }
            }
        }
    }

}


@Preview(device = "spec:width=400dp,height=850dp,orientation=portrait")
@Preview(device = "spec:width=700dp,height=1000dp,orientation=portrait")
@Composable
fun GreetingPreview() {
    ChatAppTheme(
        darkTheme = true
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            MainView(
                isLogged = false,
                user = User()
            )
        }
    }
}