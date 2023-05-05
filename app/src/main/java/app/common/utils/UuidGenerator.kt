package app.common.utils

import java.util.*

fun randomUUID() = UUID.randomUUID().toString()
fun randomInt() = (0..9999).shuffled().last()