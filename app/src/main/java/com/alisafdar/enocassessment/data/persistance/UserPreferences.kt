package com.alisafdar.enocassessment.data.persistance

import android.content.Context
import android.provider.ContactsContract
import androidx.datastore.DataStore
import com.alisafdar.enocassessment.datastore.data.PreferenceData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.prefs.Preferences
import javax.inject.Inject

class UserPreferences @Inject constructor(private val dataStore: DataStore<PreferenceData>) {

    val accessToken: Flow<String?>
        get() = dataStore.data.map { it.access_token }

    val refreshToken: Flow<String?>
        get() = dataStore.data.map { it.refresh_token }

    suspend fun saveAccessTokens(accessToken: String, refreshToken: String) {
        dataStore.updateData { data ->
            data.copy(accessToken, refreshToken)
        }
    }

    suspend fun clear() {
//        dataStore.updateData { data ->
//        }
    }
}