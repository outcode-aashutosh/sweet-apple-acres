package app.common.extension

import androidx.appcompat.app.AppCompatDelegate

/**
 * Created by Ayush Shrestha on 8/31/21.
 */
fun changeToDarkTheme(){
    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
}

fun changeToLightTheme(){
    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
}