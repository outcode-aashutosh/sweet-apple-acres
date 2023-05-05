package app.common.extension

import app.sweetappleacres.App
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.RetentionManager

val chuckerCollector: ChuckerCollector by lazy {
    ChuckerCollector(App.instance, true, RetentionManager.Period.ONE_HOUR)
}

fun Throwable.logException(tag: String = "EXCEPTION_TRACKER") {
    chuckerCollector.onError(tag, this)
    printStackTrace()
}