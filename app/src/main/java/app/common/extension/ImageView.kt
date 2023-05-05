package app.common.extension

import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.outcode.sweetappleacres.R


private const val defaultPlaceHolder = R.drawable.blank
private const val defaultErrorImage =  R.drawable.blank

@JvmName("loadImage")
fun ImageView.load(
    file: String?,
    @DrawableRes placeHolder: Int = defaultPlaceHolder,
    @DrawableRes errorImage: Int = defaultErrorImage
) {
    val requestOptions = RequestOptions().placeholder(placeHolder).error(errorImage)
    Glide.with(context)
        .setDefaultRequestOptions(requestOptions)
        .load(file)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(this)
}

/*fun ImageView.loadWithProgress(
    file: String?,
    @DrawableRes placeHolder: Int = defaultPlaceHolder,
    @DrawableRes errorImage: Int = defaultErrorImage
) {
    val requestOptions = RequestOptions().placeholder(placeHolder).error(errorImage)

    val options: RequestOptions = RequestOptions()
        .centerCrop()
        .placeholder(R.drawable.progress_animation)
        .error(errorImage)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .priority(Priority.HIGH)
        .dontAnimate()
        .dontTransform()
    Glide.with(context)
        .setDefaultRequestOptions(options)
        .load(file)
        .into(this)
}*/

fun ImageView.loadResizedFile(
    file: String?,
    BANNER_HEIGHT: Int,
    BANNER_WIDTH: Int
) {
    val requestOptions = RequestOptions().placeholder(defaultPlaceHolder).error(defaultPlaceHolder)
    Glide.with(context)
        .setDefaultRequestOptions(requestOptions)
        .load(file)
        .into(this)
}

fun ImageView.loadUri(
    uri: Uri?,
    @DrawableRes placeHolder: Int = defaultPlaceHolder,
    @DrawableRes errorImage: Int = defaultErrorImage
) {
    val requestOptions = RequestOptions().placeholder(placeHolder).error(errorImage)
    Glide.with(context)
        .setDefaultRequestOptions(requestOptions)
        .load(uri)
        .into(this)
}
fun ImageView.loadThumbnail(
    uri: String?,
    @DrawableRes placeHolder: Int = defaultPlaceHolder,
    @DrawableRes errorImage: Int = defaultErrorImage
) {
    val requestOptions = RequestOptions().placeholder(placeHolder).error(errorImage)
    Glide.with(context)
        .setDefaultRequestOptions(requestOptions)
        .load(uri)
        .into(this)
}

fun ImageView.loadDrawable(
    drawable: Int,
    @DrawableRes placeHolder: Int = defaultPlaceHolder,
    @DrawableRes errorImage: Int = defaultErrorImage
) {
    val requestOptions = RequestOptions().placeholder(placeHolder).error(errorImage)
    Glide.with(context)
        .setDefaultRequestOptions(requestOptions)
        .load(drawable)
        .into(this)
}
fun ImageView.load(
    drawable: Bitmap,
    @DrawableRes placeHolder: Int = defaultPlaceHolder,
    @DrawableRes errorImage: Int = defaultErrorImage
) {
    val requestOptions = RequestOptions().placeholder(placeHolder).error(errorImage)
    Glide.with(context)
        .setDefaultRequestOptions(requestOptions)
        .load(drawable)
        .into(this)
}