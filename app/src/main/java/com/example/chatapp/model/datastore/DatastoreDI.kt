package com.example.chatapp.model.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.example.chatapp.PERMISSIONS_PREFERENCES
import com.example.chatapp.model.datastore.permissionPreferences.PermissionsPreferencesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatastoreDI {

    @Provides
    @Singleton
    fun providePermissionStore( @ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            migrations = listOf(SharedPreferencesMigration(context, PERMISSIONS_PREFERENCES)),
            produceFile = { context.preferencesDataStoreFile(PERMISSIONS_PREFERENCES) }
        )
    }


    @Provides
    @Singleton
    fun providePermissionsPreferencesRepository(datastore: DataStore<Preferences>): PermissionsPreferencesRepository {
        return PermissionsPreferencesRepository(datastore)
    }
}