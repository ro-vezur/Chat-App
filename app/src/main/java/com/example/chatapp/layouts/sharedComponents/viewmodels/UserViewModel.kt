package com.example.chatapp.layouts.sharedComponents.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.Dtos.User
import com.example.chatapp.USERS_DB_COLLECTION
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private var userStateListener: ListenerRegistration? = null

@HiltViewModel
class UserViewModel @Inject constructor(
    db: FirebaseFirestore,
    private val auth: FirebaseAuth,
): ViewModel() {

    private val usersDb = db.collection(USERS_DB_COLLECTION)

    private val _user: MutableStateFlow<User> = MutableStateFlow(User())
    val user: StateFlow<User> = _user.asStateFlow()

    init {
        fetchUser()
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

}