package app.sweetappleacres.data.handler

import android.util.Log
import app.common.extension.logException
import app.common.extension.loggerE
import app.sweetappleacres.App
import app.sweetappleacres.data.api.Resource
import app.sweetappleacres.data.response.BaseErrorEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Response


suspend fun <T> Response<T>.handleResponse(
    doActionOnSuccess: suspend (body: T) -> Unit = {},
    doActionOnFailure: suspend (body: String?) -> Unit = {}
): Resource<T> {
    return if (isSuccessful) {
        if (body() != null) {
            doActionOnSuccess.invoke(body()!!)
            Resource.Success(body()!!)
        } else {
            doActionOnFailure.invoke(message())
            Resource.Error(message())
        }
    } else if (code() in listOf(401, 403)) {
        App.instance.forceLogout()
        val errorBody = errorBody()?.string()
        val errorModel = Gson().fromJson(errorBody, BaseErrorEntity::class.java)
        doActionOnFailure.invoke(
            errorModel.detail ?: errorModel.message
            ?: errorModel.nonFieldErrors?.get(0) ?: throw Exception()
        )
        Resource.Error(
            code = code(),
            message = errorModel.detail ?: errorModel.message
            ?: errorModel.nonFieldErrors?.get(0) ?: throw Exception()
        )
    } else if (code() == 500) {
        doActionOnFailure.invoke("Something went wrong on server side please try again later.")
        Resource.Error(
            "Something went wrong on server side please try again later.", code = code()
        )
    } else if (code() == 404) {
        doActionOnFailure.invoke("Something went wrong on server side please try again later.")
        Resource.Error(
            "Something went wrong on server side please try again later.", code = code()
        )
    } else {
        val genericErrorMessage = "Error encountered. Please try again later."

        val errorBody = errorBody()?.string()

        return try {
            //Parse: {"message":{"Incorrect otp"}}
            //Parse: {"detail":{"Incorrect otp"}}
            //Parse: {"errors":["Incorrect otp","Error 2"]}
            //Parse: {"errors":["Incorrect otp","Error 2"]}
            loggerE("generic")
            val errorModel = Gson().fromJson(errorBody, BaseErrorEntity::class.java)
            doActionOnFailure.invoke(
                errorModel.detail ?: errorModel.message
                ?: errorModel.nonFieldErrors?.get(0) ?: throw Exception()
            )
            Resource.Error(
                code = code(),
                message = errorModel.detail ?: errorModel.message
                ?: errorModel.nonFieldErrors?.get(0) ?: throw Exception()
            )

        } catch (e: Exception) {
            e.logException()
            loggerE("first error")
            //Parse: {password:This field may not be blank.}
            val type = object : TypeToken<Map<String, String>>() {}.type

            try {
                val data: Map<String, String> = Gson().fromJson(errorBody, type)
                return if (!data.isNullOrEmpty()) {
                    val firstEntry = data.entries.iterator().next()
                    val errorMessage = firstEntry.value
                    if (errorMessage.isNullOrEmpty()) {
                        doActionOnFailure.invoke(message())
                        Resource.Error(code = code(), message = message())
                    } else {
                        //Display first message from the message list in given key
                        doActionOnFailure.invoke(errorMessage)
                        Resource.Error(code = code(), message = errorMessage)
                    }
                } else {
                    throw Exception()
                }
            } catch (e: Exception) {
                Log.e("ResponseHandler", "Second Exception")
                e.logException()
                //Parse: ["User with this email already exists"]*/


                try {
                    val stringArrayType = object : TypeToken<List<String>>() {}.type
                    val arrayMessages: List<String> = Gson().fromJson(errorBody, stringArrayType)
                    return if (!arrayMessages.isNullOrEmpty()) {
                        doActionOnFailure.invoke(arrayMessages[0])
                        Resource.Error(code = code(), message = arrayMessages[0])
                    } else {
                        throw Exception()
                    }
                } catch (e: Exception) {
                    Log.e("ResponseHandler", "third Exception")
                    e.logException()
                    //Parse: {"password":["This field may not be blank."]}
                    val type = object : TypeToken<Map<String, List<String>>>() {}.type

                    val data: Map<String, List<String>> = Gson().fromJson(errorBody, type)
                    loggerE("error:$data")

                    return if (!data.isNullOrEmpty()) {
                        val firstEntry = data.entries.iterator().next()
                        val errorMessage = firstEntry.value
                        if (errorMessage.isNullOrEmpty()) {
                            doActionOnFailure.invoke(message())
                            Resource.Error(code = code(), message = message())
                        } else {
                            //Display first message from the message list in given key
                            doActionOnFailure.invoke(errorMessage.first())
                            Resource.Error(code = code(), message = errorMessage.first())
                        }
                    } else {
                        Resource.Error(code = code(), message = message())
                    }
                }
            }
        } catch (e: Exception) {
            loggerE("last error")

            e.logException()
            doActionOnFailure.invoke(genericErrorMessage)
            Resource.Error(genericErrorMessage, code())
        }
    }
}

inline fun <T> doTryCatch(task: () -> Resource<T>): Resource<T> {
    return try {
        task.invoke()
    } catch (e: Exception) {
        e.logException()
        loggerE("catchError:${e.message}")
        Resource.Error(e.generateMessage())
    }
}

inline fun tryIgnoreCatch(task: () -> Unit) {
    try {
        task.invoke()
    } catch (e: Exception) {
        e.logException()
    }
}
