package com.example.chatapp.layouts.sharedComponents.validation.validators.email

import com.example.chatapp.USERS_DB_COLLECTION
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CheckIsEmailRegisteredUseCase @Inject constructor(db: FirebaseFirestore) {
    val usersDb = db.collection(USERS_DB_COLLECTION)

    suspend operator fun invoke(email: String): Boolean {
        return try {
            val filteredUsers = usersDb.whereEqualTo("email",email).get().await()
            val documents = filteredUsers.documents

            documents.isNotEmpty()
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}