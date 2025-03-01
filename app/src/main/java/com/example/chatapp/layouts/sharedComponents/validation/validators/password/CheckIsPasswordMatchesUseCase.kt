package com.example.chatapp.layouts.sharedComponents.validation.validators.password

import com.example.chatapp.USERS_DB_COLLECTION
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CheckIsPasswordMatchesUseCase @Inject constructor(
    db: FirebaseFirestore
) {
    val usersDb = db.collection(USERS_DB_COLLECTION)

    suspend operator fun invoke(email: String, password: String): Boolean {
        return try {
            val usersDocuments = usersDb.whereEqualTo("email",email).whereEqualTo("password",password).get().await()
            usersDocuments.documents.isNotEmpty()
        } catch (e: Exception) {
            false
        }
    }
}