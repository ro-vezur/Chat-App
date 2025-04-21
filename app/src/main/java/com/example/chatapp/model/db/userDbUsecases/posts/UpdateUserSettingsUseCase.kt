package com.example.chatapp.model.db.userDbUsecases.posts

import com.example.chatapp.Dtos.user.userSettings.UserSettings
import com.example.chatapp.USERS_DB_COLLECTION
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class UpdateUserSettingsUseCase @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    operator fun invoke(userId: String,settings: UserSettings) {
        firestore.runTransaction { transaction ->
            val userRef = firestore.collection(USERS_DB_COLLECTION).document(userId)
            val userSnapshot = transaction[userRef]

            if(userSnapshot.exists()) {
                transaction.update(userRef,"settings",settings)
            }
        }
    }
}