package com.example.coincap.util

import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.math.BigDecimal

class UtilTest {

    @Test
    fun testMoneyFormatter(){
        // Given
        val decimal = 3.14156700
        val thousand = 1000.0
        val million = 1000000.0
        val billion = 1000000000.0
        val trillion = 1000000000000.0
        val quadrillion = 1000000000000000.0
        val quintillion = 1000000000000000000.0

        // When
        val resultDecimal = Util.moneyFormatter(decimal)
        val resultThousand = Util.moneyFormatter(thousand)
        val resultMillion = Util.moneyFormatter(million)
        val resultBillion = Util.moneyFormatter(billion)
        val resultTrillion = Util.moneyFormatter(trillion)
        val resultQuadrillion = Util.moneyFormatter(quadrillion)
        val resultQuintillion = Util.moneyFormatter(quintillion)


        // Then
        assertEquals("$3.14", resultDecimal)
        assertEquals("$1K", resultThousand)
        assertEquals("$1M", resultMillion)
        assertEquals("$1B", resultBillion)
        assertEquals("$1T", resultTrillion)
        assertEquals("$1P", resultQuadrillion)
        assertEquals("$1E", resultQuintillion)
    }


    @Test
    fun testChangePercentageFormatter(){
        // Given
        val bigDecimalA = 1_000_000.bd
        val bigDecimalB = 0.1415674.toBigDecimal()

        // When
        val resultA = Util.changePercentageFormatter(bigDecimalA)
        val resultB = Util.changePercentageFormatter(bigDecimalB)

        // Then
        assertEquals("1,000,000.00%", resultA)
        assertEquals("0.14%", resultB)
    }

    private val Int.bd: BigDecimal get() = BigDecimal(this)

}