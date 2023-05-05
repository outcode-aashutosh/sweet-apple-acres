package app.common.extension

import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible

fun View.gone() {
    isGone = true
}

fun View.visible() {
    isVisible = true
}

fun View.invisible() {
    isInvisible = true
}

fun View.clickable() {
    isClickable = true
    isFocusable = true
    isEnabled = true
    alpha = 1f
}

fun View.unClickable() {
    isClickable = false
    isFocusable = false
    isEnabled = false
    alpha = 0.5f

}
