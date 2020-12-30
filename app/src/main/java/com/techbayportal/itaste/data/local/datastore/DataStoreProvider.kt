package com.techbayportal.itaste.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import com.techbayportal.itaste.constants.AppConstants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreProvider(context: Context) {

    //Create the dataStore
    private val dataStore = context.createDataStore(name = AppConstants.DataStore.DATA_STORE_NAME)

    //Create some keys
    companion object {
        val IS_LOCALIZATION_KEY = preferencesKey<Boolean>(AppConstants.DataStore.LOCALIZATION_KEY_NAME)
        val USER_NAME_KEY = preferencesKey<String>(AppConstants.DataStore.USER_NAME_KEY)
        val DARK_MODE_KEY = preferencesKey<Boolean>(AppConstants.DataStore.DARK_MODE_KEY)
        val ARABIC_LANGUAGE_KEY = preferencesKey<String>(AppConstants.DataStore.ARABIC_LANGUAGE_KEY)

    }

    //Store data
    suspend fun storeData(isLocalizationKey: Boolean, name: String) {
        dataStore.edit {
            it[IS_LOCALIZATION_KEY] = isLocalizationKey
            it[USER_NAME_KEY] = name
        }
    }
    suspend fun storeDarkMode(isDarkModeOn: Boolean) {
        dataStore.edit {
            it[DARK_MODE_KEY] = isDarkModeOn
        }
    }

    suspend fun storeLanguage(isArabicLanguageActive: String) {
        dataStore.edit {
            it[ARABIC_LANGUAGE_KEY] = isArabicLanguageActive
        }
    }

    //Create an Localization flow
    val localizationFlow: Flow<Boolean> = dataStore.data.map {
        it[IS_LOCALIZATION_KEY] ?: false
    }

    //Create a name flow
    val userNameFlow: Flow<String> = dataStore.data.map {
        it[USER_NAME_KEY] ?: ""
    }

    val darkModeFlow: Flow<Boolean> = dataStore.data.map {
        it[DARK_MODE_KEY] ?: false
    }

    val languageFlow: Flow<String> = dataStore.data.map {
        it[ARABIC_LANGUAGE_KEY] ?: "English"
    }


}