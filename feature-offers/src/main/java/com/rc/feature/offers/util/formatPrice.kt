package com.rc.feature.offers.util

import java.text.NumberFormat
import java.util.Locale

fun formatPrice(raw: String): String {
    return runCatching {
        val n = raw.replace("[^\\d.]".toRegex(), "").toDouble()
        NumberFormat.getCurrencyInstance(Locale.US).format(n)
    }.getOrElse { raw }
}
