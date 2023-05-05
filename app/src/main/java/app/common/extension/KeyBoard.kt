package app.common.extension

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.content.getSystemService

fun View.showSoftKeyboard() {
    val imm = context.getSystemService<InputMethodManager>()
    imm?.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}
fun View.hideSofteyBoard() {
    val imm = context.getSystemService<InputMethodManager>()
    imm?.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}
fun EditText.hideKeyboard() {
    val imm = context.getSystemService<InputMethodManager>()
    imm?.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

fun Activity.showKeyboard() {
    val imm = getSystemService<InputMethodManager>()
    imm?.showSoftInput(currentFocus ?: View(this), InputMethodManager.SHOW_IMPLICIT)
}

fun Activity.hideKeyboard() {
    // If no view currently has focus, createService a new one, just so we can grab a window token from it
    val imm = getSystemService<InputMethodManager>()
    val view = currentFocus ?: View(this)
    imm?.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}