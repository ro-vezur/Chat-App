package com.example.chatapp.layouts.sharedComponents.validation.validators

import com.example.chatapp.layouts.sharedComponents.validation.validators.email.CheckIsEmailRegisteredUseCase
import com.example.chatapp.layouts.sharedComponents.validation.validators.email.ValidateEmailUseCase
import com.example.chatapp.layouts.sharedComponents.validation.validators.password.CheckIsPasswordMatchesUseCase
import com.example.chatapp.layouts.sharedComponents.validation.validators.password.GetUserLoginStateUseCase
import com.example.chatapp.layouts.sharedComponents.validation.validators.password.UpdateUserLogInStateUseCase
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ValidatorsDI {

    @Provides
    @Singleton
    fun provideGetUserLogInState(db: FirebaseFirestore): GetUserLoginStateUseCase {
        return GetUserLoginStateUseCase(db)
    }

    @Provides
    @Singleton
    fun provideUpdateUserLoginState(db: FirebaseFirestore): UpdateUserLogInStateUseCase {
        return UpdateUserLogInStateUseCase(db)
    }

    @Provides
    @Singleton
    fun provideCheckIsPasswordMatches(db: FirebaseFirestore): CheckIsPasswordMatchesUseCase {
        return CheckIsPasswordMatchesUseCase(db)
    }

    @Provides
    @Singleton
    fun provideCheckIsEmailRegisteredUseCase(db: FirebaseFirestore): CheckIsEmailRegisteredUseCase {
        return CheckIsEmailRegisteredUseCase(db)
    }

    @Provides
    @Singleton
    fun provideEmailValidator(checkIsEmailRegisteredUseCase: CheckIsEmailRegisteredUseCase): ValidateEmailUseCase {
        return ValidateEmailUseCase(checkIsEmailRegisteredUseCase)
    }
}