package com.example.chatapp

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.chatapp.Dtos.user.User
import com.example.chatapp.layouts.sharedComponents.viewmodels.UserViewModel
import com.example.chatapp.ui.theme.ChatAppTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

val LocalUser = compositionLocalOf { User() }

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val sharedUserViewModel: UserViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { _, insets -> insets }

        setContent {

            val notificationPermission = rememberPermissionState(
                permission = Manifest.permission.POST_NOTIFICATIONS
            )

            var keepSplashScreen = true
            var isLogged by  remember { mutableStateOf<Boolean?>(null) }

            splashScreen.setKeepOnScreenCondition{ keepSplashScreen}

            lifecycleScope.launch {
                FirebaseAuth.getInstance().addAuthStateListener {  }
                isLogged = FirebaseAuth.getInstance().currentUser != null
                keepSplashScreen = false
            }

            val user by sharedUserViewModel.user.collectAsStateWithLifecycle()
            val unseenMessagesCount by sharedUserViewModel.unseenMessagesCount.collectAsStateWithLifecycle()
            val isAskedForNotificationPermission by sharedUserViewModel.isAskedForNotificationPermission.collectAsStateWithLifecycle()

            FirebaseAuth.getInstance().addAuthStateListener {
                isLogged = it.currentUser != null
                sharedUserViewModel.updateOnlineStatus(isLogged == true)

                if(isLogged == true) {
                    if(!isAskedForNotificationPermission && !notificationPermission.status.isGranted) {
                        notificationPermission.launchPermissionRequest()
                        sharedUserViewModel.saveIsAskedNotificationPermission(true)
                    }
                }

            }

            ChatAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    isLogged?.let {
                        MainView(
                            isLogged = it,
                            user = user,
                            unseenMessagesCount = unseenMessagesCount,
                        )
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("OnResume","Resume!")
        sharedUserViewModel.updateOnlineStatus(true)
    }

    override fun onStop() {
        super.onStop()
        Log.d("OnStop","Stop!")
        sharedUserViewModel.updateOnlineStatus(false)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("OnDestroy","Destroy!")
        sharedUserViewModel.updateOnlineStatus(false)
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
                user = User(),
                unseenMessagesCount = 0,
            )
        }
    }
}