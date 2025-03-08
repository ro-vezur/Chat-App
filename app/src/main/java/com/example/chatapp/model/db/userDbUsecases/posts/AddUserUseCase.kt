package com.example.chatapp.model.db.userDbUsecases.posts

import com.example.chatapp.Dtos.user.User
import com.example.chatapp.USERS_DB_COLLECTION
import com.example.chatapp.others.Resource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AddUserUseCase @Inject constructor(db: FirebaseFirestore) {
    private val usersDb = db.collection(USERS_DB_COLLECTION)

    suspend operator fun invoke(user: User): Flow<Resource<User?>> = flow<Resource<User?>> {
        emit(Resource.Loading())

        val userDocument = usersDb.document(user.id).get().await()

        if(userDocument.exists()) {
            emit(Resource.Error(message = "User already exists"))
        } else {
            emit(Resource.Success(data = user))
            usersDb.document(user.id).set(user).await()
        }
    }.catch { e ->
        emit(Resource.Error(message = e.message.toString()))
    }
}