package com.example.chattingappscreens.core.preference

import android.content.Context
import android.text.BoringLayout
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.chattingappscreens.data.modell.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


val Context.dataStore  by preferencesDataStore(name = "USER_PREFERENCE")

class PreferenceData( private val dataStore: DataStore<Preferences>) {

    private val USER_UID_KEY  = stringPreferencesKey("user_uid")
    private val USER_NAME_KEY = stringPreferencesKey("user_name")
    private val USER_EMAIL_KEY = stringPreferencesKey("user_email")
    private val USER_PHONE_KEY = stringPreferencesKey("user_phone")
    private val USER_PROFILE_KEY = stringPreferencesKey("user_profile")
    private val USER_IS_ONLINE_KEY = booleanPreferencesKey("user_isOnline")
    private val USER_LAST_SEEN_KEY = stringPreferencesKey("user_lastSeen")
    private val USER_FCM_TOKEN = stringPreferencesKey("user_fcmToken")
    private val USER_LOGGED_IN = booleanPreferencesKey("logged_in")


    val uid : Flow<String> = dataStore.data.map { preferences ->  preferences[USER_UID_KEY] ?: "" }    //key ke value ko live set krre hai
    val name : Flow<String> = dataStore.data.map { preferences -> preferences[USER_NAME_KEY] ?: ""}
    val email : Flow<String>  = dataStore.data.map { preferences -> preferences[USER_EMAIL_KEY] ?: ""}
    val phone : Flow<String> = dataStore.data.map { preferences -> preferences[USER_PHONE_KEY] ?: "" }
    val profile : Flow<String> = dataStore.data.map { preferences -> preferences[USER_PROFILE_KEY] ?: "" }
    val isOnline : Flow<Boolean> = dataStore.data.map { preferences -> preferences[USER_IS_ONLINE_KEY] ?:  false }
    val lastSeen : Flow<String> = dataStore.data.map {preferences -> preferences[USER_LAST_SEEN_KEY] ?: "" }
    val fcmToken : Flow<String> = dataStore.data.map {preferences -> preferences[USER_FCM_TOKEN] ?: ""  }
    val loggedIn : Flow<Boolean> = dataStore.data.map { preferences -> preferences[USER_LOGGED_IN] ?: false }



    suspend fun onlineLastSeen(isOnline: Boolean , lastSeen: Long = System.currentTimeMillis()){
        dataStore.edit { preferences ->
            preferences[USER_IS_ONLINE_KEY] = isOnline
            preferences[USER_LAST_SEEN_KEY] = lastSeen.toString()
        }
    }

    suspend fun updateProfile(imageUrl : String){
        dataStore.edit { preferences ->
            preferences[USER_PROFILE_KEY] = imageUrl
        }
    }



    suspend fun saveUser(
         uid: String ,
         name: String ,
         phone: String ,
         email: String ,
         profile: String ,
         isOnline: Boolean ,
         lastSeen: Long = System.currentTimeMillis(),
         fcmToken: String = "",
    ){
        dataStore.edit { preferences ->
            preferences[USER_UID_KEY] = uid                          // isme mene = ki place pr to lagara ya joki wrong tha
            preferences[USER_NAME_KEY] = name
            preferences[USER_PHONE_KEY] = phone
            preferences[USER_EMAIL_KEY] = email
            preferences[USER_PROFILE_KEY] = profile
            preferences[USER_IS_ONLINE_KEY] = isOnline
            preferences[USER_LAST_SEEN_KEY] = lastSeen.toString()
            preferences[USER_FCM_TOKEN] = fcmToken
            preferences[USER_LOGGED_IN] = true                               // manually because this is not in the UserModel
        }
    }

    suspend fun updateUser(
        uid: String,
        phone: String,
        name: String
    ){
        dataStore.edit { preferences ->
            preferences[USER_UID_KEY] = uid
            preferences[USER_PHONE_KEY] = phone
            preferences[USER_NAME_KEY] = name
        }
    }


//    suspend fun deleteUid(){
//        dataStore.edit {
//            preferences ->
//            preferences.remove(USER_UID_KEY)
//        }
//    }

    suspend fun deleteUser(){
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }


//    suspend fun setUid(uid : String){
//       dataStore.edit { preference ->
//           preference[USER_UID_KEY] = uid
//       }
//    }



}