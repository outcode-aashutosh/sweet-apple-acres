package app.common.extension
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

fun String.toRequestBody(): RequestBody =
    this.toRequestBody("multipart/form-data".toMediaTypeOrNull())


fun File.imageToMultiPart(partName: String, fileName: String): MultipartBody.Part {
    val requestFile = this.asRequestBody("image/*".toMediaTypeOrNull())
    return MultipartBody.Part.createFormData(partName, fileName, requestFile)
}
fun File.videoToMultiPart(partName: String, fileName: String): MultipartBody.Part {
    val requestFile = this.asRequestBody("video/*".toMediaTypeOrNull())
    return MultipartBody.Part.createFormData(partName, fileName, requestFile)
}
fun File.fileToMultiPart(partName: String, fileName: String): MultipartBody.Part {
    val requestFile = this.asRequestBody("file/*".toMediaTypeOrNull())
    return MultipartBody.Part.createFormData(partName, fileName, requestFile)
}