@file:Suppress("UNUSED_VARIABLE", "LocalVariableName")

package app.common.extension

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.text.InputType
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import androidx.core.content.res.ResourcesCompat
import com.outcode.sweetappleacres.R

@SuppressLint("ClickableViewAccessibility")
fun EditText.setPasswordToggleable(
    drawablePosition: Int = 2,
    visibilityOffIcon: Int = R.drawable.ic_visibility_off_24,
    visibilityOnIcon: Int = R.drawable.ic_visibility_24
) {

    // drawable positions
    val DRAWABLE_LEFT = 0
    val DRAWABLE_TOP = 1
    val DRAWABLE_RIGHT = 2
    val DRAWABLE_BOTTOM = 3

    var passwordShown = false
    setOnTouchListener(View.OnTouchListener { v, event ->

        if (event.action == MotionEvent.ACTION_UP) {
            if (event.rawX >= right - compoundDrawables[drawablePosition].bounds.width()) {
                if (passwordShown) {
                    passwordShown = false
                    inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                    setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        visibilityOffIcon,
                        0
                    )
                    setSelection(this.length())

                } else {
                    passwordShown = true
                    inputType = InputType.TYPE_CLASS_TEXT
                    setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        visibilityOnIcon,
                        0
                    )
                    setSelection(this.length())
                }
                return@OnTouchListener false
            }
        }
        false
    })
}

fun EditText.getEditTextString(): String {
    return text.toString().trim()
}