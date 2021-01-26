package com.techbayportal.itaste.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import com.google.gson.Gson
import com.techbayportal.itaste.constants.AppConstants
import com.techbayportal.itaste.data.models.LoginResponse
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
        val USER_TYPE_KEY = preferencesKey<String>(AppConstants.DataStore.USER_TYPE_KEY)
        //val USER_DATA = preferencesKey<String>(AppConstants.DataStore.USER_DATA)
        val USER_OBJECT = preferencesKey<String>(AppConstants.DataStore.USER_OBJ)
        val IS_DARK_MODE = preferencesKey<String>(AppConstants.DataStore.IS_DARK_MODE)
        val LANGUAGE_PREF = preferencesKey<String>(AppConstants.DataStore.LANGUAGE_PREF)

    }

    //Store data
    suspend fun storeData(isLocalizationKey: Boolean, name: String) {
        dataStore.edit {
            it[IS_LOCALIZATION_KEY] = isLocalizationKey
            it[USER_NAME_KEY] = name
        }
    }

    //SignupConfigration
    suspend fun signUpConfig(isDarkMode: Boolean, languagePref : String){
        dataStore.edit {
            it
        }

    }

    //store user object
    suspend fun saveUserObj(userObject: LoginResponse){
        val gson = Gson()
        val json = gson.toJson(userObject)
        dataStore.edit {
            it[USER_OBJECT] = json
        }
    }

    suspend fun clearUserObj(){
        dataStore.edit {
            it[USER_OBJECT] = ""
        }
    }

    /*//Store User Object For Session
    suspend fun storeUserData(userData : LoginResponse){
        val gson = Gson()
        val json = gson.toJson(userData)
        dataStore.edit {
            it[USER_OBJECT] = json
        }
    }*/

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



    //Store userType
    suspend fun storeUserType(userType: String) {
        dataStore.edit {
            it[USER_TYPE_KEY] = userType
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
        it[ARABIC_LANGUAGE_KEY] ?: ""
    }

    //Create userType flow
    val userTypeFlow: Flow<String> = dataStore.data.map {
        it[USER_TYPE_KEY] ?: ""
    }

    //flow for user object
    /*val userDataFlow: Flow<String> = dataStore.data.map {
        it[USER_DATA] ?: ""
    }*/

    //Create an userObject flow
    val userObjFlow: Flow<String?> = dataStore.data.map {
        it[USER_OBJECT]
    }


}