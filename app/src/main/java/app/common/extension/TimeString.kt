package app.common.extension

import java.util.*
import java.util.concurrent.TimeUnit

fun timeString(millisUntilFinished: Long): String {
        val time = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)
        val minute = time * 1000 / (60 * 1000)
        val sec = (time * 1000 / 1000) % 60
        return String.format(Locale.getDefault(), "%02d ", minute, sec)
    }