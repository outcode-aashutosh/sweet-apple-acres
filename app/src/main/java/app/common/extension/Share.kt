package app.common.extension

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import java.io.File

fun Context.share(file: File) {
    val authority = "${this.packageName}.fileprovider"
    val contentUri = FileProvider.getUriForFile(this, authority, file)

    val shareIntent = Intent(Intent.ACTION_SEND)
    shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri)
    shareIntent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
    shareIntent.flags = Intent.FLAG_GRANT_WRITE_URI_PERMISSION
    shareIntent.type = "*/*"

    try {
        startActivity(Intent.createChooser(shareIntent, "share.."))
    } catch (e: ActivityNotFoundException) {
       toast("Could not share this file.")
    }
}