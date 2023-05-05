package app.sweetappleacres.data.response

import com.google.gson.annotations.SerializedName

open class BaseResponseEntity(
    @SerializedName("message")
    open var message: String? = null,
    @SerializedName("ErrMessage")
    open var errorMessage: String? = null,
    @SerializedName("ErrMsg")
    open var ErrMsg: String? = null,
    @SerializedName("InfoMessage")
    open var infoMessage: String? = null,
    @SerializedName("detail")
    open var detail: String? = null,
    @SerializedName("ErrCode")
    var errorCode: Int? = null,
    @SerializedName("is_hidden")
    var isHidden: Boolean = false,
    @SerializedName("Status")
    var status: Boolean = false,

)

open class BaseErrorEntity(
    @SerializedName("message")
    open var message: String? = null,
    @SerializedName("detail")
    open var detail: String? = null,
    @SerializedName("non_field_errors")
    val nonFieldErrors: List<String>? = null,
    @SerializedName("errors")
    var errors: List<String>? = null,
    @SerializedName("status")
    var status: Int,
    @SerializedName("data")
    var data: List<Any>? = null

    /*,@SerializedName("email")
    var email: List<String> = emptyList(),
    @SerializedName("registration_id")
    var registrationId: List<String> = emptyList(),
    @SerializedName("code")
    val code: String? = null*/
)

data class Errors(
    @SerializedName("errors")
    val errors: Any?
)