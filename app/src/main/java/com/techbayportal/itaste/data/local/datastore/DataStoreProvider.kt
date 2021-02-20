package com.techbayportal.itaste.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.emptyPreferences
import androidx.datastore.preferences.preferencesKey
import com.google.gson.Gson
import com.techbayportal.itaste.constants.AppConstants
import com.techbayportal.itaste.data.models.LoginResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

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
        val SWITCH_TO_PREMIUM = preferencesKey<Boolean>(AppConstants.DataStore.SWITCH_TO_PREMIUM)
        val GUEST_MODE = preferencesKey<Boolean>(AppConstants.DataStore.GUEST_MODE)
        val KEY_FCM = preferencesKey<String>(AppConstants.DataStore.FCM_KEY)

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

    //Switch to Premium
    suspend fun switchToPremium(switchToPremium: Boolean){
        dataStore.edit {
            it[SWITCH_TO_PREMIUM] = switchToPremium
        }
    }

    //Guest Mode
    suspend fun guestMode(guestMode: Boolean){
        dataStore.edit {
            it[GUEST_MODE] = guestMode
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

    //Store fcmToken
    suspend fun setFcm(fcm: String) {
        dataStore.edit {
            it[KEY_FCM] = fcm
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

    val switchToPremiumFlow: Flow<Boolean> = dataStore.data.map {
        it[SWITCH_TO_PREMIUM] ?: false
    }

    val guestModeFlow: Flow<Boolean> = dataStore.data.map {
        it[GUEST_MODE] ?: false
    }

    fun getFcm(): Flow<String> {
        return dataStore.data
            .catch { exception ->
                // dataStore.data throws an IOException when an error is encountered when reading data
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }.map {
                it[KEY_FCM] ?: ""
            }
    }


}