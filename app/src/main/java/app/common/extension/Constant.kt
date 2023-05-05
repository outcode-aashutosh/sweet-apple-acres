package app.common.extension

import com.outcode.sweetappleacres.BuildConfig


const val BASE_PATH = ".netlify/functions/api"


//int Constant
const val DEFAULT_CHARACTER_COUNT = 14
const val CODE_EXPIRE = 120
const val DEFAULT_CHARACTER_COUNT_LONG = 1000
const val DEFAULT_CHARACTER_COUNT_SHORT = 100
const val FILEPATH_XML_KEY = "files-path"
const val countDownInterval: Long = 1000
const val millisInFuture: Long = 1000 * 60

const val PASSWORD_LENGTH = 8
const val PAGE: Int = 20
const val JSA_STEP_COUNT: Int = 6

const val dateFormatWithOutTime = "yyyy-MM-dd"
const val SERVER_TIME_DATE_FORMAT_WITH_TIME = "MMM dd,yyyy @ hh:mm aa"


const val BACK_STACK = "BACK_STACK"
const val KEY_USERNAME = "forget_username"
const val KEY_PASSWORD = "forget_password"
const val KEY_ALREADY_EXIST = "already_selected"


//string constant
const val NULL_DATE = "0000-00-00"
const val START = "start"
const val DATE_FORMAT = "MM-dd-yyyy"

const val START_INITIAL = -1

const val FIRST_NAME_REGEX = "[\\s\\S]{3,}\$"
const val PHONE_REGEX = "^[0-9]{9,15}\$"
const val NUMBER_REGEX = "^[0-9]{6}\$"
val PasswordRegex = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@\$%^&*-]).{8,}\$".toRegex()
val NewPasswordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^\\da-zA-Z]).{8,}\$".toRegex()
const val FLOAT_REGEX = "^([+-]?\\d*\\.?\\d*)$"
const val URL_REGEX = ("((http|https)://)(www.)?"
        + "[a-zA-Z0-9@:%._\\+~#?&//=]"
        + "{2,256}\\.[a-z]"
        + "{2,6}\\b([-a-zA-Z0-9@:%"
        + "._\\+~#?&//=]*)")

val PasswordInfo = """Password must contain
                |1. At least eight characters
                |2. At least one capital letter
                |3. At least one small letter
                |4. At least one number
                |5. At lease one special character
            """.trimMargin()

const val cannotConnectErrorMessage =
    "Could not connect to server. Please check your network connection and try again."
const val internetNotAvailableErrorMessage =
    "No internet connection. Please check your network connection and try again."


const val KELVIN = 273.15
const val CELSIUS = 100

const val REQUEST_CHECK_SETTINGS = 484

val SOCKET_URL = BuildConfig.BASE_URL


var IS_FIRST = true
var IS_DIALOG_SHOWING = false





