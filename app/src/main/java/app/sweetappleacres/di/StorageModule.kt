package app.sweetappleacres.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import app.sweetappleacres.data.Prefs
import app.sweetappleacres.data.database.AppDatabase
import app.sweetappleacres.di.PersistenceDataSourceProperties.PREF_NAME
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val storageModule = module {
    single { PREF_NAME.provideSharedPreference(get()) }
    single { providePrefsManager(get()) }
    single {
        Room
            .databaseBuilder(androidApplication(), AppDatabase::class.java, "Sweet Apple Acres Db")
            .fallbackToDestructiveMigration()
            .build()
    }
    single {
        get<AppDatabase>().appDao()
    }
}

object PersistenceDataSourceProperties {
    const val PREF_NAME = "Sweet.Apple.Acres.preference"
}

private fun String.provideSharedPreference(context: Context): SharedPreferences {
    return context.getSharedPreferences(this, Context.MODE_PRIVATE)
}

private fun providePrefsManager(sharedPreferences: SharedPreferences): Prefs {
    return Prefs(sharedPreferences)
}





