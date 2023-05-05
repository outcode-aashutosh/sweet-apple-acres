import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.res.XmlResourceParser
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.media.ExifInterface
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.core.content.ContextCompat.getDrawable
import androidx.documentfile.provider.DocumentFile
import app.common.extension.loggerE
import timber.log.Timber
import java.io.*
import java.nio.channels.FileChannel
import java.text.DecimalFormat
import java.util.*
import kotlin.jvm.Throws

@SuppressLint("LogNotTimber")
object FileUtils {

    /**
     * TAG for log messages.
     */
    internal const val TAG = "FileUtils"
    private const val DEBUG = false // Set to true to enable logging
    const val APP_NAME = "Jipe"

    const val MIME_TYPE_IMAGE = "IMAGE/*"

    const val HIDDEN_PREFIX = "."

    private const val IMAGE_PREFIX = "img_"
    private const val IMAGE_POSTFIX = ".jpg"
    private const val DOCUMENTS_DIR: String = "document"

    /**
     * Get the IMAGE directory to store photo taken in your app
     *
     * @return the IMAGE directory to save photo to
     */
    val imageDirectory: File?
        get() {
            val filePath = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                File.separator + APP_NAME
            )

            if (!filePath.exists()) {
                if (!(filePath.mkdirs() || filePath.isDirectory)) {
                    return null
                }
            }
            return filePath
        }

    /**
     * Get unique filename for photo.
     *
     * @return the unique filename for the photo
     */
    val uniqueImageName: String
        get() {
            return try {
                File.createTempFile(IMAGE_PREFIX, IMAGE_POSTFIX).name
            } catch (e: IOException) {
                IMAGE_PREFIX + System.currentTimeMillis() + IMAGE_POSTFIX
            }
        }

    /**
     * Get unique filename for documents.
     *
     * @return the unique filename for the document
     */
    val uniqueDocumentName: String
        get() {
            try {
                return File.createTempFile("doc_", ".pdf").name
            } catch (e: IOException) {
                return "doc_" + System.currentTimeMillis() + ".pdf"
            }

        }

    /**
     * File and folder comparator.
     *  @author paulburke
     */
    var sComparator: Comparator<File> = Comparator { f1, f2 ->
        // Sort alphabetically by lower case, which is much cleaner
        f1.name.toLowerCase().compareTo(
            f2.name.toLowerCase()
        )
    }

    /**
     * File (not directories) filter.
     *
     * @author paulburke
     */
    var sFileFilter: FileFilter = FileFilter { file ->
        val fileName = file.name
        // Return files only (not directories) and skip hidden files
        file.isFile && !fileName.startsWith(HIDDEN_PREFIX)
    }

    /**
     * Folder (directories) filter.
     *
     * @author paulburke
     */
    var sDirFilter: FileFilter = FileFilter { file ->
        val fileName = file.name
        // Return directories only and skip hidden directories
        file.isDirectory && !fileName.startsWith(HIDDEN_PREFIX)
    }

    /**
     * Gets the extension of a file, like ".png" or ".jpg".
     *
     * @param filePath
     * @return
     * -- Extension including the dot(".");
     * -- "" if there is no extension;
     * -- null if filePath was null.
     */
    private fun getExtension(filePath: String?): String? {
        if (filePath == null) {
            return null
        }

        val dot = filePath.lastIndexOf(".")
        return if (dot >= 0) {
            filePath.substring(dot)
        } else {
            // No extension.
            ""
        }
    }

    /**
     * @return Whether the URI is a local one.
     */
    fun isLocal(url: String?): Boolean {
        return url != null && !url.startsWith("http://") && !url.startsWith("https://")
    }

    fun removeLinkPrefix(url: String): String {
        return when {
            url.startsWith("http://") -> url.removePrefix("http://")
                .toUpperCase(Locale.getDefault())
            url.startsWith("https://") -> url.removePrefix("https://")
                .toUpperCase(Locale.getDefault())
            else -> url.toUpperCase(Locale.getDefault())
        }
    }

    /**
     * @return True if Uri is a MediaStore Uri.
     * @author paulburke
     */
    fun isMediaUri(uri: Uri): Boolean {
        return "media".equals(uri.authority, ignoreCase = true)
    }

    /**
     * Convert File into Uri.
     *
     * @param file
     * @return uri
     */
    fun getUri(file: File?): Uri? {
        return if (file != null) {
            Uri.fromFile(file)
        } else null
    }

    /**
     * Returns the path only (without file name).
     *
     * @param file
     * @return
     */
    fun getPathWithoutFilename(file: File?): File? {
        if (file != null) {
            if (file.isDirectory) {
                // no file to be split off. Return everything
                return file
            } else {
                val filename = file.name
                val filepath = file.absolutePath

                // Construct path without file name.
                var pathwithoutname = filepath.substring(
                    0,
                    filepath.length - filename.length
                )
                if (pathwithoutname.endsWith("/")) {
                    pathwithoutname = pathwithoutname.substring(0, pathwithoutname.length - 1)
                }
                return File(pathwithoutname)
            }
        }
        return null
    }

    /**
     * @return The MIME type for the given file.
     */
    fun getMimeType(file: File): String? {
        val extension = getExtension(file.name)
        return if (extension!!.isNotEmpty()) MimeTypeMap.getSingleton().getMimeTypeFromExtension(
            extension.substring(1)
        ) else "application/octet-stream"
    }

    /**
     * @return The MIME type for the give Uri.
     */
    fun getMimeType(context: Context, uri: Uri): String? {
        val file = File(getPath(context, uri)!!)
        return getMimeType(file)
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     * @author paulburke
     */
    fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     * @author paulburke
     */
    fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     * @author paulburke
     */
    fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Document.
     */
    fun isGoogleDocumentUri(uri: Uri): Boolean {
        //content://com.google.android.apps.docs.storage/document/acc%3D1%3Bdoc%3D2936
        return "com.google.android.apps.docs.storage" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority || "com.google.android.apps.photos.contentprovider" == uri.authority
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     * @author paulburke
     */
    fun getDataColumn(
        context: Context, uri: Uri, selection: String?,
        selectionArgs: Array<String>?,
    ): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)
        try {
            cursor = context.contentResolver.query(
                uri,
                projection,
                selection,
                selectionArgs,
                null
            )
            if (cursor != null && cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(columnIndex)
            }
        } finally {
            cursor?.close()
        }
        return null
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.<br></br>
     * <br></br>
     * Callers should check whether the path is local before assuming it
     * represents a local file.
     *
     * @param context The context.
     * @param uri     The Uri to query.
     * @author paulburke
     * @see .isLocal
     * @see .getFile
     */
    @SuppressLint("NewApi")
    fun getPath(context: Context, uri: Uri): String? {
        // DocumentProvider
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]

                if ("primary".equals(type, ignoreCase = true)) {
                    return context.externalCacheDir.toString() + "/" + split[1]
                }
            } else if (isDownloadsDocument(uri)) {
                return try {
                    val fileName = getFileName(context, uri)
                    if (fileName != null) {
                        return context.externalCacheDir.toString() + "/Download/" + fileName
                    }
                    val id = DocumentsContract.getDocumentId(uri)
                    //Parse uri with raw like:
                    //content://com.android.providers.downloads.documents/document/raw%3A%2Fstorage%2Femulated%2F0%2FDownload%2Fgochat-V1%20copy-UI%20KIT.pdf
                    if (id != null && id.startsWith("raw:")) {
                        return id.substring(4)
                    }
                    val contentUriPrefixesToTry = arrayOf(
                        "content://downloads/public_downloads",
                        "content://downloads/my_downloads",
                        "content://downloads/all_downloads",
                        "content://com.android.providers.media.documents/document",
                        "content://com.android.providers.media.documents/document"
                    )

                    for (contentUriPrefix in contentUriPrefixesToTry) {
                        val contentUri = ContentUris.withAppendedId(
                            Uri.parse(contentUriPrefix),
                            java.lang.Long.valueOf(id!!)
                        )
                        try {
                            val path = getDataColumn(context, contentUri, null, null)
                            if (path != null) {
                                return path
                            }
                        } catch (e: Exception) {
                            Timber.e(e)
                        }
                    }
                    // path could not be retrieved using ContentResolver,
                    // therefore copy file to accessible cache using streams
                    var cacheFileName = getFileName(context, uri)
                    if (cacheFileName == null) {
                        cacheFileName = File(uri.path).name
                    }
                    val cacheDir = getDocumentCacheDir(context)
                    val cacheFile = generateFileName(cacheFileName, cacheDir)
                    var destinationPath: String? = null
                    if (cacheFile != null) {
                        destinationPath = cacheFile.absolutePath
                        saveFileFromUri(context, uri, destinationPath)
                    }
                    return destinationPath
                } catch (e: NumberFormatException) {
                    Timber.e(e)
                    null
                }
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]

                var contentUri: Uri? = null
                when (type) {
                    "IMAGE" -> contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    "VIDEO" -> contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    "AUDIO" -> contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }

                val selection = "_id=?"
                val selectionArgs = arrayOf(split[1])
                return getDataColumn(context, contentUri!!, selection, selectionArgs)
            } else if (isGoogleDocumentUri(uri)) {
                return downloadFileFromUri(context, uri)
            }
        } else if ("content".equals(uri.scheme, ignoreCase = true)) {
            return if (isGooglePhotosUri(uri)) {
                downloadFileFromUri(context, uri)
            } else {
                getDataColumn(context, uri, null, null)
            }
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }
        return null
    }

    fun getFileName(context: Context, uri: Uri): String? {
        var cursor: Cursor? = null
        val projection = arrayOf(MediaStore.MediaColumns.DISPLAY_NAME)

        try {
            cursor = context.contentResolver.query(uri, projection, null, null, null)
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)
                return cursor.getString(index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }

    /**
     * Download file from [uri] and store in documents directory
     * @param context The context
     * @param uri     Uri of google doc
     * @return path of the new file from uri
     */
    private fun downloadFileFromUri(context: Context, uri: Uri): String {
        val documentFile = DocumentFile.fromSingleUri(context, uri)
        val file =
            File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), documentFile?.name)
        val maxBufferSize = 1 * 1024 * 1024
        try {
            val inputStream = context.contentResolver.openInputStream(uri)
            Timber.d("InputStream Size %s", inputStream!!)
            val bytesAvailable = inputStream.available()
            //                    int bufferSize = 1024;
            val bufferSize = Math.min(bytesAvailable, maxBufferSize)
            val buffers = ByteArray(bufferSize)

            val outputStream = FileOutputStream(file)
            var read = 0
            while ({ read = inputStream.read(buffers);read }() != -1) {
                outputStream.write(buffers, 0, read)
            }
            Timber.e("File Size ${file.length()}")
            inputStream.close()
            outputStream.close()
            Timber.d("File Path ${file.path}")
            Timber.d("File Size ${file.length()}")
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file.path
    }

    /**
     * Convert Uri into File, if possible.
     *
     * @return file A local file that the Uri was pointing to, or null if the
     * Uri is unsupported or pointed to a remote resource.
     * @author paulburke
     * @see .getPath
     */
    fun getFile(context: Context, uri: Uri?): File? {
        if (uri != null) {
            val path = getPath(context, uri)
            if (path != null && isLocal(path)) {
                return File(path)
            }
        }
        return null
    }

    /**
     * Get the file size in a human-readable string.
     *
     * @param size
     * @return
     * @author paulburke
     */
    fun getReadableFileSize(size: Int): String {
        val BYTES_IN_KILOBYTES = 1024
        val dec = DecimalFormat("#.##")
        val KILOBYTES = " KB"
        val MEGABYTES = " MB"
        val GIGABYTES = " GB"
        var fileSize = 0f
        var suffix = KILOBYTES

        if (size > BYTES_IN_KILOBYTES) {
            fileSize = (size / BYTES_IN_KILOBYTES).toFloat()
            if (fileSize > BYTES_IN_KILOBYTES) {
                fileSize = fileSize.div(BYTES_IN_KILOBYTES)
                if (fileSize > BYTES_IN_KILOBYTES) {
                    fileSize /= BYTES_IN_KILOBYTES
                    suffix = GIGABYTES
                } else {
                    suffix = MEGABYTES
                }
            }
        }
        return dec.format(fileSize.toDouble()) + suffix
    }

    fun getFileSize(file: File): String {

        val format = DecimalFormat("#.##")
        val mb = (1024 * 1024).toLong()
        val kb: Long = 1024

        require(file.isFile) { "Expected a file" }
        val length = file.length().toDouble()

        if (length > mb) {
            return format.format(length / mb) + " MB"
        }
        return if (length > kb) {
            format.format(length / kb) + " KB"
        } else format.format(length) + " B"
    }

    /**
     * Attempt to retrieve the thumbnail of given File from the MediaStore. This
     * should not be called on the UI thread.
     *
     * @param context
     * @param file
     * @return
     * @author paulburke
     */
    fun getThumbnail(context: Context, file: File): Bitmap? {
        return getThumbnail(context, getUri(file), getMimeType(file))
    }

    /**
     * Attempt to retrieve the thumbnail of given Uri from the MediaStore. This
     * should not be called on the UI thread.
     *
     * @param context
     * @param uri
     * @param mimeType
     * @return
     * @author paulburke
     */
    @JvmOverloads
    fun getThumbnail(
        context: Context,
        uri: Uri?,
        mimeType: String? = getMimeType(context, uri!!),
    ): Bitmap? {
        if (DEBUG)
            Log.d(TAG, "Attempting to get thumbnail")

        if (!isMediaUri(uri!!)) {
            Log.e(TAG, "You can only retrieve thumbnails for images and videos.")
            return null
        }

        var bm: Bitmap? = null
        val resolver = context.contentResolver
        var cursor: Cursor? = null
        try {
            cursor = resolver.query(uri, null, null, null, null)
            if (cursor!!.moveToFirst()) {
                val id = cursor.getInt(0)
                if (DEBUG)
                    Log.d(TAG, "Got thumb ID: $id")

                if (mimeType!!.contains("VIDEO")) {
                    bm = MediaStore.Video.Thumbnails.getThumbnail(
                        resolver,
                        id.toLong(),
                        MediaStore.Video.Thumbnails.MINI_KIND, null
                    )
                } else if (mimeType.contains(MIME_TYPE_IMAGE)) {
                    bm = MediaStore.Images.Thumbnails.getThumbnail(
                        resolver,
                        id.toLong(),
                        MediaStore.Images.Thumbnails.MINI_KIND, null
                    )
                }
            }
        } catch (e: Exception) {
            if (DEBUG)
                Log.e(TAG, "getThumbnail", e)
        } finally {
            if (cursor != null)
                cursor.close()
        }
        return bm
    }

    /**
     * Get the Intent for selecting content to be used in an Intent Chooser.
     *
     * @return The intent for opening a file with Intent.createChooser()
     * @author paulburke
     */
    fun createGetContentIntent(): Intent {
        // Implicitly allow the user to select a particular kind of data
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        // The MIME data type filter
        intent.type = "*/*"
        // Only return URIs that can be opened with ContentResolver
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        return intent
    }

    /**
     * Resize the IMAGE to pre-defined size and compress to meet pre-defined file size
     *
     * @param context  context to use
     * @param filePath path of IMAGE to resize
     * @return resized IMAGE path
     */
    fun resizeAndCompressImageBeforeSend(
        context: Context,
        filePath: String,
        requiredHeight: Int,
        requiredWidth: Int,
    ): String {
        val MAX_IMAGE_SIZE = 600 * 1300 // max final file size in kilobytes
        val resizedFileName = uniqueImageName

        // First decode with inJustDecodeBounds=true to check dimensions of IMAGE
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(filePath, options)

        // Calculate inSampleSize(First we are going to resize the IMAGE to 800x800 IMAGE, in order to not have a big but very low quality IMAGE.
        //resizing the IMAGE will already reduce the file size, but after resizing we will check the file size and loadData to compress IMAGE
        options.inSampleSize = calculateInSampleSize(options, requiredWidth, requiredHeight)

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false
        options.inPreferredConfig = Bitmap.Config.ARGB_8888

        val bmpPic = BitmapFactory.decodeFile(filePath, options)

        var rotatedBitmap: Bitmap
        val exif: ExifInterface
        try {
            exif = ExifInterface(filePath)
            val orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION)
            val orientation =
                if (orientString != null) Integer.parseInt(orientString) else ExifInterface.ORIENTATION_NORMAL

            var rotationAngle = 0
            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90
            if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180
            if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270

            val matrix = Matrix()
            matrix.setRotate(
                rotationAngle.toFloat(),
                bmpPic.width.toFloat() / 2,
                bmpPic.height.toFloat() / 2
            )
            rotatedBitmap =
                Bitmap.createBitmap(bmpPic, 0, 0, options.outWidth, options.outHeight, matrix, true)
        } catch (e: IOException) {
            e.printStackTrace()
            rotatedBitmap = bmpPic
        }

        var compressQuality = 100 // quality decreasing by 5 every loop.
        var streamLength: Int
        do {
            val bmpStream = ByteArrayOutputStream()
            Log.d("compressBitmap", "Quality: $compressQuality")
            rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
            val bmpPicByteArray = bmpStream.toByteArray()
            streamLength = bmpPicByteArray.size
            compressQuality -= 5
            Log.d("compressBitmap", "Size: " + streamLength / 1024 + " kb")
        } while (streamLength >= MAX_IMAGE_SIZE)

        try {
            //save the resized and compressed file to disk cache
            Log.d("compressBitmap", "cacheDir: " + context.cacheDir)
            val bmpFile = FileOutputStream(context.cacheDir.toString() + resizedFileName)

            rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpFile)
            bmpFile.flush()
            bmpFile.close()
        } catch (e: Exception) {
            Log.e("compressBitmap", "Error on saving file")
            // return the original IMAGE if we fail to save the compressed IMAGE
            return filePath
        }

        //return the path of resized and compressed file
        return context.cacheDir.toString() + resizedFileName
    }

    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int,
    ): Int {
        val debugTag = "MemoryInformation"
        val height = options.outHeight
        val width = options.outWidth
        Log.d(debugTag, "IMAGE height: $height---IMAGE width: $width")
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {

            val halfHeight = height / 2
            val halfWidth = width / 2

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (halfHeight / inSampleSize > reqHeight && halfWidth / inSampleSize > reqWidth) {
                inSampleSize *= 2
            }
        }
        Log.d(debugTag, "inSampleSize: $inSampleSize")
        return inSampleSize
    }

    /**
     * @param createdImagePath = path of the file to be deleted
     */
    fun deleteCreatedFile(createdImagePath: String?) {
        if (createdImagePath != null && !createdImagePath.isEmpty()) {
            val file = File(createdImagePath)
            if (file.exists()) {
                val isDeleted = file.delete()
                Log.d("FileUtils", "is created file deleted= $isDeleted")
            }
        }
    }

    /**
     * @return cache dir
     */
    private fun getDocumentCacheDir(context: Context): File {
        val dir = File(context.cacheDir, DOCUMENTS_DIR)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        return dir
    }

    /**
     * Generate new file which does not exit in given [directory]
     */
    private fun generateFileName(name: String?, directory: File): File? {
        if (name == null) {
            return null
        }
        var file = File(directory, name)
        if (file.exists()) {
            var fileName = name
            var extension = ""
            val dotIndex = name.lastIndexOf('.')
            if (dotIndex > 0) {
                fileName = name.substring(0, dotIndex)
                extension = name.substring(dotIndex)
            }
            var index = 0
            while (file.exists()) {
                index++
                val newName = "$fileName($index)$extension"
                file = File(directory, newName)
            }
        }
        try {
            if (!file.createNewFile()) {
                return null
            }
        } catch (e: IOException) {
            return null
        }
        return file
    }

    /**
     * Method to save file to [destinationPath] from [uri]
     */
    private fun saveFileFromUri(context: Context, uri: Uri, destinationPath: String) {
        var inputStream: InputStream? = null
        var bos: BufferedOutputStream? = null
        try {
            inputStream = context.contentResolver.openInputStream(uri)
            bos = BufferedOutputStream(FileOutputStream(destinationPath, false))
            val buf = ByteArray(1024)
            inputStream!!.read(buf)
            do {
                bos.write(buf)
            } while (inputStream.read(buf) !== -1)
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                inputStream?.close()
                bos?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }


    fun copyGalleryImageToPrivateImage(
        context: Context,
        sourceFile: File,
        fileName: String,
    ): String {
        val destinationFile = getAlbumStorageDir(context, APP_NAME + System.currentTimeMillis())
        destinationFile.createNewFile()
        val output = File(destinationFile, fileName)
        copyFile(sourceFile, output)
        return output.absolutePath
    }

    @Throws(IOException::class)
    private fun copyFile(sourceFile: File, destFile: File) {
        if (!sourceFile.exists()) {
            return
        }
        val source: FileChannel? = FileInputStream(sourceFile).channel
        val destination: FileChannel? = FileOutputStream(destFile).channel
        if (destination != null && source != null) {
            destination.transferFrom(source, 0, source.size())
        }
        source?.close()
        destination?.close()
    }

    fun getAlbumStorageDir(context: Context, albumName: String): File {
        // Get the directory for the app's private pictures directory.
        val file = File(
            context.getExternalFilesDir(
                Environment.DIRECTORY_PICTURES
            ), APP_NAME
        )
        if (!file.mkdirs()) {
            Timber.e("Directory not created")
        }
        return file
    }

    /**
     * @return image path width and height
     */
    fun getImageWidthHeight(url: String): Pair<Int, Int> {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true

        BitmapFactory.decodeFile(url, options)
        val width = options.outWidth
        val height = options.outHeight

        return Pair(width, height)
    }

    private fun copy(mContext: Context, srcUri: Uri?, dstFile: File?) {
        try {
            val inputStream = mContext.contentResolver.openInputStream(srcUri!!) ?: return
            val outputStream: OutputStream = FileOutputStream(dstFile)
            copyStream(inputStream, outputStream)
            inputStream.close()
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Throws(Exception::class, IOException::class)
    fun copyStream(input: InputStream?, output: OutputStream?): Int {
        val buffer = ByteArray(32)
        val `in` = BufferedInputStream(input, 32)
        val out = BufferedOutputStream(output, 32)
        var count = 0
        var n = 0
        try {
            while (`in`.read(buffer, 0, 32).also { n = it } != -1) {
                out.write(buffer, 0, n)
                count += n
            }
            out.flush()
        } finally {
            try {
                out.close()
            } catch (e: IOException) {
                Log.e(e.message, java.lang.String.valueOf(e))
            }
            try {
                `in`.close()
            } catch (e: IOException) {
                Log.e(e.message, java.lang.String.valueOf(e))
            }
        }
        return count
    }

    fun Context.resizeImage(filePath: Uri, requiredHeight: Int, requiredWidth: Int): String {
        val options = BitmapFactory.Options()
        val inputStream: InputStream? = filePath.let { contentResolver.openInputStream(it) }
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

        try {
            //save the resized and compressed file to disk cache
            val bmpFile = FileOutputStream(cacheDir.toString() + resizedFileName)

            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpFile)
            bmpFile.flush()
            bmpFile.close()
        } catch (e: Exception) {
            Log.e("compressBitmap", "Error on saving file")
            // return the original IMAGE if we fail to save the compressed IMAGE
            return filePath.toString()
        }

        //return the path of resized and compressed file
        return cacheDir.toString() + resizedFileName


    }

    fun getDrawablePath(context: Context, drawable: Int): File {
        val drawable: BitmapDrawable =
            getDrawable(context, drawable) as BitmapDrawable
        val bitmap: Bitmap = drawable.bitmap

        val f: File = File(context.cacheDir, "filename")
        f.createNewFile()
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        val bitmapdata = bos.toByteArray()

        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(f)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        try {
            fos!!.write(bitmapdata)
            fos.flush()
            fos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return f
    }

    fun getBitMapPath(context: Context, bitmap: Bitmap): File {
        val root: String? = context.getExternalFilesDir(null)?.absolutePath
        val storageDir = File(root.toString(), "$uniqueImageName")

        storageDir.createNewFile()
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        val bitmapdata = bos.toByteArray()

        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(storageDir)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        try {
            fos!!.write(bitmapdata)
            fos.flush()
            fos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return storageDir
    }

    fun getFilePathFromURI(mContext: Context, contentUri: Uri?): String? {
        val root: String? = mContext.getExternalFilesDir(null)?.absolutePath
        val storageDir = File(root.toString(), "$uniqueImageName")

        // create folder if not exists
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(storageDir)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        try {
            val bitmap:Bitmap = MediaStore.Images.Media.getBitmap(mContext.contentResolver, contentUri)
            val bos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 15, bos);
            val bitmapData = bos.toByteArray()
            fos!!.write(bitmapData)
            fos.flush()
            fos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return storageDir.toString()
    }

     fun encodeImageFile(path: String): String? {
        val imagefile = File(path)
        var fis: FileInputStream? = null
        try {
            fis = FileInputStream(imagefile)
        } catch (e: FileNotFoundException) {
            loggerE("Error:${e.localizedMessage}")
        }
        val bm: Bitmap = BitmapFactory.decodeStream(fis)

        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 20, baos)
        val b: ByteArray = baos.toByteArray()
        val imgString = Base64.encodeToString(b, Base64.NO_WRAP)

        return imgString
    }

    fun encodebitmap(bitmap: Bitmap):String{
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos)
        val b: ByteArray = baos.toByteArray()
        val imgString = Base64.encodeToString(b, Base64.NO_WRAP)

        return imgString
    }

     fun convertBase64ToBitmap(b64: String): Bitmap {
        val imageAsBytes =
            Base64.decode(b64.toByteArray(), Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.size)
    }

  /*   fun getShareableFolder(context: Context, filepathXmlKey: String): File {

        val xml = context.resources.getXml(R.xml.provider_paths)

        val attributes = getAttributesFromXmlNode(xml, filepathXmlKey)

        val folderPath = attributes["path"]
            ?: error("You have to specify the sharable directory in res/xml/filepaths.xml")

        return File(context.filesDir, folderPath).also {
            if (!it.exists()) {
                it.mkdir()
            }
        }
    }*/

     fun getAttributesFromXmlNode(
        xml: XmlResourceParser,
        nodeName: String
    ): Map<String, String> {
        while (xml.eventType != XmlResourceParser.END_DOCUMENT) {
            if (xml.eventType == XmlResourceParser.START_TAG) {
                if (xml.name == nodeName) {
                    if (xml.attributeCount == 0) {
                        return emptyMap()
                    }

                    val attributes = mutableMapOf<String, String>()

                    for (index in 0 until xml.attributeCount) {
                        attributes[xml.getAttributeName(index)] = xml.getAttributeValue(index)
                    }
                    return attributes
                }
            }

            xml.next()
        }

        return emptyMap()
    }

}