@file:Suppress("FunctionName")

package kotlinx.time

import kotlin.test.Test
import kotlin.test.assertTrue

class TestPlatformZoneId {
    @Test
    fun testValidPlatformTimeZoneId() {
        val zoneId = getPlatformCurrentTimeZoneId()
        assertTrue(zoneId.isNotEmpty())
        val zoneIds = getPlatformAvailableTimeZoneIds()
        assertTrue(zoneIds.contains(zoneId))
    }
}
