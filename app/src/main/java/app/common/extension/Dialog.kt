package app.common.extension

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.esgonsite.dialogbox.ConfigModel
import com.esgonsite.dialogbox.OptionModel
import com.esgonsite.dialogbox.showCustomDialog
import com.outcode.sweetappleacres.R


fun Context.showAlert(
    title: String? = getString(R.string.app_name),
    message: String,
    positiveText: String = "Ok",
) {
    if (!IS_DIALOG_SHOWING) {
        IS_DIALOG_SHOWING = true
        showCustomDialog(
            this, configModel = ConfigModel(
                messageStyle = R.style.TextStyle_Regular, options = listOf(
                    OptionModel(title = positiveText,
                        backgroundDrawable = R.drawable.bg_rounded_primary,
                        color = R.color.white,
                        onItemClick = {
                            IS_DIALOG_SHOWING = false
                        }),
                ),
                isOneLineButton = true,
                message = message,
                title = title, showTitle = true
            )
        )
    }
}

fun Context.showMaintenanceAlert(
    title: String? = getString(R.string.app_name),
    message: String? = "This feature is under maintenance",
    positiveText: String = "Ok",
) {
    AlertDialog.Builder(this)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(positiveText, null)
        .show()
}

fun Activity.showMaintenanceAlert(
    title: String? = getString(R.string.app_name),
    message: String? = "This feature is under maintenance",
    positiveText: String = "Ok",
) {
    AlertDialog.Builder(this)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(positiveText, null)
        .show()
}

fun Context.showPositiveAlert(
    title: String? = getString(R.string.app_name),
    message: String,
    positiveText: String = "Ok",
    onConfirm: () -> Unit,
) {
    showCustomDialog(
        this, configModel = ConfigModel(
            messageStyle = R.style.TextStyle_Regular, options = listOf(
                OptionModel(title = positiveText,
                    backgroundDrawable = R.drawable.bg_rounded_primary,
                    color = R.color.white,
                    onItemClick = {
                        onConfirm.invoke()
                    }),
            ),
            isOneLineButton = true,
            message = message,
            title = title
        )
    )
}

fun Context.showConfirmationDialog(
    title: String = getString(R.string.app_name),
    message: String,
    positiveText: String = "Ok",
    negativeText: String = "Cancel",
    textColor: Int? = R.color.white,
    positiveDrawable: Int? = R.drawable.bg_rounded_primary,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
) {

    showCustomDialog(
        this, configModel = ConfigModel(
            messageStyle = R.style.TextStyle_Regular, options = listOf(
                OptionModel(title = positiveText,
                    backgroundDrawable = positiveDrawable,
                    color = textColor,
                    onItemClick = {
                        onConfirm()
                    }),
                OptionModel(title = negativeText,
                    backgroundDrawable = R.drawable.bg_rounded_primary,
                    color = textColor,
                    onItemClick = {
                        onCancel()
                    }),
            ),
            isOneLineButton = true,
            message = message,
            title = title
        )
    )


}

fun Context.showOptionsDialog(
    title: String,
    vararg options: String,
    listener: (dialog: DialogInterface, which: Int) -> Unit,
) {
    AlertDialog.Builder(this)
        .setTitle(title)
        .setItems(options) { dialog, which ->
            listener.invoke(dialog, which)
        }
        .show()
}

fun Fragment.showAlert(message: String) = context?.showAlert(message = message)

fun Context.optionDialog(
    title: String? = getString(R.string.app_name),
    message: String?,
    positiveText: String? = "Ok",
    negativeText: String = "Cancel",
    positiveAction: () -> Unit,
    negativeAction: () -> Unit,
) {
    AlertDialog.Builder(this)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(positiveText) { dialog, _ ->
            positiveAction()
            dialog.dismiss()
        }
        .setNegativeButton(negativeText) { dialog, _ ->
            negativeAction()
            dialog.dismiss()
        }
        .show()
}

/*
fun Context.showConfirmationDialog(
    title: String? = getString(R.string.app_name),
    message: String?,
    positiveText: String? = "Ok",
    positiveAction: () -> Unit
) {
    AlertDialog.Builder(this)
        .setTitle(title)
        .setMessage(message)
        .setCancelable(false)
        .setPositiveButton(positiveText) { dialog, _ ->
            positiveAction()
            dialog.dismiss()
        }.show()
}
*/

fun Context.messageOnlyDialog(
    message: String?,
    positiveText: String? = "Ok",
    negativeText: String? = "Cancel",
    positiveAction: () -> Unit,
) {
    AlertDialog.Builder(this)
        .setMessage(message)
        .setPositiveButton(positiveText) { dialog, _ ->
            positiveAction()
            dialog.dismiss()
        }
        .setNegativeButton(negativeText, null)
        .show()
}

fun Context.neutralDialog(
    title: String? = getString(R.string.app_name),
    message: String?,
    positiveText: String? = "Ok",
    negativeText: String? = "Cancel",
    neutralText: String,
    positiveAction: () -> Unit,
    negativeAction: () -> Unit,
    neutralAction: () -> Unit,
) {
    AlertDialog.Builder(this)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(positiveText) { dialog, _ ->
            positiveAction()
            dialog.dismiss()
        }
        .setNeutralButton(negativeText) { dialog, _ ->
            negativeAction()
            dialog.dismiss()
        }
        .setNegativeButton(neutralText) { dialog, _ ->
            neutralAction()
            dialog.dismiss()
        }
        .show()
}



