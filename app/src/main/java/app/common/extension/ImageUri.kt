package app.common.extension

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.core.net.toUri
import java.io.OutputStream


fun Context.getImageUri(bitmap: Bitmap): Uri? {
    val values = ContentValues()
    values.put(MediaStore.Images.Media.TITLE, "image")
    values.put(MediaStore.Images.Media.DISPLAY_NAME, System.currentTimeMillis())
    values.put(MediaStore.Images.Media.DESCRIPTION, "banner logo")
    values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
    // Add the date meta data to ensure the image is added at the front of the gallery
    values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
    values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
    var url: Uri? = "null".toUri()
    var stringUri: Uri? = null /* value to be returned */
    try {
        url = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        if (bitmap != null) {
            val imageOut: OutputStream? = contentResolver.openOutputStream(url!!)
            imageOut.use { imageOut ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 1000, imageOut)
            }
        } else {
            contentResolver.delete(url!!, null, null)
            url = null
        }
    } catch (e: Exception) {
        if (url != null) {
            contentResolver.delete(url, null, null)
            url = null
        }
    }
    if (url != null) {
        stringUri = url
    }
    return stringUri
}

fun Context.getRealPathFromURI(bitmap: Bitmap): String? {
    var path = ""
    val uri = getImageUri(bitmap)
    if (contentResolver != null) {
        val cursor: Cursor? = uri?.let { contentResolver.query(it, null, null, null, null) }
        if (cursor != null) {
            cursor.moveToFirst()
            val idx: Int = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            path = cursor.getString(idx)
            cursor.close()
        }
    }
    return path
}
