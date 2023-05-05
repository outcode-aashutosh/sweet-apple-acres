package app.common.utils

import FileUtils.getBitMapPath
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import app.common.extension.compatColor
import com.outcode.sweetappleacres.R
import java.io.File

fun getImageFromText(mContext: Context, text: String): File {
    val width = 500
    val height = 500
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    val paint = Paint()

    paint.color = mContext.compatColor(org.koin.android.R.color.material_blue_grey_800)
    val size = 500
    val radius = size / 2f
    paint.style = Paint.Style.FILL
    paint.strokeWidth = 8f
    canvas.drawCircle(size / 2f, size / 2f, radius, paint)

    paint.color = mContext.compatColor(R.color.white)
    paint.isAntiAlias = true
    paint.style = Paint.Style.FILL
    paint.textSize = 150f
    paint.textAlign = Paint.Align.CENTER
    canvas.drawText(text, size / 1.9f, size / 1.6f, paint)

    return getBitMapPath(mContext, bitmap)
}