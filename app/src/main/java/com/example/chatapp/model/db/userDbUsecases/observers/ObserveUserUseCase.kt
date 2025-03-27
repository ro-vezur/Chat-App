package com.example.chatapp.model.db.userDbUsecases.observers

import com.example.chatapp.Dtos.user.User
import com.example.chatapp.USERS_DB_COLLECTION
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class ObserveUserUseCase @Inject constructor(
    db: FirebaseFirestore
) {
    val usersDb = db.collection(USERS_DB_COLLECTION)

    operator fun invoke(userId: String) = callbackFlow {
        val listener = usersDb.document(userId).addSnapshotListener { snapshot, error ->
            if(error != null) {
                close(error)
                return@addSnapshotListener
            }

            val user = snapshot?.toObject(User::class.java)

            if(user != null) {
                trySend(user)
            }
        }

        awaitClose { listener.remove() }
    }
}