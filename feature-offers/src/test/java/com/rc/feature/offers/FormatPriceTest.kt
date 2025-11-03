package com.rc.feature.offers

import com.rc.feature.offers.util.formatPrice
import junit.framework.TestCase.assertEquals
import kotlin.test.Test

// NOTE: Test environment locale can affect NumberFormat.
// A robust test might temporarily set the locale or verify formatting parts.
// We rely on Locale.US being used inside formatPrice.

class FormatPriceTest {

    @Test
    fun `test valid numeric string formats correctly`() {
        assertEquals("$123.45", formatPrice("123.45"))
    }

    @Test
    fun `test valid integer string formats correctly`() {
        // Should include the two decimal places
        assertEquals("$500.00", formatPrice("500"))
    }

    @Test
    fun `test string with non-numeric symbols formats correctly`() {
        // Should strip '€' and format as USD
        assertEquals("$99.99", formatPrice("€99.99"))
    }

    @Test
    fun `test string with text and symbols formats correctly`() {
        // Should strip all text and symbols
        assertEquals("$10.50", formatPrice("price 10.50 AUD"))
    }

    @Test
    fun `test large number includes grouping separator`() {
        // Verifies the use of NumberFormat
        assertEquals("$1,234,567.89", formatPrice("1234567.89"))
    }

    @Test
    fun `test empty string returns original input`() {
        // Should fail to parse and return ""
        assertEquals("", formatPrice(""))
    }

    @Test
    fun `test non-numeric text returns original input`() {
        val input = "invalid price"
        // Should fail to parse and return the original string
        assertEquals(input, formatPrice(input))
    }

    @Test
    fun `test malformed decimal returns original input`() {
        val input = "1.2.3.4"
        // toDouble() will likely fail or behave unexpectedly, causing fallback
        assertEquals(input, formatPrice(input))
    }
}