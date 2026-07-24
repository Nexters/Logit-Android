package com.useai.core.common.extensions

import java.text.NumberFormat
import java.util.Locale

fun Int.formatTokenCount(): String =
    NumberFormat.getIntegerInstance(Locale.getDefault()).format(this)
