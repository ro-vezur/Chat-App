package com.example.chatapp.model.db.userDbUsecases.gets

import com.example.chatapp.Dtos.User
import com.example.chatapp.USERS_DB_COLLECTION
import com.example.chatapp.others.ResourceResult
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FindUsersByNameUseCase @Inject constructor(
    db: FirebaseFirestore,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase
) {
    private val usersDb = db.collection(USERS_DB_COLLECTION)

    suspend operator fun invoke(query: String): Flow<ResourceResult<List<User>>> = flow {
        emit(ResourceResult.Loading())

        val usersDocuments =  usersDb.get().await().documents
        val users = usersDocuments.filter {
            it["name",String::class.java]?.contains(query,ignoreCase = false) == true &&
                    it["id",String::class.java] != getCurrentUserIdUseCase()
        }.map { it.toObject(User::class.java)?: User() }

        emit(ResourceResult.Success(data = users))
    }.catch { e ->
        emit(ResourceResult.Error(message = e.message.toString()))
    }
}