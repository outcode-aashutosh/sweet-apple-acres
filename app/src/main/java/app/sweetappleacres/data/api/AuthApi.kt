package app.sweetappleacres.data.api

import app.common.extension.BASE_PATH
import app.sweetappleacres.data.request.RefreshTokenRequest
import app.sweetappleacres.data.response.BaseResponseEntity
import app.sweetappleacres.data.response.LoginResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    companion object {
        private const val LOGIN = "$BASE_PATH/login/"
        private const val REGISTER = "$BASE_PATH/register/"
        private const val REFRESH_TOKEN = "$BASE_PATH/token/refresh"
        private const val SET_FCM_TOKEN = "$BASE_PATH/set-fcm-token/"
        private const val LOG_OUT = "$BASE_PATH/logout/"
    }

    @POST(LOG_OUT)
    suspend fun logout(): Response<BaseResponseEntity>

    @POST(LOGIN)
    suspend fun loginUser(
        //@Body request: LoginRequest,
    ): Response<LoginResponse>

    @POST(REGISTER)
    suspend fun registerUser(
//        @Body request: RegisterRequest,
    ): Response<BaseResponseEntity>

    @POST(REFRESH_TOKEN)
    fun refreshToken(
        @Body request: RefreshTokenRequest,
    ): Call<LoginResponse>

    @POST(SET_FCM_TOKEN)
    suspend fun setFcmToken(
//        @Body request: FcmTokenRequest
    ): Response<BaseResponseEntity>
}