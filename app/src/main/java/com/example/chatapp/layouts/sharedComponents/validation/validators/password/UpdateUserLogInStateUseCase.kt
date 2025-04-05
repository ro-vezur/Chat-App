package com.example.chatapp.layouts.sharedComponents.validation.validators.password

import com.example.chatapp.AWAIT_MINUTES_UNTIL_UNBLOCK
import com.example.chatapp.Dtos.user.LogInState
import com.example.chatapp.Dtos.user.User
import com.example.chatapp.MAX_LOGIN_ATTEMPTS
import com.example.chatapp.USERS_DB_COLLECTION
import com.example.chatapp.helpers.time.getCurrentTimeInMillis
import com.example.chatapp.helpers.time.getFutureTimeInMillis
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UpdateUserLogInStateUseCase @Inject constructor(
    private val db: FirebaseFirestore
) {
    val usersDb = db.collection(USERS_DB_COLLECTION)

    suspend operator fun invoke(email: String,password: String) {
        try {
            val usersDocuments = usersDb.whereEqualTo("email",email).get().await().documents
            val userDocumentRef = usersDocuments.first().reference

            db.runTransaction { transaction ->
                val user = transaction[userDocumentRef].toObject(User::class.java)

                if(user != null) {
                    var logInState = user.logInState

                    logInState.blockedUntil?.let { blockedUntil ->

                        if(getCurrentTimeInMillis() >= blockedUntil) {
                            logInState = LogInState()
                            transaction.update(userDocumentRef,"logInState",logInState)
                        } else {
                            return@runTransaction
                        }
                    }

                    if(user.password != password) {


                        logInState = logInState.copy(
                            failedAttempts = if(getCurrentTimeInMillis() >= (logInState.lastAttemptWasAt ?: Long.MAX_VALUE)) 0 else logInState.failedAttempts + 1,
                            lastAttemptWasAt = getFutureTimeInMillis(AWAIT_MINUTES_UNTIL_UNBLOCK)
                        )

                        if (logInState.failedAttempts >= MAX_LOGIN_ATTEMPTS) {
                            logInState = logInState.copy(
                                blockedUntil = getFutureTimeInMillis(AWAIT_MINUTES_UNTIL_UNBLOCK)
                            )
                            transaction.update(userDocumentRef, "logInState", logInState)
                        } else {
                            transaction.update(userDocumentRef, "logInState", logInState)
                        }
                    } else {
                        logInState = LogInState(
                            failedAttempts = 0,
                            lastAttemptWasAt = null
                        )

                        transaction.update(userDocumentRef,"logInState",logInState)
                    }

                } else {
                    return@runTransaction
                }
            }.await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}