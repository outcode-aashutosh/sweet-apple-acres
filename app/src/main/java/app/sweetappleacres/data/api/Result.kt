package app.sweetappleacres.data.api

import app.sweetappleacres.data.response.BaseResponseEntity
import com.google.gson.Gson
import retrofit2.Response

/**
 * A generic class that holds hopper value with its loading status.
 * @param <T>
 */
sealed class Result<T> {

    data class Success<T>(val data: T?, val code: Int = 0) : Result<T>()
    data class Error<T>(
        val exception: Exception? = null,
        val errorMsg: String? = "",
        val code: Int = -1
    ) : Result<T>()

    data class Loading<T>(val data: T?, val percentage: Int? = 0) : Result<T>()

    override fun toString(): String {
        return when (this) {
            is Success -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
            is Loading -> "Loading[data=$data percentage=$percentage]"
        }
    }
}

fun <T> Response<T>.handleResult(): Result<T> {
    return if (isSuccessful && body() != null) {
        Result.Success(body(), this.code())
    } else if (code() == 401) {
        Result.Error(errorMsg = message(), code = 401)
    } else if (errorBody() != null) {
        val errorBody = errorBody()
        val errorModel = Gson().fromJson(
            errorBody?.charStream(), BaseResponseEntity::class.java
        )
        Result.Error(errorMsg = errorModel.message)
    } else {
        Result.Error(errorMsg = "Unknown Error")
    }
}
