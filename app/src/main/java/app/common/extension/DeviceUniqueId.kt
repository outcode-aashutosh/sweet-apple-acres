package app.common.extension

import android.content.Context
import android.provider.Settings

fun Context.uniqueId(): String {
    return Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
}