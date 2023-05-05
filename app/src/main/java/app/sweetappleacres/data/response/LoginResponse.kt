package app.sweetappleacres.data.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("data")
    var loginData: LoginData,
    @SerializedName("message")
    var message: String
)

data class LoginData(
    @SerializedName("refresh_token")
    var refreshToken: String,
    @SerializedName("token")
    var token: String,
    @SerializedName("user_id")
    var userId: String,
)