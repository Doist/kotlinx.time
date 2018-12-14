@file:Suppress("FunctionName")

package kotlinx.time

import kotlin.test.Test
import kotlin.test.assertEquals

class TestZoneId {
    @Test
    fun test_constant_UTC() {
        val test = ZoneOffset.UTC
        assertEquals(test.id, "Z")
    }

    //-----------------------------------------------------------------------
    // Europe/London
    //-----------------------------------------------------------------------
    fun test_London() {
        val test = ZoneId.of("Europe/London")
        assertEquals(test.id, "Europe/London")
    }

    //-----------------------------------------------------------------------
    // getXxx() isXxx()
    //-----------------------------------------------------------------------
    fun test_get_Tzdb() {
        val test = ZoneId.of("Europe/London")
        assertEquals(test.id, "Europe/London")
    }

    fun test_get_TzdbFixed() {
        val test = ZoneId.of("+01:30")
        assertEquals(test.id, "+01:30")
    }
}
