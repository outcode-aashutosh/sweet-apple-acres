package app.sweetappleacres.di

import android.content.Context
import android.provider.Settings
import app.common.extension.logger
import app.common.utils.TLSSocketFactory
import app.common.utils.UserAgentInterceptor
import app.sweetappleacres.data.Prefs
import app.sweetappleacres.data.api.AuthApi
import app.sweetappleacres.data.api.ProductApi
import app.sweetappleacres.data.handler.TokenInterceptor
import app.sweetappleacres.di.DataSourceProperties.SERVER_URL
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import com.outcode.sweetappleacres.BuildConfig
import okhttp3.Cache
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

val netModule = module {
    single { provideGson() }
    single { provideLoggingInterceptor() }
    factory { provideHeaderInterceptor(get(), get()) }
    factory { provideOkHttpClient(get(), get(), get(), get()) }
    factory { provideApiService<AuthApi>(get(), SERVER_URL) }
    factory { provideApiService<ProductApi>(get(), SERVER_URL) }
}

object DataSourceProperties {
    const val SERVER_URL = BuildConfig.BASE_URL
}

fun provideGson(): Gson = Gson()

fun provideLoggingInterceptor() =
    HttpLoggingInterceptor { message -> logger(message, "logRetrofit") }.apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }

fun provideHeaderInterceptor(prefs: Prefs, context: Context): Interceptor =
    Interceptor { chain ->
        var request = chain.request()
        val version = BuildConfig.VERSION_NAME

        request = request.newBuilder()
//            .header("Content-Type", "application/json")
//            .header("Accept", "application/json")
//            .header("App-Version", version)
//            .header("devicetype", "android")
//            .header(
//                "deviceid",
//                Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
//            )
            .build()
        if (prefs.authToken != null) {
            val authorizationHeader: Headers = request.headers.newBuilder()
//                .add("Authorization", prefs.authToken!!)
                .build()
            request = request.newBuilder().headers(authorizationHeader).build()
        }
        chain.proceed(request)
    }

fun provideOkHttpClient(
    context: Context,
    loggingInterceptor: HttpLoggingInterceptor,
    headerInterceptor: Interceptor,
    prefs: Prefs
): OkHttpClient {
    val tlsSocketFactory = TLSSocketFactory()
    val builder = OkHttpClient().newBuilder()
        .sslSocketFactory(tlsSocketFactory, tlsSocketFactory.trustManager)
        .cache(Cache(context.cacheDir, 10 * 1024 * 1024))  // 10MB
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(2, TimeUnit.MINUTES)
        .connectTimeout(2, TimeUnit.MINUTES)
        .addInterceptor(headerInterceptor)
        .addInterceptor(TokenInterceptor(prefs = prefs, context))
        .addInterceptor(UserAgentInterceptor(context))


    if (BuildConfig.DEBUG) {
        builder
            .addInterceptor(loggingInterceptor)
            .addInterceptor(ChuckerInterceptor.Builder(context).build())
    }
    return builder.build()
}

inline fun <reified T> provideApiService(okHttpClient: OkHttpClient, url: String): T {
    val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .client(okHttpClient)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    return retrofit.create()
}
