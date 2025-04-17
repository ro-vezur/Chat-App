package com.example.chatapp.layouts.mainLayout.loggedScreens.screens.editProfileScreen

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.Dtos.Media.ImageBody
import com.example.chatapp.Dtos.user.User
import com.example.chatapp.domain.MediaInterface
import com.example.chatapp.model.apis.apisUsecases.DeleteImageUseCase
import com.example.chatapp.model.db.userDbUsecases.posts.UpdateUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val mediaImpl: MediaInterface,
    private val updateUserUseCase: UpdateUserUseCase,
    private val deleteImageUseCase: DeleteImageUseCase,
): ViewModel() {
    fun getImageFromServer() {
        mediaImpl.getImageFromServer()
    }

    fun uploadImageToServer(file: Uri,onSuccess: (String) -> Unit) = viewModelScope.launch {
        mediaImpl.uploadImageToServer(file,onSuccess)
    }

    fun updateUser(user: User,onSuccess: () -> Unit) = viewModelScope.launch {
        updateUserUseCase(user)
        onSuccess()
    }

    fun deleteImage(image: ImageBody) = viewModelScope.launch {
        deleteImageUseCase.invoke(image)
    }
}