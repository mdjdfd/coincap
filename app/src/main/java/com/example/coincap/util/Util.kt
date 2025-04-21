package com.example.coincap.util

import java.math.BigDecimal
import java.text.DecimalFormat

object Util {

    fun moneyFormatter(money: Double): String {
        var value = money
        val arr = arrayOf("", "K", "M", "B", "T", "P", "E")
        var index = 0
        while ((value / 1000) >= 1) {
            value /= 1000
            index++
        }
        val decimalFormat = DecimalFormat("#.##")
        return String.format("$%s%s", decimalFormat.format(value), arr[index])
    }

    fun changePercentageFormatter(change: BigDecimal): String{
        val decimalFormat = DecimalFormat("#,##0.00")
        return String.format("%s", decimalFormat.format(change) + "%")
    }

}