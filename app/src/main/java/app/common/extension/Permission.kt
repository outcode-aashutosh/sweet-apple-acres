/*
@file:JvmName("PermissionKt")

package app.common.extension

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.outcode.esgonsite.R

fun AppCompatActivity.askRequiredPermission(
    permission: String,
    dialogTitle: String? = "Permissions Required!",
    dialogMessage: String? = "These permissions are required to operate the application properly. Please allow.",
    successBlock: () -> Unit
) {
    Dexter.withContext(this)
        .withPermission(permission)
        .withListener(object : PermissionListener {
            override fun onPermissionGranted(response: PermissionGrantedResponse) {
            successBlock.invoke()
            }

            override fun onPermissionDenied(response: PermissionDeniedResponse) {
                AlertDialog.Builder(this)
                    .setMessage(dialogMessage)
                    .setPositiveButton("Ask Again") { _, _ ->
                        Dexter.withContext(th)
                    }
                    .setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }

            override fun onPermissionRationaleShouldBeShown(
                permission: PermissionRequest?,
                token: PermissionToken?
            ) {
                this@askRequiredPermission.showConfirmationDialog(message = getString(R.string.permission_denied_message),
                    onConfirm = {
                        val i = Intent()
                        i.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        i.addCategory(Intent.CATEGORY_DEFAULT)
                        i.data = Uri.parse("package:" + this@askRequiredPermission.packageName)
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
                        this@askRequiredPermission.startActivity(i)
                    },
                    onCancel = {
                    })
            }
        }).check()
}
*/

/*  askPermission(*permissions) { successBlock() }.onDeclined { e ->
        if (e.hasDenied()) {
            // the list of denied permissions
            e.denied.forEach {
                logger("Denied: $it")
            }

            AlertDialog.Builder(this)
                .setMessage(dialogMessage)
                .setPositiveButton("Ask Again") { _, _ ->
                    e.askAgain()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }

        if (e.hasForeverDenied()) {
            AlertDialog.Builder(this)
                .setTitle(dialogTitle)
                .setMessage(dialogMessage)
                .setPositiveButton(R.string.ok_) { _, _ ->
                    e.goToSettings()
                }
                .setNegativeButton(R.string.cancel, null)
                .setCancelable(false)
                .show()
            // you need to open setting manually if you really need it

        }
    }*/
