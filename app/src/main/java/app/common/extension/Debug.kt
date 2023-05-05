package app.common.extension

import android.content.Context
import androidx.fragment.app.Fragment
import com.outcode.sweetappleacres.BuildConfig

val developerTest: Boolean = BuildConfig.DEBUG

inline fun debugOnly(func: () -> Unit) {
    if (BuildConfig.DEBUG) {
        func()
    }
}

inline fun developerTestOnly(func: () -> Unit) {
    if (developerTest) {
        func()
    }
}

fun Context.toastDevOnly(message: String) = developerTestOnly { toast(message) }
fun Fragment.toastDevOnly(message: String) = context?.toastDevOnly(message)
