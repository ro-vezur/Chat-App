package com.example.chatapp.model.datastore.permissionPreferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PermissionsPreferencesRepository @Inject constructor(
    private val datastore: DataStore<Preferences>
) {

    companion object {
        val NOTIFICATION_PERMISSION_KEY = booleanPreferencesKey("NOTIFICATION_PERMISSION")
    }

    val askedForNotificationPermissionFlow: Flow<Boolean> = datastore.data
        .map { preferences -> preferences[NOTIFICATION_PERMISSION_KEY]?: false }

    suspend fun saveNotificationPermission(isAsked: Boolean) {
        datastore.edit { preferences ->
            preferences[NOTIFICATION_PERMISSION_KEY] = isAsked
        }
    }

}