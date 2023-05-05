package app.sweetappleacres.data

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson


class Prefs(private val sharedPreferences: SharedPreferences) {

    companion object {
        private const val AUTH_TOKEN = "AUTH_TOKEN"
        private const val REFRESH_TOKEN = "REFRESH_TOKEN"
        private const val USER_NAME = "USER_NAME"
        private const val LOGIN_RESPONSE = "LOGIN_RESPONSE"
        private const val KEY_IS_LOGGED_IN = "KEY_IS_LOGGED_IN"
        private const val KEY_IS_CONNECTED = "KEY_IS_CONNECTED"
    }

    val gson = Gson()

    var authToken: String
        get() = sharedPreferences.getString(AUTH_TOKEN, "") ?: "NoAuthentication"
        set(value) = sharedPreferences.edit {
            putString(AUTH_TOKEN, "token $value")
        }

    var refreshToken: String
        get() = sharedPreferences.getString(REFRESH_TOKEN, "") ?: "NoAuthentication"
        set(value) = sharedPreferences.edit {
            putString(REFRESH_TOKEN, value)
        }

    var isLogin: Boolean
        get() = sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
        set(value) = sharedPreferences.edit {
            putBoolean(KEY_IS_LOGGED_IN, value)
        }

    var isConnected: Boolean
        get() = sharedPreferences.getBoolean(KEY_IS_CONNECTED, false)
        set(value) = sharedPreferences.edit {
            putBoolean(KEY_IS_CONNECTED, value)
        }

    fun clearPrefs() = sharedPreferences.edit().clear().commit()


}