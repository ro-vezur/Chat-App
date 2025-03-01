package com.example.chatapp.layouts.sharedComponents.validation.validators.password

import com.example.chatapp.Dtos.user.LogInState
import com.example.chatapp.Dtos.user.User
import com.example.chatapp.USERS_DB_COLLECTION
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GetUserLoginStateUseCase @Inject constructor(
    db: FirebaseFirestore
) {
    private val users = db.collection(USERS_DB_COLLECTION)

    suspend operator fun invoke(email: String): LogInState? {
        return try {
            val usersDocuments = users.whereEqualTo("email",email).get().await().documents
            if(usersDocuments.isNotEmpty()) {
                val user = usersDocuments.first().toObject(User::class.java)
                user?.logInState
            } else null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}