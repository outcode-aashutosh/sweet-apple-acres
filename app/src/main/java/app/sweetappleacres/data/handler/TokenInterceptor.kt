package app.sweetappleacres.data.handler

import android.content.Context
import app.common.extension.failedToRefreshToken
import app.sweetappleacres.data.Prefs
import app.sweetappleacres.data.api.AuthApi
import app.sweetappleacres.data.request.RefreshTokenRequest
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.outcode.sweetappleacres.BuildConfig.BASE_URL
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TokenInterceptor(private val prefs: Prefs, private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val response = customInterceptor(chain, "")
        val refreshToken = prefs.refreshToken
        return when (response.code) {
            401 -> {
                response.close()
                runBlocking {
                    val newAccessToken = refreshToken.let { refreshToken(it) }
                    prefs.authToken = newAccessToken
                    customInterceptor(chain, newAccessToken ?: "")
                }
            }
            else -> {
                response
            }
        }

    }

    private fun customInterceptor(
        chain: Interceptor.Chain,
        newAccessToken: String
    ): Response {
      //  loggerE("customInterceptor:${prefs.authToken}")
        val request =
            chain.request().newBuilder().header("Authorization", "${prefs.authToken}").build()
        return chain.proceed(request)
    }

    private fun refreshToken(token: String): String {
    //    loggerE("refresh token initialized")

        val tokenApiService = getRefreshTokenService()
        try {
            val response = runBlocking {
                tokenApiService.refreshToken(
                    RefreshTokenRequest(refresh = token)
                ).execute()
            }
            if (response.code() == 200) {
                prefs.authToken = response.body()?.loginData?.token!!
                prefs.refreshToken = response.body()?.loginData?.refreshToken!!
                return response.body()?.loginData?.token!!
            }
        } catch (e: Exception) {
            return e.message.toString()
        }

        return failedToRefreshToken()
    }

    private fun getRefreshTokenService(): AuthApi {
        val okHttpClient = OkHttpClient
            .Builder()
            .addInterceptor(ChuckerInterceptor.Builder(context).build())
            .build()
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(okHttpClient).build().create(AuthApi::class.java)
    }
}
