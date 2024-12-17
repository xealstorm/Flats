package com.kalachev.aviv.ext

import java.math.BigDecimal
import java.math.RoundingMode

fun BigDecimal.toGoodLookingString(): String = if (this.stripTrailingZeros().scale() <= 0) {
    this.toBigInteger().toString()
} else {
    this.setScale(2, RoundingMode.HALF_UP).toPlainString()
}