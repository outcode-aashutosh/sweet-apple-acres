@file:Suppress(
    "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS",
    "RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS"
)

package app.common.extension

import android.annotation.SuppressLint
import androidx.core.util.Preconditions
import com.google.android.material.timepicker.TimeFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

const val ESG_DATE_FORMAT = "MM/dd/yyyy"

fun getDateText(obtainedDate: String?): String? {
    val newDateFormat = SimpleDateFormat("yyyy-MM-dd")
    var date: Date? = null
    try {
        date = newDateFormat.parse(obtainedDate?.split("T".toRegex())?.toTypedArray()?.get(0))
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    newDateFormat.applyPattern("EE d MMM yyyy")
    return newDateFormat.format(date)
}

fun formatDate(obtainedDate: String?): String? {
    val newDateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy")
    var date: Date? = null
    try {
        date = newDateFormat.parse(obtainedDate)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    newDateFormat.applyPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
    return newDateFormat.format(date)
}


@SuppressLint("NewApi")
fun getDate(obtainedDate: String): String? {
    val tz = TimeZone.getDefault()
    val gmt1 = TimeZone.getTimeZone(tz.id)
        .getDisplayName(false, TimeZone.SHORT).substring(3)
    val modifiedDate = obtainedDate.substring(0, obtainedDate.length - 1) + gmt1

    loggerE("modify:$modifiedDate")

    val sdf =
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.ENGLISH)
    val formatedDate = sdf.format("2021-05-27T07:44:41:0545")

    val newDateFormat = SimpleDateFormat("hh:mm aa")
    var date: Date? = null
    try {
        date = newDateFormat.parse(formatedDate)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    newDateFormat.applyPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
    return newDateFormat.format(date)


}


//SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
//EEE MMM dd HH:mm:ss zzzz yyyy

fun getDateMonth(obtainedDate: String?): String? {
    obtainedDate ?: return null
    var MyDate1: Date? = null
    val newDateFormat = SimpleDateFormat(DATE_FORMAT)
    try {
        MyDate1 = newDateFormat.parse(obtainedDate)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    newDateFormat.applyPattern("MMM yyyy")
    return newDateFormat.format(MyDate1)
}

fun getMonth(obtainedDate: String?): String {
    var date: Date? = null

    val newDateFormat = SimpleDateFormat(DATE_FORMAT)
    try {
        date = newDateFormat.parse(obtainedDate)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    val date_format1 = SimpleDateFormat(DATE_FORMAT)
    var date_str = date_format1.format(date)

    val date_format2 = SimpleDateFormat("MMMM yyyy ")
    date_str = date_format2.format(date)

    return date_str
}


fun isDateValid(startDate: String?, endDate: String?): Boolean {
    val dfDate = SimpleDateFormat("MM-dd-yyyy")
    var b = false
    try {
        b = dfDate.parse(startDate).before(dfDate.parse(endDate))
        loggerE("$startDate/$endDate")
    } catch (e: Exception) {
        e.printStackTrace()
        loggerE(e.message)
    }
    return b
}

@SuppressLint("SimpleDateFormat")
fun isTimeValid(startDate: String?, endDate: String?): Boolean {
    val sdf = SimpleDateFormat("hh:mm")
    var b = false
    try {
        b = sdf.parse(startDate).before(sdf.parse(endDate))
        loggerE("$startDate/$endDate")
    } catch (e: Exception) {
        e.printStackTrace()
        loggerE(e.message)
    }
    return b
}


fun getDayMonthText(obtainedDate: String?): String? {
    val newDateFormat = SimpleDateFormat("yyyy-MM-dd")
    var date: Date? = null
    try {
        date = newDateFormat.parse(obtainedDate)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    newDateFormat.applyPattern("yyyy/MMMM/dd")
    val dateString = newDateFormat.format(date)
    loggerE("DateString:$dateString")
    return try {
        "${dateString.split("/")[1]} ${getDayString(dateString.split("/")[2].toInt())}, ${
            dateString.split(
                "/"
            )[0]
        }  "
    } catch (e: Exception) {
        loggerE("dateError:${e.localizedMessage}")
        ""
    }
}

fun getDayString(day: Int): String {
    return "$day${getDayOfMonthSuffix(day)}"
}


@SuppressLint("RestrictedApi")
fun getDayOfMonthSuffix(n: Int): String {
    Preconditions.checkArgument(n in 1..31, "illegal day of month: $n")
    return if (n in 11..13) {
        "th"
    } else when (n % 10) {
        1 -> "st"
        2 -> "nd"
        3 -> "rd"
        else -> "th"
    }
}

fun String.getDateWithServerTimeStamp(dateFormatString: String = dateFormatWithOutTime): String? {
    val dateFormat = SimpleDateFormat(
        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
        Locale.getDefault()
    )
    dateFormat.timeZone = TimeZone.getTimeZone("GMT")  // IMP !!!
    try {
        val dateString = dateFormat.parse(this)
        return convertToCustomFormat(dateString.toString(), dateFormatString)
    } catch (e: ParseException) {
        loggerE("datFormat error:${e.localizedMessage}")
        return null
    }
}

private fun convertToCustomFormat(dateStr: String?, dateFormat: String): String {
    val utc = TimeZone.getTimeZone("UTC")
    val sourceFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy")
    val destFormat = SimpleDateFormat(dateFormat)
    sourceFormat.timeZone = utc
    val convertedDate = sourceFormat.parse(dateStr)
    return destFormat.format(convertedDate)
}

fun currentDate(format: String = SERVER_TIME_DATE_FORMAT_WITH_TIME): String {
    var c: Calendar = Calendar.getInstance()
    var df: SimpleDateFormat? = null
    var formattedDate = ""
    df = SimpleDateFormat(format)
    formattedDate = df.format(c.time)
    return formattedDate
}

fun currentTime(): String {
    var c: Calendar = Calendar.getInstance()
    var df: SimpleDateFormat? = null
    var formattedDate = ""
    df = SimpleDateFormat("hh:mm aa")
    formattedDate = df.format(c.time)
    return formattedDate
}

fun getTimePeriod(c: Calendar): Int {
    when (c.get(Calendar.HOUR_OF_DAY)) {
        in 0..11 -> {
            return 1
        }
        in 12..15 -> {
            return 2
        }
        in 16..20 -> {
            return 3
        }
        in 21..23 -> {
            return 4
        }
        else -> {
            return 1
        }
    }
}

fun getDayOfWeek(calendar: Calendar): String {
    calendar.add(Calendar.DAY_OF_WEEK, 1)
    return SimpleDateFormat("EE", Locale.ENGLISH).format(calendar.time)
}

fun getDateTime(date: Date): String {
    val sdf = SimpleDateFormat("MM/dd/yyyy, hh:mm a", Locale.ENGLISH)
    sdf.timeZone = TimeZone.getDefault()
    return sdf.format(date)
}

fun getDateTimeInFormat(date: Date, format: String): String {
    val sdf = SimpleDateFormat(format, Locale.ENGLISH)
    sdf.timeZone = TimeZone.getDefault()
    return sdf.format(date)
}

fun getCurrentDateTime(date: Date): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.ENGLISH)
    sdf.timeZone = TimeZone.getDefault()
    return sdf.format(date)
}

fun getCurrentDateTimeUTC(date: Date): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.ENGLISH)
    sdf.timeZone = TimeZone.getTimeZone("UTC")
    return sdf.format(date)
}


