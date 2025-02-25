package com.example.chatapp.model.db.userDbUsecases.posts

import com.example.chatapp.Dtos.User
import com.example.chatapp.USERS_DB_COLLECTION
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class AcceptFriendRequestUseCase @Inject constructor(
    private val db: FirebaseFirestore
) {
    val usersDb = db.collection(USERS_DB_COLLECTION)

    operator fun invoke(acceptorUserId: String, newFriendId: String) {
        db.runTransaction { transaction ->
            val userDocumentRef = usersDb.document(acceptorUserId)
            val user = transaction.get(userDocumentRef).toObject(User::class.java)

            if(user != null) {
                val friendsList = user.friends
                val requests = user.requests

                if(!friendsList.contains(newFriendId)) {
                    friendsList.add(newFriendId)
                    transaction.update(userDocumentRef,"friends",friendsList)
                }

                if(requests.map { it.userId }.contains(newFriendId)) {
                    requests.removeAll { it.userId == newFriendId }
                    transaction.update(userDocumentRef,"requests",requests)
                }
            }
        }
    }
}