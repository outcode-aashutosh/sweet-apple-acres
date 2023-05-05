package app.common.extension

fun kelvinToFahrenheit(data: Double): String {
    return "%.2f".format((9 / 5 * (data - 273) + 32))
}

fun kelvinToCelsius(data: Double): String {
    return "%.2f".format((data - KELVIN.toFloat()))
}

fun meterPerSecToKmPerHour(data: Double): String {
    return "%.2f".format((data * 18 / 5))
}

fun meterPerSecToMilePerSec(data: Double): String {
    return "%.2f".format((data * 1.60934 / 0.000277778))

}