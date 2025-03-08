package com.example.chatapp.layouts.sharedComponents.images

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import com.example.chatapp.R

@Composable
fun UserImage(
    modifier: Modifier = Modifier,
    imageUrl: String?
) {
    if(imageUrl != null) {
        AsyncImage(
            modifier = modifier,
            model = imageUrl,
            contentDescription = "user image",
            contentScale = ContentScale.Crop
        )
    } else {
        Image(
            modifier = modifier,
            painter = painterResource(id = R.drawable.empty_profile),
            contentDescription = "empty user image",
            contentScale = ContentScale.Crop
        )
    }
}