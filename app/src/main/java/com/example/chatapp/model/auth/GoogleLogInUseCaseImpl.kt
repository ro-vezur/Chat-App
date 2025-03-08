package com.example.chatapp.model.auth

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.example.chatapp.Dtos.user.User
import com.example.chatapp.domain.auth.GoogleLogInUseCase
import com.example.chatapp.model.db.userDbUsecases.posts.AddUserUseCase
import com.example.chatapp.others.Resource
import com.example.chatapp.webClientId
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GoogleLogInUseCaseImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val context: Context,
    private val addUserUseCase: AddUserUseCase,
): GoogleLogInUseCase {
    override operator fun invoke(): Flow<Resource<AuthResult>> = flow {
        emit(Resource.Loading())

        val googleOption: GetSignInWithGoogleOption = GetSignInWithGoogleOption.Builder(webClientId)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleOption)
            .build()

        try {
            val credentialManager = CredentialManager.create(context)
            val result = credentialManager.getCredential(
                context = context,
                request = request
            )

            val credential = result.credential

            if(credential is CustomCredential) {
                if(credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleIdTokenCredential = GoogleIdTokenCredential
                            .createFrom(credential.data)

                        val firebaseCredential = GoogleAuthProvider
                            .getCredential(
                                googleIdTokenCredential.idToken,
                                null
                            )
                        val signInResult = firebaseAuth.signInWithCredential(firebaseCredential).await()

                        emit(Resource.Success(data = signInResult))

                        val user = User(
                            id = signInResult.user?.uid.toString(),
                            name = signInResult.user?.displayName.toString(),
                            email = signInResult.user?.email.toString(),
                            password = "None",
                            isCustomProviderUsed = false
                        )

                        addUserUseCase(user)

                    } catch (e: Exception) {
                        emit(
                            Resource.Error(
                                message = e.message.toString(),
                            )
                        )
                    }
                }
            }
        } catch (e: Exception) {
            emit(
                Resource.Error(
                    message = e.message.toString()
                )
            )
        }
    }
}