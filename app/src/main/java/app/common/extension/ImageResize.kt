package app.common.extension

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.core.net.toUri
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

/*
fun Context.resizeImage(context: Context,filePath: String, requiredHeight: Int, requiredWidth: Int): String {
    val options = BitmapFactory.Options()
    val inputStream: InputStream? = filePath.let { contentResolver.openInputStream(it.toUri()) }
    val bm = BitmapFactory.decodeStream(inputStream, null, options)
    val height: Int? = bm?.height
    val width: Int? = bm?.width
    val scaleWidth = requiredWidth.toFloat() / width!!
    val scaleHeight = requiredHeight.toFloat() / height!!
    val matrix = Matrix()
    matrix.postScale(scaleWidth, scaleHeight)
    val resizedBitmap = bm.let { Bitmap.createBitmap(it, 0, 0, width, height, matrix, true) }


    var compressQuality = 100 // quality decreasing by 5 every loop.
    val resizedFileName = uniqueImageName

    val bmpStream = ByteArrayOutputStream()
    resizedBitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
    val bmpPicByteArray = bmpStream.toByteArray()
    streamLength = bmpPicByteArray.size


    try {
        //save the resized and compressed file to disk cache
        val bmpFile = FileOutputStream(context.cacheDir.toString() + resizedFileName)

        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpFile)
        bmpFile.flush()
        bmpFile.close()
    } catch (e: Exception) {
        Log.e("compressBitmap", "Error on saving file")
        // return the original IMAGE if we fail to save the compressed IMAGE
        return filePath
    }

    //return the path of resized and compressed file
    return context.cacheDir.toString() + resizedFileName


}*/
