package app.common.extension

import android.content.Context
import android.content.Intent

fun Context.shareContent(text: String) {
    val intent = Intent(Intent.ACTION_SEND)
    intent.type = "text/plain"
    intent.putExtra(Intent.EXTRA_TEXT, text)
    startActivity(
        Intent.createChooser(
            intent,
            "Share using"
        )
    )
}