package app.common.extension

import android.content.Context
import android.content.Intent
import android.net.Uri

fun openSocialMedia(context: Context, urls: String) {
    try {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(urls)))
    } catch (e: Exception) {
        context.toast("something went wrong!!")
        logger(e.message)
    }
}