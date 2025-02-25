package com.example.chatapp.model.db.userDbUsecases.gets

import com.example.chatapp.Dtos.User
import com.example.chatapp.USERS_DB_COLLECTION
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GetUserUseCase @Inject constructor(db: FirebaseFirestore) {
    private val usersDb = db.collection(USERS_DB_COLLECTION)

    suspend operator fun invoke(id: String): User {
        return try {
            
            val user = usersDb.document(id).get().await()

            user.toObject(User::class.java)?: User()
        } catch (e: Exception) {
            User()
        }
    }
}