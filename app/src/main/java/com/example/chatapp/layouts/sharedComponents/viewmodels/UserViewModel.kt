package com.example.chatapp.layouts.sharedComponents.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.Dtos.user.User
import com.example.chatapp.USERS_DB_COLLECTION
import com.example.chatapp.helpers.time.getCurrentTimeInMillis
import com.example.chatapp.model.datastore.permissionPreferences.PermissionsPreferencesRepository
import com.example.chatapp.model.db.userDbUsecases.posts.SetLastTimeSeenUseCase
import com.example.chatapp.model.db.userDbUsecases.posts.UpdateOnlineStatusUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

private var userStateListener: ListenerRegistration? = null

@HiltViewModel
class UserViewModel @Inject constructor(
    db: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val updateOnlineStatusUseCase: UpdateOnlineStatusUseCase,
    private val setLastTimeSeenUseCase: SetLastTimeSeenUseCase,
    private val permissionsPreferencesRepository: PermissionsPreferencesRepository,
): ViewModel() {

    private val usersDb = db.collection(USERS_DB_COLLECTION)

    private val _user: MutableStateFlow<User> = MutableStateFlow(User())
    val user: StateFlow<User> = _user.asStateFlow()

    private val _isAskedForNotificationPermission: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isAskedForNotificationPermission: StateFlow<Boolean> = _isAskedForNotificationPermission.asStateFlow()

    init {
        fetchUser()
        fetchIsAskedNotificationPermission()
    }

    private fun fetchUser() = viewModelScope.launch {
        auth.addAuthStateListener { authState ->
            if(userStateListener != null) {
                userStateListener?.remove()
            }

            userStateListener = usersDb.document(authState.currentUser?.uid.toString()).addSnapshotListener { document, error ->
                if (error != null) return@addSnapshotListener

                document?.toObject(User::class.java)?.let { userObject ->
                    _user.value = userObject
                }
            }
        }
    }

    fun updateOnlineStatus(status: Boolean) = viewModelScope.launch {
        if(status) {
            updateOnlineStatusUseCase(true)
            setLastTimeSeenUseCase(null)
        } else {
            setLastTimeSeenUseCase(getCurrentTimeInMillis())
            updateOnlineStatusUseCase(false)
        }
    }

    fun fetchIsAskedNotificationPermission() = viewModelScope.launch {
        permissionsPreferencesRepository.askedForNotificationPermissionFlow.collectLatest { isAsked ->
            _isAskedForNotificationPermission.emit(isAsked)
        }
    }

    fun saveIsAskedNotificationPermission(isAsked: Boolean) = viewModelScope.launch {
        permissionsPreferencesRepository.saveNotificationPermission(isAsked)
    }

}