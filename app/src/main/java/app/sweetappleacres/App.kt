package app.sweetappleacres

import android.app.Application
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleObserver
import app.common.extension.loggerE
import app.common.extension.plantLogger
import app.common.extension.showPositiveAlert
import app.sweetappleacres.data.Prefs
import app.sweetappleacres.di.*
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin

class App : Application(), LifecycleObserver {
    companion object {
        lateinit var instance: App
    }

    private lateinit var activity: AppCompatActivity
    private val pref: Prefs by inject()
    override fun onCreate() {
        super.onCreate()
        instance = this
        plantLogger()
        startKoin {
            androidLogger()
            androidContext(this@App)
            koin.loadModules(
                listOf(
                    appModule,
                    netModule,
                    storageModule,
                    dataSourceModule,
                    repositoryModule,
                    viewModelModule,
                )
            )
        }
    }

    fun setUpActivityContext(activityContext: AppCompatActivity) {
        this.activity = activityContext
    }

    fun forceLogout() {
        if (::activity.isInitialized) {
            try {
                activity.runOnUiThread {
                    runBlocking {
                        pref.clearPrefs()
                        activity.showPositiveAlert(message = "Your session has expired. Please login again.",
                            onConfirm = {
                                val i = baseContext.packageManager.getLaunchIntentForPackage(
                                    baseContext.packageName
                                )
                                i!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                startActivity(i)
                            })
                    }
                }
            } catch (e: ExceptionInInitializerError) {
                loggerE("forceLogOutError:${e.message}")
            }

        } else {
            loggerE("else:forceLogout")
        }
    }

}