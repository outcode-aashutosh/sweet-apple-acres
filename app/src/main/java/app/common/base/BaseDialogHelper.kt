package app.common.base

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.appcompat.app.AlertDialog

/**
 * Reference : https://android.jlelse.eu/android-custom-alert-dialogs-kotlin-extension-functions-kotlin-higher-order-functions-life-682305c5322e
 * This is used as an Base for creating custom dailogs.
 */
abstract class BaseDialogHelper {

    abstract val dialogView: View
    abstract val builder: AlertDialog.Builder

    open var cancelable: Boolean = true
    open var title: String = ""
    open var showTitle: Boolean = false
    open var isBackGroundTransparent: Boolean = true

    open var dialog: AlertDialog? = null

    open fun create(): AlertDialog {
        dialog = builder
                .setCancelable(cancelable)
                .create()

        if(showTitle)
            dialog?.setTitle(title)

        if (isBackGroundTransparent)
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return dialog!!
    }

    open fun onCancelListener(func: () -> Unit): AlertDialog.Builder? =
            builder.setOnCancelListener {
                func()
            }
}