package app.sweetappleacres

import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import android.os.LocaleList
import java.util.*

class AppContextWrapper(base: Application): ContextWrapper(base) {

    companion object {
        @Suppress("DEPRECATION")
        fun wrap(context: Context?, newLocale: Locale):ContextWrapper {
            val res = context?.resources
            val configuration = res?.configuration
            val ctx: Context?
            when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> {
                    configuration?.setLocale(newLocale)
                    val localeList = LocaleList(newLocale)
                    LocaleList.setDefault(localeList)
                    configuration?.setLocales(localeList)
                    ctx = configuration?.let { context.createConfigurationContext(it) }
                }
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> {
                    val resources = context?.resources
                    val dm = resources?.displayMetrics
                    val conf = resources?.configuration
                    conf?.setLocale(newLocale)
                    resources?.updateConfiguration(conf, dm)
                    ctx = conf?.let { context.createConfigurationContext(it) }
                }
                else -> {
                    val resources = context?.resources
                    val dm = resources?.displayMetrics
                    val conf = resources?.configuration
                    conf?.setLocale(newLocale)
                    resources?.updateConfiguration(conf, dm)
                    ctx = conf?.let { context.createConfigurationContext(it) }
                }
            }
            return ContextWrapper(ctx)
        }
    }
}