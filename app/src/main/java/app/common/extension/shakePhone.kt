package app.common.extension

import android.content.Context
import android.content.Context.VIBRATOR_SERVICE
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator

fun Context.shakePhone(){
    val DURATION = 500 // you can change this according to your need

    if (Build.VERSION.SDK_INT >= 26) {
        (getSystemService(VIBRATOR_SERVICE) as Vibrator?)!!.vibrate(
            VibrationEffect.createOneShot(
            DURATION.toLong(),
            VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        (getSystemService(VIBRATOR_SERVICE) as Vibrator?)!!.vibrate(DURATION.toLong())
    }
}