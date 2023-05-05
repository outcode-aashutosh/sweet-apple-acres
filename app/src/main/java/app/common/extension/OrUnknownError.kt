package app.common.extension

fun String?.orUnknownError() = this ?: "Something went wrong Please try again!!"
fun String?.orSuccess() = this ?: "Success!!"
fun orUnknownError() = "Something went wrong Please try again!!"
fun failedToRefreshToken() = "Failed to refresh the token"
