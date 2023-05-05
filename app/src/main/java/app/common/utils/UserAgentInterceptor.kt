package app.common.utils

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import okhttp3.Interceptor
import okhttp3.Response

class UserAgentInterceptor(
    private val context: Context,
    private val overrideAppName: String? = null
) : Interceptor {

    companion object{
        /**
         * DO NOT MODIFY THIS FUNCTION!! ANY MODIFICATION MAY CAUSE UNINTENDED BUGS
         * */
        fun getDefaultUserAgent(): String {
            val result = StringBuilder(64)
            result.append("Dalvik/")
            result.append(System.getProperty("java.vm.version")) // such as 1.1.0
            result.append(" (Linux; U; Android ")
            val version = Build.VERSION.RELEASE // "1.0" or "3.4b5"
            result.append(if (version.isNotEmpty()) version else "1.0")

            // add the model for the release build
            if ("REL" == Build.VERSION.CODENAME) {
                val model = Build.MODEL
                if (model.isNotEmpty()) {
                    result.append("; ")
                    result.append(model)
                }
            }
            val id = Build.ID // "MASTER" or "M4-rc20"
            if (id.isNotEmpty()) {
                result.append(" Build/")
                result.append(id)
            }
            result.append(")")
            return result.toString()
        }
    }

    private val userAgent: String by lazy {
        //buildUserAgent(context)
        getDefaultUserAgent()
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        builder.header("User-Agent", userAgent)
        return chain.proceed(builder.build())
    }

    private fun buildUserAgent(context: Context): String {
        with(context.packageManager) {
            val versionName = try {
                getPackageInfo(context.packageName, 0).versionName
            } catch (e: PackageManager.NameNotFoundException) {
                "nameNotFound"
            }
            val versionCode = try {
                getPackageInfo(context.packageName, 0).versionCode.toString()
            } catch (e: PackageManager.NameNotFoundException) {
                "versionCodeNotFound"
            }

            val applicationInfo = context.applicationInfo
            val stringId = applicationInfo.labelRes
            val appName =
                overrideAppName ?: if (stringId == 0) applicationInfo.nonLocalizedLabel.toString()
                else context.getString(stringId)

            val manufacturer = Build.MANUFACTURER
            val model = Build.MODEL
            val version = Build.VERSION.SDK_INT
            val versionRelease = Build.VERSION.RELEASE

            val installerName = getInstallerPackageName(context.packageName) ?: "StandAloneInstall"

            return "$appName / $versionName($versionCode); $installerName; ($manufacturer; $model; SDK $version; Android $versionRelease)"
        }
    }
}