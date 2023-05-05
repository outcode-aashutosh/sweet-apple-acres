package app.common.extension

import android.app.Activity
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat


fun setStatusBarColor(color: Int, activity: Activity) {
    val window: Window = activity.window
    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    window.statusBarColor = ContextCompat.getColor(activity, color)
}
