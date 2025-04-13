package com.example.chatapp.layouts.mainLayout.loggedScreens.screens.editProfileScreen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.example.chatapp.Dtos.user.User
import com.example.chatapp.LocalUser
import com.example.chatapp.differentScreensSupport.sdp
import com.example.chatapp.layouts.sharedComponents.images.UserImage

@Composable
fun EditProfileScreen(
    uploadImage: (Uri,(String) -> Unit) -> Unit,
    updateUser: (User) -> Unit,
) {
    val user = LocalUser.current
    val context = LocalContext.current

    var selectedImage by remember {
        mutableStateOf(if(user.imageUrl == null) null else Uri.parse(user.imageUrl))
    }

    val imageCropLauncher = rememberLauncherForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {

            selectedImage = result.uriContent
        } else {
            val exception = result.error
        }
    }

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            val cropOptions = CropImageContractOptions(uri, CropImageOptions())
            imageCropLauncher.launch(cropOptions)
        }
    )
    
    Column(
        modifier = Modifier
            .padding(top = 20.sdp)
            .padding(horizontal = 32.sdp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .clickable {
                     singlePhotoPickerLauncher.launch(
                         input = PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                     )
                },
        ) {
            UserImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.sdp)
                    .clip(RoundedCornerShape(10.sdp)),
                uri = selectedImage
            )
        }

        Button(
            onClick = {
                when {
                    selectedImage != Uri.parse(user.imageUrl) -> {
                        selectedImage?.let { uri ->
                             uploadImage(uri) { newImageUrl ->
                                 val newUser = user.copy(imageUrl = newImageUrl)
                                 if(newUser != user) {
                                     selectedImage = Uri.parse(newImageUrl)
                                     updateUser(newUser)
                                 }
                             }
                        }
                    }
                    else -> {

                    }
                }
            }
        ) {
            Text(
                text = "Save"
            )
        }
    }
    
}