fun parseServerDate(date: String): String {
    return try {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.ENGLISH)
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        val parsedDate = sdf.parse(date)
        getDateTime(parsedDate)
    } catch (e: Exception) {
        loggerE(e.localizedMessage.orEmpty())
        ""
    }
}

fun parseDateToFormat(date: String, format: String): String {
    return try {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        val parsedDate = sdf.parse(date)
        getDateTimeInFormat(parsedDate, format)
    } catch (e: Exception) {
        loggerE(e.localizedMessage.orEmpty())
        ""
    }
}

fun parseServerDateToDateTime(date: String): Date {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.ENGLISH)
    sdf.timeZone = TimeZone.getTimeZone("UTC")
    return sdf.parse(date)
}

fun convertToLocalTime(time: String): String {
    return try {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.ENGLISH)
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        val timeInUTC = dateFormat.parse(time)
        dateFormat.timeZone = TimeZone.getDefault()
        dateFormat.format(timeInUTC)
    } catch (e: Exception) {
        ""
    }
}

fun getFormattedDifference(millis: Long): String {
    var elapsedMillis = millis
    val secondsInMilli: Long = 1000
    val minutesInMilli = secondsInMilli * 60
    val hoursInMilli = minutesInMilli * 60
    val daysInMilli = hoursInMilli * 24
    val elapsedDays: Long = millis / daysInMilli
    elapsedMillis %= daysInMilli
    val elapsedHours: Long = millis / hoursInMilli
    elapsedMillis %= hoursInMilli
    val elapsedMinutes: Long = millis / minutesInMilli
    elapsedMillis %= minutesInMilli
    val elapsedSeconds: Long = millis / secondsInMilli
    return if (daysInMilli != 0L) "${elapsedDays}d:${elapsedHours}h:${elapsedMinutes}m:${elapsedSeconds}s" else "${elapsedHours}h:${elapsedMinutes}m:${elapsedSeconds}s"
}

fun getFormattedMillis(millis: Long): String = String.format("%02dd:%02dh:%02dm:%02ds",
    TimeUnit.MILLISECONDS.toDays(millis),
    TimeUnit.MILLISECONDS.toHours(millis) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(millis)),
    TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), // The change is in this line
    TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)))

fun getFormattedTime(date: String): String {
    return try {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.ENGLISH)
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        val parsedTime = sdf.parse(date)
        val toFormat = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
        toFormat.timeZone = TimeZone.getDefault()
        toFormat.format(parsedTime)
    } catch (e: Exception) {
        ""
    }
}

fun getFormattedCurrentTime(date: Date) : String {
    val sdf = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
    sdf.timeZone = TimeZone.getDefault()
    return sdf.format(date)
}

fun getMilliSecondsFromDateTime(date: String): Long {
    val sdf = SimpleDateFormat("HH:mm:ss", Locale.ENGLISH)
    sdf.timeZone = TimeZone.getTimeZone("UTC")
    val parsedTime = sdf.parse(date)
    return parsedTime.time
}

