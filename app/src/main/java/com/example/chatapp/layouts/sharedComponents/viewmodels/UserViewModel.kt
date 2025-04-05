package com.example.chatapp.layouts.sharedComponents.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.Dtos.user.User
import com.example.chatapp.USERS_DB_COLLECTION
import com.example.chatapp.model.datastore.permissionPreferences.PermissionsPreferencesRepository
import com.example.chatapp.model.db.messagesDbUseCases.gets.GetAllUserChatsUnseenMessagesCountUseCase
import com.example.chatapp.model.db.userDbUsecases.posts.userOnlineStatus.AddUserDeviceUseCase
import com.example.chatapp.model.db.userDbUsecases.posts.userOnlineStatus.DeleteUserDeviceUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private var userStateListener: ListenerRegistration? = null

@HiltViewModel
class UserViewModel @Inject constructor(
    db: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val permissionsPreferencesRepository: PermissionsPreferencesRepository,
    private val addUserDeviceUseCase: AddUserDeviceUseCase,
    private val removeUserDeviceUseCase: DeleteUserDeviceUseCase,
    private val getAllUserChatsUnseenMessagesCountUseCase: GetAllUserChatsUnseenMessagesCountUseCase,
): ViewModel() {

    private val usersDb = db.collection(USERS_DB_COLLECTION)

    private val _user: MutableStateFlow<User> = MutableStateFlow(User())
    val user: StateFlow<User> = _user.asStateFlow()
    
    private val _unseenMessagesCount: MutableStateFlow<Int> = MutableStateFlow(0)
    val unseenMessagesCount: StateFlow<Int> = _unseenMessagesCount.asStateFlow()

    private val _isAskedForNotificationPermission: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isAskedForNotificationPermission: StateFlow<Boolean> = _isAskedForNotificationPermission.asStateFlow()

    init {
        viewModelScope.launch {
            fetchUser()
            fetchIsAskedNotificationPermission()
        }
    }

    private fun fetchUser() = viewModelScope.launch {
        auth.addAuthStateListener { authState ->
            if(userStateListener != null) {
                userStateListener?.remove()
            }

            getAllUserChatsUnseenMessagesCount(isLogged = authState.currentUser != null)

            try {
                userStateListener = usersDb.document(authState.currentUser?.uid.toString()).addSnapshotListener { document, error ->
                    if (error != null) return@addSnapshotListener

                    document?.toObject(User::class.java)?.let { userObject ->
                        _user.value = userObject
                    }
                }
            } catch (e: Exception) {
                Log.e("Error fetching current user",e.message.toString())
            }
        }
    }

    private fun getAllUserChatsUnseenMessagesCount(isLogged: Boolean) = viewModelScope.launch {
        if(isLogged) {
            getAllUserChatsUnseenMessagesCountUseCase(_user.value.id).collectLatest { messagesCount ->
                _unseenMessagesCount.update { messagesCount }
            }
        }
    }

    fun updateOnlineStatus(status: Boolean) = viewModelScope.launch {
        if(status) {
            addUserDeviceUseCase(_user.value.id)
        } else {
            removeUserDeviceUseCase(_user.value.id)
        }
    }

    private fun fetchIsAskedNotificationPermission() = viewModelScope.launch {
        permissionsPreferencesRepository.askedForNotificationPermissionFlow.collectLatest { isAsked ->
            _isAskedForNotificationPermission.emit(isAsked)
        }
    }

    fun saveIsAskedNotificationPermission(isAsked: Boolean) = viewModelScope.launch {
        permissionsPreferencesRepository.saveNotificationPermission(isAsked)
    }

}