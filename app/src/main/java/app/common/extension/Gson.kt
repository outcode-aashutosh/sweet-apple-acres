package app.common.extension

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

// Transform simple object to String with Gson
inline fun <reified T : Any> T.toJson(): String = Gson().toJson(this, T::class.java)

fun getPrettyGson(): Gson {
    return GsonBuilder().setPrettyPrinting().create()
}

inline fun <reified T : Any> T.toJsonPretty(): String = getPrettyGson().toJson()

// Transform String Json to Object
inline fun <reified T : Any> String.fromJson(): T = Gson().fromJson(this, T::class.java)

// Transform String List Json to Object
inline fun <reified T : Any> String.fromJsonList(): List<T> =
    when (this.isNotEmpty()) {
        true -> Gson().fromJson(this, object : TypeToken<List<T>>() {}.type)
        false -> emptyList()
    }