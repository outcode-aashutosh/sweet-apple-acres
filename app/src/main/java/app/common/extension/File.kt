package app.common.extension

import android.content.Context
import java.io.File

fun Context.getCacheDirectory(): String {
    return filesDir.path + File.separator + "cache"
}