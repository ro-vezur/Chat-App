package com.example.chatapp.model.db.userDbUsecases.posts

import com.example.chatapp.Dtos.User
import com.example.chatapp.Dtos.requests.FriendRequest
import com.example.chatapp.USERS_DB_COLLECTION
import com.example.chatapp.helpers.time.getCurrentTimeInMillis
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class SendFriendRequestUseCase @Inject constructor(
    private val db: FirebaseFirestore,
) {
    private val usersDb = db.collection(USERS_DB_COLLECTION)

    operator fun invoke(sender: User, receiver: User): Task<Unit> {
        return db.runTransaction { transaction ->
            val receiverDocumentRef = usersDb.document(receiver.id)

            val user = transaction.get(receiverDocumentRef).toObject(User::class.java)?: User()
            val requests = user.requests

            if(requests.map { it.userId }.contains(sender.id) || user.friends.contains(sender.id)) {
                return@runTransaction
            } else {
                requests.add(FriendRequest(sender.id, getCurrentTimeInMillis()))
                transaction.update(receiverDocumentRef,"requests",requests)
            }
        }
    }
}