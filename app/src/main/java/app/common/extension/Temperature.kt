package app.common.extension

import kotlin.math.roundToInt


fun Double.checkNegativeTemperature(): String {
    return if (this < 0) "${this.roundToInt()}" else "+ ${this.roundToInt()}"
}