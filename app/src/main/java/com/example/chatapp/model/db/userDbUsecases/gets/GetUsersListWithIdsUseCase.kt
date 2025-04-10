package com.example.chatapp.model.db.userDbUsecases.gets

import com.example.chatapp.Dtos.user.User
import com.example.chatapp.USERS_DB_COLLECTION
import com.example.chatapp.others.Resource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GetUsersListWithIdsUseCase @Inject constructor(
    fireStore: FirebaseFirestore
) {
    private val usersDb  = fireStore.collection(USERS_DB_COLLECTION)

    suspend operator fun invoke(ids: List<String>): Flow<Resource<List<User>>> = flow {
        val documents = usersDb
            .whereIn("id",ids)
            .get().await().documents

        emit(Resource.Success(
            data = documents.map { it.toObject(User::class.java)?: User() })
        )
    }.catch { e ->
        e.printStackTrace()
    }

    suspend operator fun invoke(ids: List<String>,query: String): Flow<Resource<List<User>>> = flow {
        val documents = usersDb
            .whereIn("id",ids)
            .get().await().documents

        emit(Resource.Success(
            data = documents.filter { (it["name"] as String).contains(query,true) }.map { it.toObject(User::class.java)?: User() })
        )
    }.catch { e ->
        e.printStackTrace()
    }

}