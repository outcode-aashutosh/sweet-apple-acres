package app.common.extension

import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputLayout

fun textChangeListener(editText: EditText, textInputLayout: TextInputLayout) {
    editText.addTextChangedListener {
        textInputLayout.isErrorEnabled = false
    }
}