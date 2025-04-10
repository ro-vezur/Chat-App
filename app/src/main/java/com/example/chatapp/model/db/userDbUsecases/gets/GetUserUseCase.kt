package com.example.chatapp.model.db.userDbUsecases.gets

import android.util.Log
import com.example.chatapp.Dtos.user.User
import com.example.chatapp.USERS_DB_COLLECTION
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException
import javax.inject.Inject

class GetUserUseCase @Inject constructor(fireStore: FirebaseFirestore) {
    private val usersDb = fireStore.collection(USERS_DB_COLLECTION)

    suspend operator fun invoke(id: String): User? {
        return try {
            
            val user = usersDb.document(id).get().await()

            user.toObject(User::class.java)
        }
        catch (e: CancellationException) {
            throw e
        }
        catch (e: Exception) {
            Log.d("get user error",e.toString())
            null
        }
    }
}