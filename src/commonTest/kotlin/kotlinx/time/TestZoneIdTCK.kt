@file:Suppress("FunctionName")

package kotlinx.time

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith


class TestZoneIdTCK {
    fun test_constant_OLD_IDS_POST_2005() {
        val ids = ZoneId.SHORT_IDS
        assertEquals(ids["EST"], "-05:00")
        assertEquals(ids["MST"], "-07:00")
        assertEquals(ids["HST"], "-10:00")
        assertEquals(ids["ACT"], "Australia/Darwin")
        assertEquals(ids["AET"], "Australia/Sydney")
        assertEquals(ids["AGT"], "America/Argentina/Buenos_Aires")
        assertEquals(ids["ART"], "Africa/Cairo")
        assertEquals(ids["AST"], "America/Anchorage")
        assertEquals(ids["BET"], "America/Sao_Paulo")
        assertEquals(ids["BST"], "Asia/Dhaka")
        assertEquals(ids["CAT"], "Africa/Harare")
        assertEquals(ids["CNT"], "America/St_Johns")
        assertEquals(ids["CST"], "America/Chicago")
        assertEquals(ids["CTT"], "Asia/Shanghai")
        assertEquals(ids["EAT"], "Africa/Addis_Ababa")
        assertEquals(ids["ECT"], "Europe/Paris")
        assertEquals(ids["IET"], "America/Indiana/Indianapolis")
        assertEquals(ids["IST"], "Asia/Kolkata")
        assertEquals(ids["JST"], "Asia/Tokyo")
        assertEquals(ids["MIT"], "Pacific/Apia")
        assertEquals(ids["NET"], "Asia/Yerevan")
        assertEquals(ids["NST"], "Pacific/Auckland")
        assertEquals(ids["PLT"], "Asia/Karachi")
        assertEquals(ids["PNT"], "America/Phoenix")
        assertEquals(ids["PRT"], "America/Puerto_Rico")
        assertEquals(ids["PST"], "America/Los_Angeles")
        assertEquals(ids["SST"], "Pacific/Guadalcanal")
        assertEquals(ids["VST"], "Asia/Ho_Chi_Minh")
    }

    //-----------------------------------------------------------------------
    // getAvailableZoneIds()
    //-----------------------------------------------------------------------
    @Test
    fun test_getAvailableGroupIds() {
        val zoneIds = ZoneId.getAvailableZoneIds().toMutableSet()
        assertEquals(zoneIds.contains("Europe/London"), true)
        zoneIds.clear()
        assertEquals(zoneIds.size, 0)
        val zoneIds2 = ZoneId.getAvailableZoneIds()
        assertEquals(zoneIds2.contains("Europe/London"), true)
    }

    //-----------------------------------------------------------------------
    // mapped factory
    //-----------------------------------------------------------------------
    @Test
    fun test_of_string_Map() {
        val map = HashMap<String, String>()
        map["LONDON"] = "Europe/London"
        map["PARIS"] = "Europe/Paris"
        val test = ZoneId.of("LONDON", map)
        assertEquals(test.id, "Europe/London")
    }

    @Test
    fun test_of_string_Map_lookThrough() {
        val map = HashMap<String, String>()
        map["LONDON"] = "Europe/London"
        map["PARIS"] = "Europe/Paris"
        val test = ZoneId.of("Europe/Madrid", map)
        assertEquals(test.id, "Europe/Madrid")
    }

    @Test
    fun test_of_string_Map_emptyMap() {
        val map = HashMap<String, String>()
        val test = ZoneId.of("Europe/Madrid", map)
        assertEquals(test.id, "Europe/Madrid")
    }

    @Test
    fun test_of_string_Map_badFormat() {
        assertFailsWith<DateTimeException> {
            val map = HashMap<String, String>()
            ZoneId.of("Not known", map)
        }
    }

    //-----------------------------------------------------------------------
    // regular factory and .normalized()
    //-----------------------------------------------------------------------
    private data class OffsetBasedValidTestCase(val input: String, val id: String)

    private val offsetBasedValidTestCases = arrayOf(
        OffsetBasedValidTestCase("Z", "Z"),
        OffsetBasedValidTestCase("+0", "Z"),
        OffsetBasedValidTestCase("-0", "Z"),
        OffsetBasedValidTestCase("+00", "Z"),
        OffsetBasedValidTestCase("+0000", "Z"),
        OffsetBasedValidTestCase("+00:00", "Z"),
        OffsetBasedValidTestCase("+000000", "Z"),
        OffsetBasedValidTestCase("+00:00:00", "Z"),
        OffsetBasedValidTestCase("-00", "Z"),
        OffsetBasedValidTestCase("-0000", "Z"),
        OffsetBasedValidTestCase("-00:00", "Z"),
        OffsetBasedValidTestCase("-000000", "Z"),
        OffsetBasedValidTestCase("-00:00:00", "Z"),
        OffsetBasedValidTestCase("+5", "+05:00"),
        OffsetBasedValidTestCase("+01", "+01:00"),
        OffsetBasedValidTestCase("+0100", "+01:00"),
        OffsetBasedValidTestCase("+01:00", "+01:00"),
        OffsetBasedValidTestCase("+010000", "+01:00"),
        OffsetBasedValidTestCase("+01:00:00", "+01:00"),
        OffsetBasedValidTestCase("+12", "+12:00"),
        OffsetBasedValidTestCase("+1234", "+12:34"),
        OffsetBasedValidTestCase("+12:34", "+12:34"),
        OffsetBasedValidTestCase("+123456", "+12:34:56"),
        OffsetBasedValidTestCase("+12:34:56", "+12:34:56"),
        OffsetBasedValidTestCase("-02", "-02:00"),
        OffsetBasedValidTestCase("-5", "-05:00"),
        OffsetBasedValidTestCase("-0200", "-02:00"),
        OffsetBasedValidTestCase("-02:00", "-02:00"),
        OffsetBasedValidTestCase("-020000", "-02:00"),
        OffsetBasedValidTestCase("-02:00:00", "-02:00")
    )

    @Test
    fun factory_of_String_offsetBasedValid_noPrefix() {
        for (testCase in offsetBasedValidTestCases) {
            val (input, id) = testCase
            val test = ZoneId.of(input)
            assertEquals(test.id, id)
            assertEquals(test, ZoneOffset.of(id))
        }
    }
}
