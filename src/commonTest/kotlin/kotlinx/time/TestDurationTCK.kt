/*
 * Copyright (c) 2019, Doist
 *
 * This code has been rewritten in Kotlin. It is redistributed under the terms
 * of the GNU General Public License version 2 license, as was the prior work.
 */

/*
 * Copyright (c) 2012, 2016, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

/*
 * This file is available under and governed by the GNU General Public
 * License version 2 only, as published by the Free Software Foundation.
 * However, the following notice accompanied the original version of this
 * file:
 *
 * Copyright (c) 2007-2012, Stephen Colebourne & Michael Nascimento Santos
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  * Neither the name of JSR-310 nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

@file:Suppress("FunctionName")

import kotlinx.time.Duration
import kotlinx.time.LocalTime.Companion.MILLIS_PER_SECOND
import kotlinx.time.LocalTime.Companion.NANOS_PER_MILLI
import kotlinx.time.LocalTime.Companion.NANOS_PER_SECOND
import kotlinx.time.LocalTime.Companion.SECONDS_PER_DAY
import kotlinx.time.LocalTime.Companion.SECONDS_PER_HOUR
import kotlinx.time.LocalTime.Companion.SECONDS_PER_MINUTE
import kotlinx.time.format.DateTimeParseException
import kotlin.test.*

/**
 * Test Duration.
 *
 * Based on OpenJDK 11's `tck/java/time/TCKDuration.java`.
 */
class TestDurationTCK {
    //-----------------------------------------------------------------------
    // zero
    //-----------------------------------------------------------------------
    @Test
    fun test_zero() {
        assertEquals(Duration.ZERO.seconds, 0L)
        assertEquals(Duration.ZERO.nanos, 0)
    }

    //-----------------------------------------------------------------------
    // ofSeconds(long)
    //-----------------------------------------------------------------------
    @Test
    fun factory_seconds_long() {
        for (i in -2L..2L) {
            val t = Duration.ofSeconds(i)
            assertEquals(t.seconds, i)
            assertEquals(t.nanos, 0)
        }
    }

    //-----------------------------------------------------------------------
    // ofSeconds(long,long)
    //-----------------------------------------------------------------------
    @Test
    fun factory_seconds_long_long() {
        for (i in -2L..2L) {
            for (j in 0L..9L) {
                val t = Duration.ofSeconds(i, j)
                assertEquals(t.seconds, i)
                assertEquals(t.nanos, j.toInt())
            }
            for (j in -10L..-1L) {
                val t = Duration.ofSeconds(i, j)
                assertEquals(t.seconds, i - 1)
                assertEquals(t.nanos, (j + 1000000000).toInt())
            }
            for (j in 999999990L..999999999L) {
                val t = Duration.ofSeconds(i, j)
                assertEquals(t.seconds, i)
                assertEquals(t.nanos, j.toInt())
            }
        }
    }

    @Test
    fun factory_seconds_long_long_nanosNegativeAdjusted() {
        val test = Duration.ofSeconds(2L, -1)
        assertEquals(test.seconds, 1)
        assertEquals(test.nanos, 999999999)
    }

    @Test
    fun factory_seconds_long_long_tooBig() {
        assertFailsWith<ArithmeticException> {
            Duration.ofSeconds(Long.MAX_VALUE, 1000000000)
        }
    }

    //-----------------------------------------------------------------------
    // ofMillis(long)
    //-----------------------------------------------------------------------

    private data class MillisDurationNoNanosTestCase(
        val millis: Long,
        val expectedSeconds: Long,
        val expectedNanoOfSecond: Int
    )

    private val millisDurationNoNanosTestCases = arrayOf(
        MillisDurationNoNanosTestCase(0, 0, 0),
        MillisDurationNoNanosTestCase(1, 0, 1000000),
        MillisDurationNoNanosTestCase(2, 0, 2000000),
        MillisDurationNoNanosTestCase(999, 0, 999000000),
        MillisDurationNoNanosTestCase(1000, 1, 0),
        MillisDurationNoNanosTestCase(1001, 1, 1000000),
        MillisDurationNoNanosTestCase(-1, -1, 999000000),
        MillisDurationNoNanosTestCase(-2, -1, 998000000),
        MillisDurationNoNanosTestCase(-999, -1, 1000000),
        MillisDurationNoNanosTestCase(-1000, -1, 0),
        MillisDurationNoNanosTestCase(-1001, -2, 999000000)
    )

    @Test
    fun factory_millis_long() {
        for (testCase in millisDurationNoNanosTestCases) {
            val (millis, expectedSeconds, expectedNanoOfSecond) = testCase
            val test = Duration.ofMillis(millis)
            assertEquals(test.seconds, expectedSeconds)
            assertEquals(test.nanos, expectedNanoOfSecond)
        }
    }

    //-----------------------------------------------------------------------
    // ofNanos(long)
    //-----------------------------------------------------------------------
    @Test
    fun factory_nanos_nanos() {
        val test = Duration.ofNanos(1)
        assertEquals(test.seconds, 0)
        assertEquals(test.nanos, 1)
    }

    @Test
    fun factory_nanos_nanosSecs() {
        val test = Duration.ofNanos(1000000002)
        assertEquals(test.seconds, 1)
        assertEquals(test.nanos, 2)
    }

    @Test
    fun factory_nanos_negative() {
        val test = Duration.ofNanos(-2000000001)
        assertEquals(test.seconds, -3)
        assertEquals(test.nanos, 999999999)
    }

    @Test
    fun factory_nanos_max() {
        val test = Duration.ofNanos(Long.MAX_VALUE)
        assertEquals(test.seconds, Long.MAX_VALUE / NANOS_PER_SECOND)
        assertEquals(test.nanos, (Long.MAX_VALUE % NANOS_PER_SECOND).toInt())
    }

    @Test
    fun factory_nanos_min() {
        val test = Duration.ofNanos(Long.MIN_VALUE)
        assertEquals(test.seconds, Long.MIN_VALUE / NANOS_PER_SECOND - 1)
        assertEquals(test.nanos, (Long.MIN_VALUE % NANOS_PER_SECOND + NANOS_PER_SECOND).toInt())
    }

    //-----------------------------------------------------------------------
    // ofMinutes()
    //-----------------------------------------------------------------------
    @Test
    fun factory_minutes() {
        val test = Duration.ofMinutes(2)
        assertEquals(test.seconds, 120)
        assertEquals(test.nanos, 0)
    }

    @Test
    fun factory_minutes_max() {
        val test = Duration.ofMinutes(Long.MAX_VALUE / SECONDS_PER_MINUTE)
        assertEquals(test.seconds, (Long.MAX_VALUE / SECONDS_PER_MINUTE) * SECONDS_PER_MINUTE)
        assertEquals(test.nanos, 0)
    }

    @Test
    fun factory_minutes_min() {
        val test = Duration.ofMinutes(Long.MIN_VALUE / SECONDS_PER_MINUTE)
        assertEquals(test.seconds, (Long.MIN_VALUE / SECONDS_PER_MINUTE) * SECONDS_PER_MINUTE)
        assertEquals(test.nanos, 0)
    }

    @Test
    fun factory_minutes_tooBig() {
        assertFailsWith<ArithmeticException> {
            Duration.ofMinutes(Long.MAX_VALUE / SECONDS_PER_MINUTE + 1)
        }
    }

    @Test
    fun factory_minutes_tooSmall() {
        assertFailsWith<ArithmeticException> {
            Duration.ofMinutes(Long.MIN_VALUE / SECONDS_PER_MINUTE - 1)
        }
    }

    //-----------------------------------------------------------------------
    // ofHours()
    //-----------------------------------------------------------------------
    @Test
    fun factory_hours() {
        val test = Duration.ofHours(2)
        assertEquals(test.seconds, 2L * SECONDS_PER_HOUR)
        assertEquals(test.nanos, 0)
    }

    @Test
    fun factory_hours_max() {
        val test = Duration.ofHours(Long.MAX_VALUE / SECONDS_PER_HOUR)
        assertEquals(test.seconds, (Long.MAX_VALUE / SECONDS_PER_HOUR) * SECONDS_PER_HOUR)
        assertEquals(test.nanos, 0)
    }

    @Test
    fun factory_hours_min() {
        val test = Duration.ofHours(Long.MIN_VALUE / SECONDS_PER_HOUR)
        assertEquals(test.seconds, (Long.MIN_VALUE / SECONDS_PER_HOUR) * SECONDS_PER_HOUR)
        assertEquals(test.nanos, 0)
    }

    @Test
    fun factory_hours_tooBig() {
        assertFailsWith<ArithmeticException> {
            Duration.ofHours(Long.MAX_VALUE / SECONDS_PER_HOUR + 1)
        }
    }

    @Test
    fun factory_hours_tooSmall() {
        assertFailsWith<ArithmeticException> {
            Duration.ofHours(Long.MIN_VALUE / SECONDS_PER_HOUR - 1)
        }
    }

    //-----------------------------------------------------------------------
    // ofDays()
    //-----------------------------------------------------------------------
    @Test
    fun factory_days() {
        val test = Duration.ofDays(2)
        assertEquals(test.seconds, 2L * SECONDS_PER_DAY)
        assertEquals(test.nanos, 0)
    }

    @Test
    fun factory_days_max() {
        val test = Duration.ofDays(Long.MAX_VALUE / SECONDS_PER_DAY)
        assertEquals(test.seconds, (Long.MAX_VALUE / SECONDS_PER_DAY) * SECONDS_PER_DAY)
        assertEquals(test.nanos, 0)
    }

    @Test
    fun factory_days_min() {
        val test = Duration.ofDays(Long.MIN_VALUE / SECONDS_PER_DAY)
        assertEquals(test.seconds, (Long.MIN_VALUE / SECONDS_PER_DAY) * SECONDS_PER_DAY)
        assertEquals(test.nanos, 0)
    }

    @Test
    fun factory_days_tooBig() {
        assertFailsWith<ArithmeticException> {
            Duration.ofDays(Long.MAX_VALUE / SECONDS_PER_DAY + 1)
        }
    }

    @Test
    fun factory_days_tooSmall() {
        assertFailsWith<ArithmeticException> {
            Duration.ofDays(Long.MIN_VALUE / SECONDS_PER_DAY - 1)
        }
    }

    //-----------------------------------------------------------------------
    // parse(String)
    //-----------------------------------------------------------------------

    private data class ParseSuccessTestCase(val text: String, val expectedSeconds: Long, val expectedNanoOfSecond: Int)

    private val parseSuccessTestCases = arrayOf(
        ParseSuccessTestCase("PT0S", 0, 0),
        ParseSuccessTestCase("PT1S", 1, 0),
        ParseSuccessTestCase("PT12S", 12, 0),
        ParseSuccessTestCase("PT123456789S", 123456789, 0),
        ParseSuccessTestCase("PT" + Long.MAX_VALUE + "S", Long.MAX_VALUE, 0),

        ParseSuccessTestCase("PT+1S", 1, 0),
        ParseSuccessTestCase("PT+12S", 12, 0),
        ParseSuccessTestCase("PT+123456789S", 123456789, 0),
        ParseSuccessTestCase("PT+" + Long.MAX_VALUE + "S", Long.MAX_VALUE, 0),

        ParseSuccessTestCase("PT-1S", -1, 0),
        ParseSuccessTestCase("PT-12S", -12, 0),
        ParseSuccessTestCase("PT-123456789S", -123456789, 0),
        ParseSuccessTestCase("PT" + Long.MIN_VALUE + "S", Long.MIN_VALUE, 0),


        ParseSuccessTestCase("PT0.1S", 0, 100000000),
        ParseSuccessTestCase("PT1.1S", 1, 100000000),
        ParseSuccessTestCase("PT1.12S", 1, 120000000),
        ParseSuccessTestCase("PT1.123S", 1, 123000000),
        ParseSuccessTestCase("PT1.1234S", 1, 123400000),
        ParseSuccessTestCase("PT1.12345S", 1, 123450000),
        ParseSuccessTestCase("PT1.123456S", 1, 123456000),
        ParseSuccessTestCase("PT1.1234567S", 1, 123456700),
        ParseSuccessTestCase("PT1.12345678S", 1, 123456780),
        ParseSuccessTestCase("PT1.123456789S", 1, 123456789),

        ParseSuccessTestCase("PT-0.1S", -1, 1000000000 - 100000000),
        ParseSuccessTestCase("PT-1.1S", -2, 1000000000 - 100000000),
        ParseSuccessTestCase("PT-1.12S", -2, 1000000000 - 120000000),
        ParseSuccessTestCase("PT-1.123S", -2, 1000000000 - 123000000),
        ParseSuccessTestCase("PT-1.1234S", -2, 1000000000 - 123400000),
        ParseSuccessTestCase("PT-1.12345S", -2, 1000000000 - 123450000),
        ParseSuccessTestCase("PT-1.123456S", -2, 1000000000 - 123456000),
        ParseSuccessTestCase("PT-1.1234567S", -2, 1000000000 - 123456700),
        ParseSuccessTestCase("PT-1.12345678S", -2, 1000000000 - 123456780),
        ParseSuccessTestCase("PT-1.123456789S", -2, 1000000000 - 123456789),

        ParseSuccessTestCase("PT" + Long.MAX_VALUE + ".123456789S", Long.MAX_VALUE, 123456789),
        ParseSuccessTestCase("PT" + Long.MIN_VALUE + ".000000000S", Long.MIN_VALUE, 0),

        ParseSuccessTestCase("PT12M", 12 * 60, 0),
        ParseSuccessTestCase("PT12M0.35S", 12 * 60, 350000000),
        ParseSuccessTestCase("PT12M1.35S", 12 * 60 + 1, 350000000),
        ParseSuccessTestCase("PT12M-0.35S", 12 * 60 - 1, 1000000000 - 350000000),
        ParseSuccessTestCase("PT12M-1.35S", 12 * 60 - 2, 1000000000 - 350000000),

        ParseSuccessTestCase("PT12H", 12 * 3600, 0),
        ParseSuccessTestCase("PT12H0.35S", 12 * 3600, 350000000),
        ParseSuccessTestCase("PT12H1.35S", 12 * 3600 + 1, 350000000),
        ParseSuccessTestCase("PT12H-0.35S", 12 * 3600 - 1, 1000000000 - 350000000),
        ParseSuccessTestCase("PT12H-1.35S", 12 * 3600 - 2, 1000000000 - 350000000),

        ParseSuccessTestCase("P12D", 12 * 24 * 3600, 0),
        ParseSuccessTestCase("P12DT0.35S", 12 * 24 * 3600, 350000000),
        ParseSuccessTestCase("P12DT1.35S", 12 * 24 * 3600 + 1, 350000000),
        ParseSuccessTestCase("P12DT-0.35S", 12 * 24 * 3600 - 1, 1000000000 - 350000000),
        ParseSuccessTestCase("P12DT-1.35S", 12 * 24 * 3600 - 2, 1000000000 - 350000000),

        ParseSuccessTestCase("PT01S", 1, 0),
        ParseSuccessTestCase("PT001S", 1, 0),
        ParseSuccessTestCase("PT000S", 0, 0),
        ParseSuccessTestCase("PT+01S", 1, 0),
        ParseSuccessTestCase("PT-01S", -1, 0),

        ParseSuccessTestCase("PT1.S", 1, 0),
        ParseSuccessTestCase("PT+1.S", 1, 0),
        ParseSuccessTestCase("PT-1.S", -1, 0),

        ParseSuccessTestCase("P0D", 0, 0),
        ParseSuccessTestCase("P0DT0H", 0, 0),
        ParseSuccessTestCase("P0DT0M", 0, 0),
        ParseSuccessTestCase("P0DT0S", 0, 0),
        ParseSuccessTestCase("P0DT0H0S", 0, 0),
        ParseSuccessTestCase("P0DT0M0S", 0, 0),
        ParseSuccessTestCase("P0DT0H0M0S", 0, 0),

        ParseSuccessTestCase("P1D", 86400, 0),
        ParseSuccessTestCase("P1DT0H", 86400, 0),
        ParseSuccessTestCase("P1DT0M", 86400, 0),
        ParseSuccessTestCase("P1DT0S", 86400, 0),
        ParseSuccessTestCase("P1DT0H0S", 86400, 0),
        ParseSuccessTestCase("P1DT0M0S", 86400, 0),
        ParseSuccessTestCase("P1DT0H0M0S", 86400, 0),

        ParseSuccessTestCase("P3D", 86400 * 3, 0),
        ParseSuccessTestCase("P3DT2H", 86400 * 3 + 3600 * 2, 0),
        ParseSuccessTestCase("P3DT2M", 86400 * 3 + 60 * 2, 0),
        ParseSuccessTestCase("P3DT2S", 86400 * 3 + 2, 0),
        ParseSuccessTestCase("P3DT2H1S", 86400 * 3 + 3600 * 2 + 1, 0),
        ParseSuccessTestCase("P3DT2M1S", 86400 * 3 + 60 * 2 + 1, 0),
        ParseSuccessTestCase("P3DT2H1M1S", 86400 * 3 + 3600 * 2 + 60 + 1, 0),

        ParseSuccessTestCase("P-3D", -86400 * 3, 0),
        ParseSuccessTestCase("P-3DT2H", -86400 * 3 + 3600 * 2, 0),
        ParseSuccessTestCase("P-3DT2M", -86400 * 3 + 60 * 2, 0),
        ParseSuccessTestCase("P-3DT2S", -86400 * 3 + 2, 0),
        ParseSuccessTestCase("P-3DT2H1S", -86400 * 3 + 3600 * 2 + 1, 0),
        ParseSuccessTestCase("P-3DT2M1S", -86400 * 3 + 60 * 2 + 1, 0),
        ParseSuccessTestCase("P-3DT2H1M1S", -86400 * 3 + 3600 * 2 + 60 + 1, 0),

        ParseSuccessTestCase("P-3DT-2H", -86400 * 3 - 3600 * 2, 0),
        ParseSuccessTestCase("P-3DT-2M", -86400 * 3 - 60 * 2, 0),
        ParseSuccessTestCase("P-3DT-2S", -86400 * 3 - 2, 0),
        ParseSuccessTestCase("P-3DT-2H1S", -86400 * 3 - 3600 * 2 + 1, 0),
        ParseSuccessTestCase("P-3DT-2M1S", -86400 * 3 - 60 * 2 + 1, 0),
        ParseSuccessTestCase("P-3DT-2H1M1S", -86400 * 3 - 3600 * 2 + 60 + 1, 0),

        ParseSuccessTestCase("PT0H", 0, 0),
        ParseSuccessTestCase("PT0H0M", 0, 0),
        ParseSuccessTestCase("PT0H0S", 0, 0),
        ParseSuccessTestCase("PT0H0M0S", 0, 0),

        ParseSuccessTestCase("PT1H", 3600, 0),
        ParseSuccessTestCase("PT3H", 3600 * 3, 0),
        ParseSuccessTestCase("PT-1H", -3600, 0),
        ParseSuccessTestCase("PT-3H", -3600 * 3, 0),

        ParseSuccessTestCase("PT2H5M", 3600 * 2 + 60 * 5, 0),
        ParseSuccessTestCase("PT2H5S", 3600 * 2 + 5, 0),
        ParseSuccessTestCase("PT2H5M8S", 3600 * 2 + 60 * 5 + 8, 0),
        ParseSuccessTestCase("PT-2H5M", -3600 * 2 + 60 * 5, 0),
        ParseSuccessTestCase("PT-2H5S", -3600 * 2 + 5, 0),
        ParseSuccessTestCase("PT-2H5M8S", -3600 * 2 + 60 * 5 + 8, 0),
        ParseSuccessTestCase("PT-2H-5M", -3600 * 2 - 60 * 5, 0),
        ParseSuccessTestCase("PT-2H-5S", -3600 * 2 - 5, 0),
        ParseSuccessTestCase("PT-2H-5M8S", -3600 * 2 - 60 * 5 + 8, 0),
        ParseSuccessTestCase("PT-2H-5M-8S", -3600 * 2 - 60 * 5 - 8, 0),

        ParseSuccessTestCase("PT0M", 0, 0),
        ParseSuccessTestCase("PT1M", 60, 0),
        ParseSuccessTestCase("PT3M", 60 * 3, 0),
        ParseSuccessTestCase("PT-1M", -60, 0),
        ParseSuccessTestCase("PT-3M", -60 * 3, 0),
        ParseSuccessTestCase("P0DT3M", 60 * 3, 0),
        ParseSuccessTestCase("P0DT-3M", -60 * 3, 0)
    )

    @Test
    fun factory_parse() {
        for (testCase in parseSuccessTestCases) {
            val (text, expectedSeconds, expectedNanoOfSecond) = testCase
            val test = Duration.parse(text)
            assertEquals(test.seconds, expectedSeconds)
            assertEquals(test.nanos, expectedNanoOfSecond)
        }
    }

    @Test
    fun factory_parse_plus() {
        for (testCase in parseSuccessTestCases) {
            val (text, expectedSeconds, expectedNanoOfSecond) = testCase
            val test = Duration.parse("+$text")
            assertEquals(test.seconds, expectedSeconds)
            assertEquals(test.nanos, expectedNanoOfSecond)
        }
    }

    @Ignore // TODO: kotlin.math
    @Test
    fun factory_parse_minus() {
        for (testCase in parseSuccessTestCases) {
            val (text, expectedSeconds, expectedNanoOfSecond) = testCase
            val test: Duration
            try {
                test = Duration.parse("-$text")
            } catch (ex: DateTimeParseException) {
                assertEquals(expectedSeconds == Long.MIN_VALUE, true)
                return
            }

            // Not inside try/catch or it breaks test.
            assertEquals(test, -Duration.ofSeconds(expectedSeconds, expectedNanoOfSecond.toLong()))
        }
    }

    @Test
    fun factory_parse_comma() {
        for (testCase in parseSuccessTestCases) {
            val (text, expectedSeconds, expectedNanoOfSecond) = testCase
            val test = Duration.parse(text.replace('.', ','))
            assertEquals(test.seconds, expectedSeconds)
            assertEquals(test.nanos, expectedNanoOfSecond)
        }
    }

    @Test
    fun factory_parse_lowerCase() {
        for (testCase in parseSuccessTestCases) {
            val (text, expectedSeconds, expectedNanoOfSecond) = testCase
            val test = Duration.parse(text.toLowerCase())
            assertEquals(test.seconds, expectedSeconds)
            assertEquals(test.nanos, expectedNanoOfSecond)
        }
    }

    private data class ParseFailureTestCase(val text: String)

    private val parseFailureTestCases = arrayOf(
        ParseFailureTestCase(""),
        ParseFailureTestCase("ABCDEF"),
        ParseFailureTestCase(" PT0S"),
        ParseFailureTestCase("PT0S "),

        ParseFailureTestCase("PTS"),
        ParseFailureTestCase("AT0S"),
        ParseFailureTestCase("PA0S"),
        ParseFailureTestCase("PT0A"),

        ParseFailureTestCase("P0Y"),
        ParseFailureTestCase("P1Y"),
        ParseFailureTestCase("P-2Y"),
        ParseFailureTestCase("P0M"),
        ParseFailureTestCase("P1M"),
        ParseFailureTestCase("P-2M"),
        ParseFailureTestCase("P3Y2D"),
        ParseFailureTestCase("P3M2D"),
        ParseFailureTestCase("P3W"),
        ParseFailureTestCase("P-3W"),
        ParseFailureTestCase("P2YT30S"),
        ParseFailureTestCase("P2MT30S"),

        ParseFailureTestCase("P1DT"),

        ParseFailureTestCase("PT+S"),
        ParseFailureTestCase("PT-S"),
        ParseFailureTestCase("PT.S"),
        ParseFailureTestCase("PTAS"),

        ParseFailureTestCase("PT-.S"),
        ParseFailureTestCase("PT+.S"),

        ParseFailureTestCase("PT1ABC2S"),
        ParseFailureTestCase("PT1.1ABC2S"),

        ParseFailureTestCase("PT123456789123456789123456789S"),
        ParseFailureTestCase("PT0.1234567891S"),

        ParseFailureTestCase("PT2.-3"),
        ParseFailureTestCase("PT-2.-3"),
        ParseFailureTestCase("PT2.+3"),
        ParseFailureTestCase("PT-2.+3")
    )

    @Test()
    fun factory_parseFailures() {
        for (testCase in parseFailureTestCases) {
            val (text) = testCase
            assertFailsWith<DateTimeParseException> {
                Duration.parse(text)
            }
        }
    }

    @Test
    fun factory_parseFailures_comma() {
        for (testCase in parseFailureTestCases) {
            val (text) = testCase
            assertFailsWith<DateTimeParseException> {
                Duration.parse(text.replace('.', ','))
            }
        }
    }

    @Test
    fun factory_parse_tooBig() {
        assertFailsWith<DateTimeParseException> {
            Duration.parse("PT" + Long.MAX_VALUE + "1S")
        }
    }

    @Test
    fun factory_parse_tooBig_decimal() {
        assertFailsWith<DateTimeParseException> {
            Duration.parse("PT" + Long.MAX_VALUE + "1.1S")
        }
    }

    @Test
    fun factory_parse_tooSmall() {
        assertFailsWith<DateTimeParseException> {
            Duration.parse("PT" + Long.MIN_VALUE + "1S")
        }
    }

    @Test
    fun factory_parse_tooSmall_decimal() {
        assertFailsWith<DateTimeParseException> {
            Duration.parse("PT" + Long.MIN_VALUE + ".1S")
        }
    }

    //-----------------------------------------------------------------------
    // isZero, isPositive(), isPositiveOrZero(), isNegative, isNegativeOrZero()
    //-----------------------------------------------------------------------
    @Test
    fun test_isZero() {
        assertEquals(Duration.ofNanos(0).isZero, true)
        assertEquals(Duration.ofSeconds(0).isZero, true)
        assertEquals(Duration.ofNanos(1).isZero, false)
        assertEquals(Duration.ofSeconds(1).isZero, false)
        assertEquals(Duration.ofSeconds(1, 1).isZero, false)
        assertEquals(Duration.ofNanos(-1).isZero, false)
        assertEquals(Duration.ofSeconds(-1).isZero, false)
        assertEquals(Duration.ofSeconds(-1, -1).isZero, false)
    }

    @Test
    fun test_isNegative() {
        assertEquals(Duration.ofNanos(0).isNegative, false)
        assertEquals(Duration.ofSeconds(0).isNegative, false)
        assertEquals(Duration.ofNanos(1).isNegative, false)
        assertEquals(Duration.ofSeconds(1).isNegative, false)
        assertEquals(Duration.ofSeconds(1, 1).isNegative, false)
        assertEquals(Duration.ofNanos(-1).isNegative, true)
        assertEquals(Duration.ofSeconds(-1).isNegative, true)
        assertEquals(Duration.ofSeconds(-1, -1).isNegative, true)
    }

    //-----------------------------------------------------------------------
    // plus()
    //-----------------------------------------------------------------------
    private data class PlusTestCase(
        val seconds: Long,
        val nanos: Long,
        val otherSeconds: Long,
        val otherNanos: Long,
        val expectedSeconds: Long,
        val expectedNanoOfSecond: Int
    )

    private val plusTestCases = arrayOf(
        PlusTestCase(Long.MIN_VALUE, 0, Long.MAX_VALUE, 0, -1, 0),

        PlusTestCase(-4, 666666667, -4, 666666667, -7, 333333334),
        PlusTestCase(-4, 666666667, -3, 0, -7, 666666667),
        PlusTestCase(-4, 666666667, -2, 0, -6, 666666667),
        PlusTestCase(-4, 666666667, -1, 0, -5, 666666667),
        PlusTestCase(-4, 666666667, -1, 333333334, -4, 1),
        PlusTestCase(-4, 666666667, -1, 666666667, -4, 333333334),
        PlusTestCase(-4, 666666667, -1, 999999999, -4, 666666666),
        PlusTestCase(-4, 666666667, 0, 0, -4, 666666667),
        PlusTestCase(-4, 666666667, 0, 1, -4, 666666668),
        PlusTestCase(-4, 666666667, 0, 333333333, -3, 0),
        PlusTestCase(-4, 666666667, 0, 666666666, -3, 333333333),
        PlusTestCase(-4, 666666667, 1, 0, -3, 666666667),
        PlusTestCase(-4, 666666667, 2, 0, -2, 666666667),
        PlusTestCase(-4, 666666667, 3, 0, -1, 666666667),
        PlusTestCase(-4, 666666667, 3, 333333333, 0, 0),

        PlusTestCase(-3, 0, -4, 666666667, -7, 666666667),
        PlusTestCase(-3, 0, -3, 0, -6, 0),
        PlusTestCase(-3, 0, -2, 0, -5, 0),
        PlusTestCase(-3, 0, -1, 0, -4, 0),
        PlusTestCase(-3, 0, -1, 333333334, -4, 333333334),
        PlusTestCase(-3, 0, -1, 666666667, -4, 666666667),
        PlusTestCase(-3, 0, -1, 999999999, -4, 999999999),
        PlusTestCase(-3, 0, 0, 0, -3, 0),
        PlusTestCase(-3, 0, 0, 1, -3, 1),
        PlusTestCase(-3, 0, 0, 333333333, -3, 333333333),
        PlusTestCase(-3, 0, 0, 666666666, -3, 666666666),
        PlusTestCase(-3, 0, 1, 0, -2, 0),
        PlusTestCase(-3, 0, 2, 0, -1, 0),
        PlusTestCase(-3, 0, 3, 0, 0, 0),
        PlusTestCase(-3, 0, 3, 333333333, 0, 333333333),

        PlusTestCase(-2, 0, -4, 666666667, -6, 666666667),
        PlusTestCase(-2, 0, -3, 0, -5, 0),
        PlusTestCase(-2, 0, -2, 0, -4, 0),
        PlusTestCase(-2, 0, -1, 0, -3, 0),
        PlusTestCase(-2, 0, -1, 333333334, -3, 333333334),
        PlusTestCase(-2, 0, -1, 666666667, -3, 666666667),
        PlusTestCase(-2, 0, -1, 999999999, -3, 999999999),
        PlusTestCase(-2, 0, 0, 0, -2, 0),
        PlusTestCase(-2, 0, 0, 1, -2, 1),
        PlusTestCase(-2, 0, 0, 333333333, -2, 333333333),
        PlusTestCase(-2, 0, 0, 666666666, -2, 666666666),
        PlusTestCase(-2, 0, 1, 0, -1, 0),
        PlusTestCase(-2, 0, 2, 0, 0, 0),
        PlusTestCase(-2, 0, 3, 0, 1, 0),
        PlusTestCase(-2, 0, 3, 333333333, 1, 333333333),

        PlusTestCase(-1, 0, -4, 666666667, -5, 666666667),
        PlusTestCase(-1, 0, -3, 0, -4, 0),
        PlusTestCase(-1, 0, -2, 0, -3, 0),
        PlusTestCase(-1, 0, -1, 0, -2, 0),
        PlusTestCase(-1, 0, -1, 333333334, -2, 333333334),
        PlusTestCase(-1, 0, -1, 666666667, -2, 666666667),
        PlusTestCase(-1, 0, -1, 999999999, -2, 999999999),
        PlusTestCase(-1, 0, 0, 0, -1, 0),
        PlusTestCase(-1, 0, 0, 1, -1, 1),
        PlusTestCase(-1, 0, 0, 333333333, -1, 333333333),
        PlusTestCase(-1, 0, 0, 666666666, -1, 666666666),
        PlusTestCase(-1, 0, 1, 0, 0, 0),
        PlusTestCase(-1, 0, 2, 0, 1, 0),
        PlusTestCase(-1, 0, 3, 0, 2, 0),
        PlusTestCase(-1, 0, 3, 333333333, 2, 333333333),

        PlusTestCase(-1, 666666667, -4, 666666667, -4, 333333334),
        PlusTestCase(-1, 666666667, -3, 0, -4, 666666667),
        PlusTestCase(-1, 666666667, -2, 0, -3, 666666667),
        PlusTestCase(-1, 666666667, -1, 0, -2, 666666667),
        PlusTestCase(-1, 666666667, -1, 333333334, -1, 1),
        PlusTestCase(-1, 666666667, -1, 666666667, -1, 333333334),
        PlusTestCase(-1, 666666667, -1, 999999999, -1, 666666666),
        PlusTestCase(-1, 666666667, 0, 0, -1, 666666667),
        PlusTestCase(-1, 666666667, 0, 1, -1, 666666668),
        PlusTestCase(-1, 666666667, 0, 333333333, 0, 0),
        PlusTestCase(-1, 666666667, 0, 666666666, 0, 333333333),
        PlusTestCase(-1, 666666667, 1, 0, 0, 666666667),
        PlusTestCase(-1, 666666667, 2, 0, 1, 666666667),
        PlusTestCase(-1, 666666667, 3, 0, 2, 666666667),
        PlusTestCase(-1, 666666667, 3, 333333333, 3, 0),

        PlusTestCase(0, 0, -4, 666666667, -4, 666666667),
        PlusTestCase(0, 0, -3, 0, -3, 0),
        PlusTestCase(0, 0, -2, 0, -2, 0),
        PlusTestCase(0, 0, -1, 0, -1, 0),
        PlusTestCase(0, 0, -1, 333333334, -1, 333333334),
        PlusTestCase(0, 0, -1, 666666667, -1, 666666667),
        PlusTestCase(0, 0, -1, 999999999, -1, 999999999),
        PlusTestCase(0, 0, 0, 0, 0, 0),
        PlusTestCase(0, 0, 0, 1, 0, 1),
        PlusTestCase(0, 0, 0, 333333333, 0, 333333333),
        PlusTestCase(0, 0, 0, 666666666, 0, 666666666),
        PlusTestCase(0, 0, 1, 0, 1, 0),
        PlusTestCase(0, 0, 2, 0, 2, 0),
        PlusTestCase(0, 0, 3, 0, 3, 0),
        PlusTestCase(0, 0, 3, 333333333, 3, 333333333),

        PlusTestCase(0, 333333333, -4, 666666667, -3, 0),
        PlusTestCase(0, 333333333, -3, 0, -3, 333333333),
        PlusTestCase(0, 333333333, -2, 0, -2, 333333333),
        PlusTestCase(0, 333333333, -1, 0, -1, 333333333),
        PlusTestCase(0, 333333333, -1, 333333334, -1, 666666667),
        PlusTestCase(0, 333333333, -1, 666666667, 0, 0),
        PlusTestCase(0, 333333333, -1, 999999999, 0, 333333332),
        PlusTestCase(0, 333333333, 0, 0, 0, 333333333),
        PlusTestCase(0, 333333333, 0, 1, 0, 333333334),
        PlusTestCase(0, 333333333, 0, 333333333, 0, 666666666),
        PlusTestCase(0, 333333333, 0, 666666666, 0, 999999999),
        PlusTestCase(0, 333333333, 1, 0, 1, 333333333),
        PlusTestCase(0, 333333333, 2, 0, 2, 333333333),
        PlusTestCase(0, 333333333, 3, 0, 3, 333333333),
        PlusTestCase(0, 333333333, 3, 333333333, 3, 666666666),

        PlusTestCase(1, 0, -4, 666666667, -3, 666666667),
        PlusTestCase(1, 0, -3, 0, -2, 0),
        PlusTestCase(1, 0, -2, 0, -1, 0),
        PlusTestCase(1, 0, -1, 0, 0, 0),
        PlusTestCase(1, 0, -1, 333333334, 0, 333333334),
        PlusTestCase(1, 0, -1, 666666667, 0, 666666667),
        PlusTestCase(1, 0, -1, 999999999, 0, 999999999),
        PlusTestCase(1, 0, 0, 0, 1, 0),
        PlusTestCase(1, 0, 0, 1, 1, 1),
        PlusTestCase(1, 0, 0, 333333333, 1, 333333333),
        PlusTestCase(1, 0, 0, 666666666, 1, 666666666),
        PlusTestCase(1, 0, 1, 0, 2, 0),
        PlusTestCase(1, 0, 2, 0, 3, 0),
        PlusTestCase(1, 0, 3, 0, 4, 0),
        PlusTestCase(1, 0, 3, 333333333, 4, 333333333),

        PlusTestCase(2, 0, -4, 666666667, -2, 666666667),
        PlusTestCase(2, 0, -3, 0, -1, 0),
        PlusTestCase(2, 0, -2, 0, 0, 0),
        PlusTestCase(2, 0, -1, 0, 1, 0),
        PlusTestCase(2, 0, -1, 333333334, 1, 333333334),
        PlusTestCase(2, 0, -1, 666666667, 1, 666666667),
        PlusTestCase(2, 0, -1, 999999999, 1, 999999999),
        PlusTestCase(2, 0, 0, 0, 2, 0),
        PlusTestCase(2, 0, 0, 1, 2, 1),
        PlusTestCase(2, 0, 0, 333333333, 2, 333333333),
        PlusTestCase(2, 0, 0, 666666666, 2, 666666666),
        PlusTestCase(2, 0, 1, 0, 3, 0),
        PlusTestCase(2, 0, 2, 0, 4, 0),
        PlusTestCase(2, 0, 3, 0, 5, 0),
        PlusTestCase(2, 0, 3, 333333333, 5, 333333333),

        PlusTestCase(3, 0, -4, 666666667, -1, 666666667),
        PlusTestCase(3, 0, -3, 0, 0, 0),
        PlusTestCase(3, 0, -2, 0, 1, 0),
        PlusTestCase(3, 0, -1, 0, 2, 0),
        PlusTestCase(3, 0, -1, 333333334, 2, 333333334),
        PlusTestCase(3, 0, -1, 666666667, 2, 666666667),
        PlusTestCase(3, 0, -1, 999999999, 2, 999999999),
        PlusTestCase(3, 0, 0, 0, 3, 0),
        PlusTestCase(3, 0, 0, 1, 3, 1),
        PlusTestCase(3, 0, 0, 333333333, 3, 333333333),
        PlusTestCase(3, 0, 0, 666666666, 3, 666666666),
        PlusTestCase(3, 0, 1, 0, 4, 0),
        PlusTestCase(3, 0, 2, 0, 5, 0),
        PlusTestCase(3, 0, 3, 0, 6, 0),
        PlusTestCase(3, 0, 3, 333333333, 6, 333333333),

        PlusTestCase(3, 333333333, -4, 666666667, 0, 0),
        PlusTestCase(3, 333333333, -3, 0, 0, 333333333),
        PlusTestCase(3, 333333333, -2, 0, 1, 333333333),
        PlusTestCase(3, 333333333, -1, 0, 2, 333333333),
        PlusTestCase(3, 333333333, -1, 333333334, 2, 666666667),
        PlusTestCase(3, 333333333, -1, 666666667, 3, 0),
        PlusTestCase(3, 333333333, -1, 999999999, 3, 333333332),
        PlusTestCase(3, 333333333, 0, 0, 3, 333333333),
        PlusTestCase(3, 333333333, 0, 1, 3, 333333334),
        PlusTestCase(3, 333333333, 0, 333333333, 3, 666666666),
        PlusTestCase(3, 333333333, 0, 666666666, 3, 999999999),
        PlusTestCase(3, 333333333, 1, 0, 4, 333333333),
        PlusTestCase(3, 333333333, 2, 0, 5, 333333333),
        PlusTestCase(3, 333333333, 3, 0, 6, 333333333),
        PlusTestCase(3, 333333333, 3, 333333333, 6, 666666666),

        PlusTestCase(Long.MAX_VALUE, 0, Long.MIN_VALUE, 0, -1, 0)
    )

    @Test
    fun plus() {
        for (testCase in plusTestCases) {
            val (seconds, nanos, otherSeconds, otherNanos, expectedSeconds, expectedNanoOfSecond) = testCase
            val t = Duration.ofSeconds(seconds, nanos).plus(Duration.ofSeconds(otherSeconds, otherNanos))
            assertEquals(t.seconds, expectedSeconds)
            assertEquals(t.nanos, expectedNanoOfSecond)
        }
    }

    @Test
    fun plusOverflowTooBig() {
        assertFailsWith<ArithmeticException> {
            val t = Duration.ofSeconds(Long.MAX_VALUE, 999999999)
            t.plus(Duration.ofSeconds(0, 1))
        }
    }

    @Test
    fun plusOverflowTooSmall() {
        assertFailsWith<ArithmeticException> {
            val t = Duration.ofSeconds(Long.MIN_VALUE)
            t.plus(Duration.ofSeconds(-1, 999999999))
        }
    }

    //-----------------------------------------------------------------------
    private data class PlusDaysTestCase(val days: Long, val amount: Long, val expectedDays: Long)

    private val plusDaysTestCases = arrayOf(
        PlusDaysTestCase(0, 0, 0),
        PlusDaysTestCase(0, 1, 1),
        PlusDaysTestCase(0, -1, -1),
        PlusDaysTestCase(Long.MAX_VALUE / 3600 / 24, 0, Long.MAX_VALUE / 3600 / 24),
        PlusDaysTestCase(Long.MIN_VALUE / 3600 / 24, 0, Long.MIN_VALUE / 3600 / 24),
        PlusDaysTestCase(1, 0, 1),
        PlusDaysTestCase(1, 1, 2),
        PlusDaysTestCase(1, -1, 0),
        PlusDaysTestCase(1, Long.MIN_VALUE / 3600 / 24, Long.MIN_VALUE / 3600 / 24 + 1),
        PlusDaysTestCase(1, 0, 1),
        PlusDaysTestCase(1, 1, 2),
        PlusDaysTestCase(1, -1, 0),
        PlusDaysTestCase(-1, 0, -1),
        PlusDaysTestCase(-1, 1, 0),
        PlusDaysTestCase(-1, -1, -2),
        PlusDaysTestCase(-1, Long.MAX_VALUE / 3600 / 24, Long.MAX_VALUE / 3600 / 24 - 1)
    )

    @Test
    fun plusDays_long() {
        for (testCase in plusDaysTestCases) {
            val (days, amount, expectedDays) = testCase
            var t = Duration.ofDays(days)
            t = t.plusDays(amount)
            assertEquals(t.toDays(), expectedDays)
        }
    }

    @Test
    fun plusDays_long_overflowTooBig() {
        assertFailsWith<ArithmeticException> {
            val t = Duration.ofDays(1)
            t.plusDays(Long.MAX_VALUE / 3600 / 24)
        }
    }

    @Test
    fun plusDays_long_overflowTooSmall() {
        assertFailsWith<ArithmeticException> {
            val t = Duration.ofDays(-1)
            t.plusDays(Long.MIN_VALUE / 3600 / 24)
        }
    }

    //-----------------------------------------------------------------------
    private data class PlusHoursTestCase(val hours: Long, val amount: Long, val expectedHours: Long)

    private val plusHoursTestCases = arrayOf(
        PlusHoursTestCase(0, 0, 0),
        PlusHoursTestCase(0, 1, 1),
        PlusHoursTestCase(0, -1, -1),
        PlusHoursTestCase(Long.MAX_VALUE / 3600, 0, Long.MAX_VALUE / 3600),
        PlusHoursTestCase(Long.MIN_VALUE / 3600, 0, Long.MIN_VALUE / 3600),
        PlusHoursTestCase(1, 0, 1),
        PlusHoursTestCase(1, 1, 2),
        PlusHoursTestCase(1, -1, 0),
        PlusHoursTestCase(1, Long.MIN_VALUE / 3600, Long.MIN_VALUE / 3600 + 1),
        PlusHoursTestCase(1, 0, 1),
        PlusHoursTestCase(1, 1, 2),
        PlusHoursTestCase(1, -1, 0),
        PlusHoursTestCase(-1, 0, -1),
        PlusHoursTestCase(-1, 1, 0),
        PlusHoursTestCase(-1, -1, -2),
        PlusHoursTestCase(-1, Long.MAX_VALUE / 3600, Long.MAX_VALUE / 3600 - 1)
    )

    @Test
    fun plusHours_long() {
        for (testCase in plusHoursTestCases) {
            val (hours, amount, expectedHours) = testCase
            var t = Duration.ofHours(hours)
            t = t.plusHours(amount)
            assertEquals(t.toHours(), expectedHours)
        }
    }

    @Test
    fun plusHours_long_overflowTooBig() {
        assertFailsWith<ArithmeticException> {
            val t = Duration.ofHours(1)
            t.plusHours(Long.MAX_VALUE / 3600)
        }
    }

    @Test
    fun plusHours_long_overflowTooSmall() {
        assertFailsWith<ArithmeticException> {
            val t = Duration.ofHours(-1)
            t.plusHours(Long.MIN_VALUE / 3600)
        }
    }

    //-----------------------------------------------------------------------
    private data class PlusMinutesTestCase(val minutes: Long, val amount: Long, val expectedMinutes: Long)

    private val plusMinutesTestCases = arrayOf(
        PlusMinutesTestCase(0, 0, 0),
        PlusMinutesTestCase(0, 1, 1),
        PlusMinutesTestCase(0, -1, -1),
        PlusMinutesTestCase(Long.MAX_VALUE / 60, 0, Long.MAX_VALUE / 60),
        PlusMinutesTestCase(Long.MIN_VALUE / 60, 0, Long.MIN_VALUE / 60),
        PlusMinutesTestCase(1, 0, 1),
        PlusMinutesTestCase(1, 1, 2),
        PlusMinutesTestCase(1, -1, 0),
        PlusMinutesTestCase(1, Long.MIN_VALUE / 60, Long.MIN_VALUE / 60 + 1),
        PlusMinutesTestCase(1, 0, 1),
        PlusMinutesTestCase(1, 1, 2),
        PlusMinutesTestCase(1, -1, 0),
        PlusMinutesTestCase(-1, 0, -1),
        PlusMinutesTestCase(-1, 1, 0),
        PlusMinutesTestCase(-1, -1, -2),
        PlusMinutesTestCase(-1, Long.MAX_VALUE / 60, Long.MAX_VALUE / 60 - 1)
    )

    @Test
    fun plusMinutes_long() {
        for (testCase in plusMinutesTestCases) {
            val (minutes, amount, expectedMinutes) = testCase
            var t = Duration.ofMinutes(minutes)
            t = t.plusMinutes(amount)
            assertEquals(t.toMinutes(), expectedMinutes)
        }
    }

    @Test
    fun plusMinutes_long_overflowTooBig() {
        assertFailsWith<ArithmeticException> {
            val t = Duration.ofMinutes(1)
            t.plusMinutes(Long.MAX_VALUE / 60)
        }
    }

    @Test
    fun plusMinutes_long_overflowTooSmall() {
        assertFailsWith<ArithmeticException> {
            val t = Duration.ofMinutes(-1)
            t.plusMinutes(Long.MIN_VALUE / 60)
        }
    }

    //-----------------------------------------------------------------------
    private data class PlusSecondsTestCase(
        val seconds: Long,
        val nanos: Long,
        val amount: Long,
        val expectedSeconds: Long,
        val expectedNanoOfSecond: Int
    )

    private val plusSecondsTestCases = arrayOf(
        PlusSecondsTestCase(0, 0, 0, 0, 0),
        PlusSecondsTestCase(0, 0, 1, 1, 0),
        PlusSecondsTestCase(0, 0, -1, -1, 0),
        PlusSecondsTestCase(0, 0, Long.MAX_VALUE, Long.MAX_VALUE, 0),
        PlusSecondsTestCase(0, 0, Long.MIN_VALUE, Long.MIN_VALUE, 0),
        PlusSecondsTestCase(1, 0, 0, 1, 0),
        PlusSecondsTestCase(1, 0, 1, 2, 0),
        PlusSecondsTestCase(1, 0, -1, 0, 0),
        PlusSecondsTestCase(1, 0, Long.MAX_VALUE - 1, Long.MAX_VALUE, 0),
        PlusSecondsTestCase(1, 0, Long.MIN_VALUE, Long.MIN_VALUE + 1, 0),
        PlusSecondsTestCase(1, 1, 0, 1, 1),
        PlusSecondsTestCase(1, 1, 1, 2, 1),
        PlusSecondsTestCase(1, 1, -1, 0, 1),
        PlusSecondsTestCase(1, 1, Long.MAX_VALUE - 1, Long.MAX_VALUE, 1),
        PlusSecondsTestCase(1, 1, Long.MIN_VALUE, Long.MIN_VALUE + 1, 1),
        PlusSecondsTestCase(-1, 1, 0, -1, 1),
        PlusSecondsTestCase(-1, 1, 1, 0, 1),
        PlusSecondsTestCase(-1, 1, -1, -2, 1),
        PlusSecondsTestCase(-1, 1, Long.MAX_VALUE, Long.MAX_VALUE - 1, 1),
        PlusSecondsTestCase(-1, 1, Long.MIN_VALUE + 1, Long.MIN_VALUE, 1)
    )

    @Test
    fun plusSeconds_long() {
        for (testCase in plusSecondsTestCases) {
            val (seconds, nanos, amount, expectedSeconds, expectedNanoOfSecond) = testCase
            var t = Duration.ofSeconds(seconds, nanos)
            t = t.plusSeconds(amount)
            assertEquals(t.seconds, expectedSeconds)
            assertEquals(t.nanos, expectedNanoOfSecond)
        }
    }

    @Test
    fun plusSeconds_long_overflowTooBig() {
        assertFailsWith<ArithmeticException> {
            val t = Duration.ofSeconds(1, 0)
            t.plusSeconds(Long.MAX_VALUE)
        }
    }

    @Test
    fun plusSeconds_long_overflowTooSmall() {
        assertFailsWith<ArithmeticException> {
            val t = Duration.ofSeconds(-1, 0)
            t.plusSeconds(Long.MIN_VALUE)
        }
    }

    //-----------------------------------------------------------------------
    private data class PlusMillisTestCase(
        val seconds: Long,
        val nanos: Long,
        val amount: Long,
        val expectedSeconds: Long,
        val expectedNanoOfSecond: Int
    )

    private val plusMillisTestCases = arrayOf(
        PlusMillisTestCase(0, 0, 0, 0, 0),
        PlusMillisTestCase(0, 0, 1, 0, 1000000),
        PlusMillisTestCase(0, 0, 999, 0, 999000000),
        PlusMillisTestCase(0, 0, 1000, 1, 0),
        PlusMillisTestCase(0, 0, 1001, 1, 1000000),
        PlusMillisTestCase(0, 0, 1999, 1, 999000000),
        PlusMillisTestCase(0, 0, 2000, 2, 0),
        PlusMillisTestCase(0, 0, -1, -1, 999000000),
        PlusMillisTestCase(0, 0, -999, -1, 1000000),
        PlusMillisTestCase(0, 0, -1000, -1, 0),
        PlusMillisTestCase(0, 0, -1001, -2, 999000000),
        PlusMillisTestCase(0, 0, -1999, -2, 1000000),

        PlusMillisTestCase(0, 1, 0, 0, 1),
        PlusMillisTestCase(0, 1, 1, 0, 1000001),
        PlusMillisTestCase(0, 1, 998, 0, 998000001),
        PlusMillisTestCase(0, 1, 999, 0, 999000001),
        PlusMillisTestCase(0, 1, 1000, 1, 1),
        PlusMillisTestCase(0, 1, 1998, 1, 998000001),
        PlusMillisTestCase(0, 1, 1999, 1, 999000001),
        PlusMillisTestCase(0, 1, 2000, 2, 1),
        PlusMillisTestCase(0, 1, -1, -1, 999000001),
        PlusMillisTestCase(0, 1, -2, -1, 998000001),
        PlusMillisTestCase(0, 1, -1000, -1, 1),
        PlusMillisTestCase(0, 1, -1001, -2, 999000001),

        PlusMillisTestCase(0, 1000000, 0, 0, 1000000),
        PlusMillisTestCase(0, 1000000, 1, 0, 2000000),
        PlusMillisTestCase(0, 1000000, 998, 0, 999000000),
        PlusMillisTestCase(0, 1000000, 999, 1, 0),
        PlusMillisTestCase(0, 1000000, 1000, 1, 1000000),
        PlusMillisTestCase(0, 1000000, 1998, 1, 999000000),
        PlusMillisTestCase(0, 1000000, 1999, 2, 0),
        PlusMillisTestCase(0, 1000000, 2000, 2, 1000000),
        PlusMillisTestCase(0, 1000000, -1, 0, 0),
        PlusMillisTestCase(0, 1000000, -2, -1, 999000000),
        PlusMillisTestCase(0, 1000000, -999, -1, 2000000),
        PlusMillisTestCase(0, 1000000, -1000, -1, 1000000),
        PlusMillisTestCase(0, 1000000, -1001, -1, 0),
        PlusMillisTestCase(0, 1000000, -1002, -2, 999000000),

        PlusMillisTestCase(0, 999999999, 0, 0, 999999999),
        PlusMillisTestCase(0, 999999999, 1, 1, 999999),
        PlusMillisTestCase(0, 999999999, 999, 1, 998999999),
        PlusMillisTestCase(0, 999999999, 1000, 1, 999999999),
        PlusMillisTestCase(0, 999999999, 1001, 2, 999999),
        PlusMillisTestCase(0, 999999999, -1, 0, 998999999),
        PlusMillisTestCase(0, 999999999, -1000, -1, 999999999),
        PlusMillisTestCase(0, 999999999, -1001, -1, 998999999)
    )

    @Test
    fun plusMillis_long() {
        for (testCase in plusMillisTestCases) {
            val (seconds, nanos, amount, expectedSeconds, expectedNanoOfSecond) = testCase
            var t = Duration.ofSeconds(seconds, nanos)
            t = t.plusMillis(amount)
            assertEquals(t.seconds, expectedSeconds)
            assertEquals(t.nanos, expectedNanoOfSecond)
        }
    }

    @Test
    fun plusMillis_long_oneMore() {
        for (testCase in plusMillisTestCases) {
            val (seconds, nanos, amount, expectedSeconds, expectedNanoOfSecond) = testCase
            var t = Duration.ofSeconds(seconds + 1, nanos)
            t = t.plusMillis(amount)
            assertEquals(t.seconds, expectedSeconds + 1)
            assertEquals(t.nanos, expectedNanoOfSecond)
        }
    }

    @Test
    fun plusMillis_long_minusOneLess() {
        for (testCase in plusMillisTestCases) {
            val (seconds, nanos, amount, expectedSeconds, expectedNanoOfSecond) = testCase
            var t = Duration.ofSeconds(seconds - 1, nanos)
            t = t.plusMillis(amount)
            assertEquals(t.seconds, expectedSeconds - 1)
            assertEquals(t.nanos, expectedNanoOfSecond)
        }
    }

    @Test
    fun plusMillis_long_max() {
        var t = Duration.ofSeconds(Long.MAX_VALUE, 998999999)
        t = t.plusMillis(1)
        assertEquals(t.seconds, Long.MAX_VALUE)
        assertEquals(t.nanos, 999999999)
    }

    @Test
    fun plusMillis_long_overflowTooBig() {
        assertFailsWith<ArithmeticException> {
            val t = Duration.ofSeconds(Long.MAX_VALUE, 999000000)
            t.plusMillis(1)
        }
    }

    @Test
    fun plusMillis_long_min() {
        var t = Duration.ofSeconds(Long.MIN_VALUE, 1000000)
        t = t.plusMillis(-1)
        assertEquals(t.seconds, Long.MIN_VALUE)
        assertEquals(t.nanos, 0)
    }

    @Test
    fun plusMillis_long_overflowTooSmall() {
        assertFailsWith<ArithmeticException> {
            val t = Duration.ofSeconds(Long.MIN_VALUE, 0)
            t.plusMillis(-1)
        }
    }

    //-----------------------------------------------------------------------
    private data class PlusNanosTestCase(
        val seconds: Long,
        val nanos: Long,
        val amount: Long,
        val expectedSeconds: Long,
        val expectedNanoOfSecond: Int
    )

    private val plusNanosTestCases = arrayOf(
        PlusNanosTestCase(0, 0, 0, 0, 0),
        PlusNanosTestCase(0, 0, 1, 0, 1),
        PlusNanosTestCase(0, 0, 999999999, 0, 999999999),
        PlusNanosTestCase(0, 0, 1000000000, 1, 0),
        PlusNanosTestCase(0, 0, 1000000001, 1, 1),
        PlusNanosTestCase(0, 0, 1999999999, 1, 999999999),
        PlusNanosTestCase(0, 0, 2000000000, 2, 0),
        PlusNanosTestCase(0, 0, -1, -1, 999999999),
        PlusNanosTestCase(0, 0, -999999999, -1, 1),
        PlusNanosTestCase(0, 0, -1000000000, -1, 0),
        PlusNanosTestCase(0, 0, -1000000001, -2, 999999999),
        PlusNanosTestCase(0, 0, -1999999999, -2, 1),

        PlusNanosTestCase(1, 0, 0, 1, 0),
        PlusNanosTestCase(1, 0, 1, 1, 1),
        PlusNanosTestCase(1, 0, 999999999, 1, 999999999),
        PlusNanosTestCase(1, 0, 1000000000, 2, 0),
        PlusNanosTestCase(1, 0, 1000000001, 2, 1),
        PlusNanosTestCase(1, 0, 1999999999, 2, 999999999),
        PlusNanosTestCase(1, 0, 2000000000, 3, 0),
        PlusNanosTestCase(1, 0, -1, 0, 999999999),
        PlusNanosTestCase(1, 0, -999999999, 0, 1),
        PlusNanosTestCase(1, 0, -1000000000, 0, 0),
        PlusNanosTestCase(1, 0, -1000000001, -1, 999999999),
        PlusNanosTestCase(1, 0, -1999999999, -1, 1),

        PlusNanosTestCase(-1, 0, 0, -1, 0),
        PlusNanosTestCase(-1, 0, 1, -1, 1),
        PlusNanosTestCase(-1, 0, 999999999, -1, 999999999),
        PlusNanosTestCase(-1, 0, 1000000000, 0, 0),
        PlusNanosTestCase(-1, 0, 1000000001, 0, 1),
        PlusNanosTestCase(-1, 0, 1999999999, 0, 999999999),
        PlusNanosTestCase(-1, 0, 2000000000, 1, 0),
        PlusNanosTestCase(-1, 0, -1, -2, 999999999),
        PlusNanosTestCase(-1, 0, -999999999, -2, 1),
        PlusNanosTestCase(-1, 0, -1000000000, -2, 0),
        PlusNanosTestCase(-1, 0, -1000000001, -3, 999999999),
        PlusNanosTestCase(-1, 0, -1999999999, -3, 1),

        PlusNanosTestCase(1, 1, 0, 1, 1),
        PlusNanosTestCase(1, 1, 1, 1, 2),
        PlusNanosTestCase(1, 1, 999999998, 1, 999999999),
        PlusNanosTestCase(1, 1, 999999999, 2, 0),
        PlusNanosTestCase(1, 1, 1000000000, 2, 1),
        PlusNanosTestCase(1, 1, 1999999998, 2, 999999999),
        PlusNanosTestCase(1, 1, 1999999999, 3, 0),
        PlusNanosTestCase(1, 1, 2000000000, 3, 1),
        PlusNanosTestCase(1, 1, -1, 1, 0),
        PlusNanosTestCase(1, 1, -2, 0, 999999999),
        PlusNanosTestCase(1, 1, -1000000000, 0, 1),
        PlusNanosTestCase(1, 1, -1000000001, 0, 0),
        PlusNanosTestCase(1, 1, -1000000002, -1, 999999999),
        PlusNanosTestCase(1, 1, -2000000000, -1, 1),

        PlusNanosTestCase(1, 999999999, 0, 1, 999999999),
        PlusNanosTestCase(1, 999999999, 1, 2, 0),
        PlusNanosTestCase(1, 999999999, 999999999, 2, 999999998),
        PlusNanosTestCase(1, 999999999, 1000000000, 2, 999999999),
        PlusNanosTestCase(1, 999999999, 1000000001, 3, 0),
        PlusNanosTestCase(1, 999999999, -1, 1, 999999998),
        PlusNanosTestCase(1, 999999999, -1000000000, 0, 999999999),
        PlusNanosTestCase(1, 999999999, -1000000001, 0, 999999998),
        PlusNanosTestCase(1, 999999999, -1999999999, 0, 0),
        PlusNanosTestCase(1, 999999999, -2000000000, -1, 999999999),

        PlusNanosTestCase(Long.MAX_VALUE, 0, 999999999, Long.MAX_VALUE, 999999999),
        PlusNanosTestCase(Long.MAX_VALUE - 1, 0, 1999999999, Long.MAX_VALUE, 999999999),
        PlusNanosTestCase(Long.MIN_VALUE, 1, -1, Long.MIN_VALUE, 0),
        PlusNanosTestCase(Long.MIN_VALUE + 1, 1, -1000000001, Long.MIN_VALUE, 0)
    )

    @Test
    fun plusNanos_long() {
        for (testCase in plusNanosTestCases) {
            val (seconds, nanos, amount, expectedSeconds, expectedNanoOfSecond) = testCase
            var t = Duration.ofSeconds(seconds, nanos)
            t = t.plusNanos(amount)
            assertEquals(t.seconds, expectedSeconds)
            assertEquals(t.nanos, expectedNanoOfSecond)
        }
    }

    @Test
    fun plusNanos_long_overflowTooBig() {
        assertFailsWith<ArithmeticException> {
            val t = Duration.ofSeconds(Long.MAX_VALUE, 999999999)
            t.plusNanos(1)
        }
    }

    @Test
    fun plusNanos_long_overflowTooSmall() {
        assertFailsWith<ArithmeticException> {
            val t = Duration.ofSeconds(Long.MIN_VALUE, 0)
            t.plusNanos(-1)
        }
    }

    //-----------------------------------------------------------------------
    private data class MinusTestCase(
        val seconds: Long,
        val nanos: Long,
        val otherSeconds: Long,
        val otherNanos: Long,
        val expectedSeconds: Long,
        val expectedNanoOfSecond: Int
    )

    private val minusTestCases = arrayOf(
        MinusTestCase(Long.MIN_VALUE, 0, Long.MIN_VALUE + 1, 0, -1, 0),

        MinusTestCase(-4, 666666667, -4, 666666667, 0, 0),
        MinusTestCase(-4, 666666667, -3, 0, -1, 666666667),
        MinusTestCase(-4, 666666667, -2, 0, -2, 666666667),
        MinusTestCase(-4, 666666667, -1, 0, -3, 666666667),
        MinusTestCase(-4, 666666667, -1, 333333334, -3, 333333333),
        MinusTestCase(-4, 666666667, -1, 666666667, -3, 0),
        MinusTestCase(-4, 666666667, -1, 999999999, -4, 666666668),
        MinusTestCase(-4, 666666667, 0, 0, -4, 666666667),
        MinusTestCase(-4, 666666667, 0, 1, -4, 666666666),
        MinusTestCase(-4, 666666667, 0, 333333333, -4, 333333334),
        MinusTestCase(-4, 666666667, 0, 666666666, -4, 1),
        MinusTestCase(-4, 666666667, 1, 0, -5, 666666667),
        MinusTestCase(-4, 666666667, 2, 0, -6, 666666667),
        MinusTestCase(-4, 666666667, 3, 0, -7, 666666667),
        MinusTestCase(-4, 666666667, 3, 333333333, -7, 333333334),

        MinusTestCase(-3, 0, -4, 666666667, 0, 333333333),
        MinusTestCase(-3, 0, -3, 0, 0, 0),
        MinusTestCase(-3, 0, -2, 0, -1, 0),
        MinusTestCase(-3, 0, -1, 0, -2, 0),
        MinusTestCase(-3, 0, -1, 333333334, -3, 666666666),
        MinusTestCase(-3, 0, -1, 666666667, -3, 333333333),
        MinusTestCase(-3, 0, -1, 999999999, -3, 1),
        MinusTestCase(-3, 0, 0, 0, -3, 0),
        MinusTestCase(-3, 0, 0, 1, -4, 999999999),
        MinusTestCase(-3, 0, 0, 333333333, -4, 666666667),
        MinusTestCase(-3, 0, 0, 666666666, -4, 333333334),
        MinusTestCase(-3, 0, 1, 0, -4, 0),
        MinusTestCase(-3, 0, 2, 0, -5, 0),
        MinusTestCase(-3, 0, 3, 0, -6, 0),
        MinusTestCase(-3, 0, 3, 333333333, -7, 666666667),

        MinusTestCase(-2, 0, -4, 666666667, 1, 333333333),
        MinusTestCase(-2, 0, -3, 0, 1, 0),
        MinusTestCase(-2, 0, -2, 0, 0, 0),
        MinusTestCase(-2, 0, -1, 0, -1, 0),
        MinusTestCase(-2, 0, -1, 333333334, -2, 666666666),
        MinusTestCase(-2, 0, -1, 666666667, -2, 333333333),
        MinusTestCase(-2, 0, -1, 999999999, -2, 1),
        MinusTestCase(-2, 0, 0, 0, -2, 0),
        MinusTestCase(-2, 0, 0, 1, -3, 999999999),
        MinusTestCase(-2, 0, 0, 333333333, -3, 666666667),
        MinusTestCase(-2, 0, 0, 666666666, -3, 333333334),
        MinusTestCase(-2, 0, 1, 0, -3, 0),
        MinusTestCase(-2, 0, 2, 0, -4, 0),
        MinusTestCase(-2, 0, 3, 0, -5, 0),
        MinusTestCase(-2, 0, 3, 333333333, -6, 666666667),

        MinusTestCase(-1, 0, -4, 666666667, 2, 333333333),
        MinusTestCase(-1, 0, -3, 0, 2, 0),
        MinusTestCase(-1, 0, -2, 0, 1, 0),
        MinusTestCase(-1, 0, -1, 0, 0, 0),
        MinusTestCase(-1, 0, -1, 333333334, -1, 666666666),
        MinusTestCase(-1, 0, -1, 666666667, -1, 333333333),
        MinusTestCase(-1, 0, -1, 999999999, -1, 1),
        MinusTestCase(-1, 0, 0, 0, -1, 0),
        MinusTestCase(-1, 0, 0, 1, -2, 999999999),
        MinusTestCase(-1, 0, 0, 333333333, -2, 666666667),
        MinusTestCase(-1, 0, 0, 666666666, -2, 333333334),
        MinusTestCase(-1, 0, 1, 0, -2, 0),
        MinusTestCase(-1, 0, 2, 0, -3, 0),
        MinusTestCase(-1, 0, 3, 0, -4, 0),
        MinusTestCase(-1, 0, 3, 333333333, -5, 666666667),

        MinusTestCase(-1, 666666667, -4, 666666667, 3, 0),
        MinusTestCase(-1, 666666667, -3, 0, 2, 666666667),
        MinusTestCase(-1, 666666667, -2, 0, 1, 666666667),
        MinusTestCase(-1, 666666667, -1, 0, 0, 666666667),
        MinusTestCase(-1, 666666667, -1, 333333334, 0, 333333333),
        MinusTestCase(-1, 666666667, -1, 666666667, 0, 0),
        MinusTestCase(-1, 666666667, -1, 999999999, -1, 666666668),
        MinusTestCase(-1, 666666667, 0, 0, -1, 666666667),
        MinusTestCase(-1, 666666667, 0, 1, -1, 666666666),
        MinusTestCase(-1, 666666667, 0, 333333333, -1, 333333334),
        MinusTestCase(-1, 666666667, 0, 666666666, -1, 1),
        MinusTestCase(-1, 666666667, 1, 0, -2, 666666667),
        MinusTestCase(-1, 666666667, 2, 0, -3, 666666667),
        MinusTestCase(-1, 666666667, 3, 0, -4, 666666667),
        MinusTestCase(-1, 666666667, 3, 333333333, -4, 333333334),

        MinusTestCase(0, 0, -4, 666666667, 3, 333333333),
        MinusTestCase(0, 0, -3, 0, 3, 0),
        MinusTestCase(0, 0, -2, 0, 2, 0),
        MinusTestCase(0, 0, -1, 0, 1, 0),
        MinusTestCase(0, 0, -1, 333333334, 0, 666666666),
        MinusTestCase(0, 0, -1, 666666667, 0, 333333333),
        MinusTestCase(0, 0, -1, 999999999, 0, 1),
        MinusTestCase(0, 0, 0, 0, 0, 0),
        MinusTestCase(0, 0, 0, 1, -1, 999999999),
        MinusTestCase(0, 0, 0, 333333333, -1, 666666667),
        MinusTestCase(0, 0, 0, 666666666, -1, 333333334),
        MinusTestCase(0, 0, 1, 0, -1, 0),
        MinusTestCase(0, 0, 2, 0, -2, 0),
        MinusTestCase(0, 0, 3, 0, -3, 0),
        MinusTestCase(0, 0, 3, 333333333, -4, 666666667),

        MinusTestCase(0, 333333333, -4, 666666667, 3, 666666666),
        MinusTestCase(0, 333333333, -3, 0, 3, 333333333),
        MinusTestCase(0, 333333333, -2, 0, 2, 333333333),
        MinusTestCase(0, 333333333, -1, 0, 1, 333333333),
        MinusTestCase(0, 333333333, -1, 333333334, 0, 999999999),
        MinusTestCase(0, 333333333, -1, 666666667, 0, 666666666),
        MinusTestCase(0, 333333333, -1, 999999999, 0, 333333334),
        MinusTestCase(0, 333333333, 0, 0, 0, 333333333),
        MinusTestCase(0, 333333333, 0, 1, 0, 333333332),
        MinusTestCase(0, 333333333, 0, 333333333, 0, 0),
        MinusTestCase(0, 333333333, 0, 666666666, -1, 666666667),
        MinusTestCase(0, 333333333, 1, 0, -1, 333333333),
        MinusTestCase(0, 333333333, 2, 0, -2, 333333333),
        MinusTestCase(0, 333333333, 3, 0, -3, 333333333),
        MinusTestCase(0, 333333333, 3, 333333333, -3, 0),

        MinusTestCase(1, 0, -4, 666666667, 4, 333333333),
        MinusTestCase(1, 0, -3, 0, 4, 0),
        MinusTestCase(1, 0, -2, 0, 3, 0),
        MinusTestCase(1, 0, -1, 0, 2, 0),
        MinusTestCase(1, 0, -1, 333333334, 1, 666666666),
        MinusTestCase(1, 0, -1, 666666667, 1, 333333333),
        MinusTestCase(1, 0, -1, 999999999, 1, 1),
        MinusTestCase(1, 0, 0, 0, 1, 0),
        MinusTestCase(1, 0, 0, 1, 0, 999999999),
        MinusTestCase(1, 0, 0, 333333333, 0, 666666667),
        MinusTestCase(1, 0, 0, 666666666, 0, 333333334),
        MinusTestCase(1, 0, 1, 0, 0, 0),
        MinusTestCase(1, 0, 2, 0, -1, 0),
        MinusTestCase(1, 0, 3, 0, -2, 0),
        MinusTestCase(1, 0, 3, 333333333, -3, 666666667),

        MinusTestCase(2, 0, -4, 666666667, 5, 333333333),
        MinusTestCase(2, 0, -3, 0, 5, 0),
        MinusTestCase(2, 0, -2, 0, 4, 0),
        MinusTestCase(2, 0, -1, 0, 3, 0),
        MinusTestCase(2, 0, -1, 333333334, 2, 666666666),
        MinusTestCase(2, 0, -1, 666666667, 2, 333333333),
        MinusTestCase(2, 0, -1, 999999999, 2, 1),
        MinusTestCase(2, 0, 0, 0, 2, 0),
        MinusTestCase(2, 0, 0, 1, 1, 999999999),
        MinusTestCase(2, 0, 0, 333333333, 1, 666666667),
        MinusTestCase(2, 0, 0, 666666666, 1, 333333334),
        MinusTestCase(2, 0, 1, 0, 1, 0),
        MinusTestCase(2, 0, 2, 0, 0, 0),
        MinusTestCase(2, 0, 3, 0, -1, 0),
        MinusTestCase(2, 0, 3, 333333333, -2, 666666667),

        MinusTestCase(3, 0, -4, 666666667, 6, 333333333),
        MinusTestCase(3, 0, -3, 0, 6, 0),
        MinusTestCase(3, 0, -2, 0, 5, 0),
        MinusTestCase(3, 0, -1, 0, 4, 0),
        MinusTestCase(3, 0, -1, 333333334, 3, 666666666),
        MinusTestCase(3, 0, -1, 666666667, 3, 333333333),
        MinusTestCase(3, 0, -1, 999999999, 3, 1),
        MinusTestCase(3, 0, 0, 0, 3, 0),
        MinusTestCase(3, 0, 0, 1, 2, 999999999),
        MinusTestCase(3, 0, 0, 333333333, 2, 666666667),
        MinusTestCase(3, 0, 0, 666666666, 2, 333333334),
        MinusTestCase(3, 0, 1, 0, 2, 0),
        MinusTestCase(3, 0, 2, 0, 1, 0),
        MinusTestCase(3, 0, 3, 0, 0, 0),
        MinusTestCase(3, 0, 3, 333333333, -1, 666666667),

        MinusTestCase(3, 333333333, -4, 666666667, 6, 666666666),
        MinusTestCase(3, 333333333, -3, 0, 6, 333333333),
        MinusTestCase(3, 333333333, -2, 0, 5, 333333333),
        MinusTestCase(3, 333333333, -1, 0, 4, 333333333),
        MinusTestCase(3, 333333333, -1, 333333334, 3, 999999999),
        MinusTestCase(3, 333333333, -1, 666666667, 3, 666666666),
        MinusTestCase(3, 333333333, -1, 999999999, 3, 333333334),
        MinusTestCase(3, 333333333, 0, 0, 3, 333333333),
        MinusTestCase(3, 333333333, 0, 1, 3, 333333332),
        MinusTestCase(3, 333333333, 0, 333333333, 3, 0),
        MinusTestCase(3, 333333333, 0, 666666666, 2, 666666667),
        MinusTestCase(3, 333333333, 1, 0, 2, 333333333),
        MinusTestCase(3, 333333333, 2, 0, 1, 333333333),
        MinusTestCase(3, 333333333, 3, 0, 0, 333333333),
        MinusTestCase(3, 333333333, 3, 333333333, 0, 0),

        MinusTestCase(Long.MAX_VALUE, 0, Long.MAX_VALUE, 0, 0, 0)
    )

    @Test
    fun minus() {
        for (testCase in minusTestCases) {
            val (seconds, nanos, otherSeconds, otherNanos, expectedSeconds, expectedNanoOfSecond) = testCase
            val t = Duration.ofSeconds(seconds, nanos).minus(Duration.ofSeconds(otherSeconds, otherNanos))
            assertEquals(t.seconds, expectedSeconds)
            assertEquals(t.nanos, expectedNanoOfSecond)
        }
    }

    @Test
    fun minusOverflowTooSmall() {
        assertFailsWith<ArithmeticException> {
            val t = Duration.ofSeconds(Long.MIN_VALUE)
            t.minus(Duration.ofSeconds(0, 1))
        }
    }

    @Test
    fun minusOverflowTooBig() {
        assertFailsWith<ArithmeticException> {
            val t = Duration.ofSeconds(Long.MAX_VALUE, 999999999)
            t.minus(Duration.ofSeconds(-1, 999999999))
        }
    }

    //-----------------------------------------------------------------------
    private data class MinusDaysTestCase(val days: Long, val amount: Long, val expectedDays: Long)

    private val minusDaysTestCases = arrayOf(
        MinusDaysTestCase(0, 0, 0),
        MinusDaysTestCase(0, 1, -1),
        MinusDaysTestCase(0, -1, 1),
        MinusDaysTestCase(Long.MAX_VALUE / 3600 / 24, 0, Long.MAX_VALUE / 3600 / 24),
        MinusDaysTestCase(Long.MIN_VALUE / 3600 / 24, 0, Long.MIN_VALUE / 3600 / 24),
        MinusDaysTestCase(1, 0, 1),
        MinusDaysTestCase(1, 1, 0),
        MinusDaysTestCase(1, -1, 2),
        MinusDaysTestCase(Long.MAX_VALUE / 3600 / 24, 1, Long.MAX_VALUE / 3600 / 24 - 1),
        MinusDaysTestCase(Long.MIN_VALUE / 3600 / 24, -1, Long.MIN_VALUE / 3600 / 24 + 1),
        MinusDaysTestCase(1, 0, 1),
        MinusDaysTestCase(1, 1, 0),
        MinusDaysTestCase(1, -1, 2),
        MinusDaysTestCase(-1, 0, -1),
        MinusDaysTestCase(-1, 1, -2),
        MinusDaysTestCase(-1, -1, 0)
    )

    @Test
    fun minusDays_long() {
        for (testCase in minusDaysTestCases) {
            val (days, amount, expectedDays) = testCase
            var t = Duration.ofDays(days)
            t = t.minusDays(amount)
            assertEquals(t.toDays(), expectedDays)
        }
    }

    @Test
    fun minusDays_long_overflowTooBig() {
        assertFailsWith<ArithmeticException> {
            val t = Duration.ofDays(Long.MAX_VALUE / 3600 / 24)
            t.minusDays(-1)
        }
    }

    @Test
    fun minusDays_long_overflowTooSmall() {
        assertFailsWith<ArithmeticException> {
            val t = Duration.ofDays(Long.MIN_VALUE / 3600 / 24)
            t.minusDays(1)
        }
    }

    //-----------------------------------------------------------------------
    private data class MinusHoursTestCase(val hours: Long, val amount: Long, val expectedHours: Long)

    private val minusHoursTestCases = arrayOf(
        MinusHoursTestCase(0, 0, 0),
        MinusHoursTestCase(0, 1, -1),
        MinusHoursTestCase(0, -1, 1),
        MinusHoursTestCase(Long.MAX_VALUE / 3600, 0, Long.MAX_VALUE / 3600),
        MinusHoursTestCase(Long.MIN_VALUE / 3600, 0, Long.MIN_VALUE / 3600),
        MinusHoursTestCase(1, 0, 1),
        MinusHoursTestCase(1, 1, 0),
        MinusHoursTestCase(1, -1, 2),
        MinusHoursTestCase(Long.MAX_VALUE / 3600, 1, Long.MAX_VALUE / 3600 - 1),
        MinusHoursTestCase(Long.MIN_VALUE / 3600, -1, Long.MIN_VALUE / 3600 + 1),
        MinusHoursTestCase(1, 0, 1),
        MinusHoursTestCase(1, 1, 0),
        MinusHoursTestCase(1, -1, 2),
        MinusHoursTestCase(-1, 0, -1),
        MinusHoursTestCase(-1, 1, -2),
        MinusHoursTestCase(-1, -1, 0)
    )

    @Test
    fun minusHours_long() {
        for (testCase in minusHoursTestCases) {
            val (hours, amount, expectedHours) = testCase
            var t = Duration.ofHours(hours)
            t = t.minusHours(amount)
            assertEquals(t.toHours(), expectedHours)
        }
    }

    @Test
    fun minusHours_long_overflowTooBig() {
        assertFailsWith<ArithmeticException> {
            val t = Duration.ofHours(Long.MAX_VALUE / 3600)
            t.minusHours(-1)
        }
    }

    @Test
    fun minusHours_long_overflowTooSmall() {
        assertFailsWith<ArithmeticException> {
            val t = Duration.ofHours(Long.MIN_VALUE / 3600)
            t.minusHours(1)
        }
    }

    //-----------------------------------------------------------------------
    private data class MinusMinutesTestCase(val minutes: Long, val amount: Long, val expectedMinutes: Long)

    private val minusMinutesTestCases = arrayOf(
        MinusMinutesTestCase(0, 0, 0),
        MinusMinutesTestCase(0, 1, -1),
        MinusMinutesTestCase(0, -1, 1),
        MinusMinutesTestCase(Long.MAX_VALUE / 60, 0, Long.MAX_VALUE / 60),
        MinusMinutesTestCase(Long.MIN_VALUE / 60, 0, Long.MIN_VALUE / 60),
        MinusMinutesTestCase(1, 0, 1),
        MinusMinutesTestCase(1, 1, 0),
        MinusMinutesTestCase(1, -1, 2),
        MinusMinutesTestCase(Long.MAX_VALUE / 60, 1, Long.MAX_VALUE / 60 - 1),
        MinusMinutesTestCase(Long.MIN_VALUE / 60, -1, Long.MIN_VALUE / 60 + 1),
        MinusMinutesTestCase(1, 0, 1),
        MinusMinutesTestCase(1, 1, 0),
        MinusMinutesTestCase(1, -1, 2),
        MinusMinutesTestCase(-1, 0, -1),
        MinusMinutesTestCase(-1, 1, -2),
        MinusMinutesTestCase(-1, -1, 0)
    )

    @Test
    fun minusMinutes_long() {
        for (testCase in minusMinutesTestCases) {
            val (minutes, amount, expectedMinutes) = testCase
            var t = Duration.ofMinutes(minutes)
            t = t.minusMinutes(amount)
            assertEquals(t.toMinutes(), expectedMinutes)
        }
    }

    @Test
    fun minusMinutes_long_overflowTooBig() {
        assertFailsWith<ArithmeticException> {
            val t = Duration.ofMinutes(Long.MAX_VALUE / 60)
            t.minusMinutes(-1)
        }
    }

    @Test
    fun minusMinutes_long_overflowTooSmall() {
        assertFailsWith<ArithmeticException> {
            val t = Duration.ofMinutes(Long.MIN_VALUE / 60)
            t.minusMinutes(1)
        }
    }

    //-----------------------------------------------------------------------
    private data class MinusSecondsTestCase(
        val seconds: Long,
        val nanos: Long,
        val amount: Long,
        val expectedSeconds: Long,
        val expectedNanoOfSecond: Int
    )

    private val minusSecondsTestCases = arrayOf(
        MinusSecondsTestCase(0, 0, 0, 0, 0),
        MinusSecondsTestCase(0, 0, 1, -1, 0),
        MinusSecondsTestCase(0, 0, -1, 1, 0),
        MinusSecondsTestCase(0, 0, Long.MAX_VALUE, -Long.MAX_VALUE, 0),
        MinusSecondsTestCase(0, 0, Long.MIN_VALUE + 1, Long.MAX_VALUE, 0),
        MinusSecondsTestCase(1, 0, 0, 1, 0),
        MinusSecondsTestCase(1, 0, 1, 0, 0),
        MinusSecondsTestCase(1, 0, -1, 2, 0),
        MinusSecondsTestCase(1, 0, Long.MAX_VALUE - 1, -Long.MAX_VALUE + 2, 0),
        MinusSecondsTestCase(1, 0, Long.MIN_VALUE + 2, Long.MAX_VALUE, 0),
        MinusSecondsTestCase(1, 1, 0, 1, 1),
        MinusSecondsTestCase(1, 1, 1, 0, 1),
        MinusSecondsTestCase(1, 1, -1, 2, 1),
        MinusSecondsTestCase(1, 1, Long.MAX_VALUE, -Long.MAX_VALUE + 1, 1),
        MinusSecondsTestCase(1, 1, Long.MIN_VALUE + 2, Long.MAX_VALUE, 1),
        MinusSecondsTestCase(-1, 1, 0, -1, 1),
        MinusSecondsTestCase(-1, 1, 1, -2, 1),
        MinusSecondsTestCase(-1, 1, -1, 0, 1),
        MinusSecondsTestCase(-1, 1, Long.MAX_VALUE, Long.MIN_VALUE, 1),
        MinusSecondsTestCase(-1, 1, Long.MIN_VALUE + 1, Long.MAX_VALUE - 1, 1)
    )

    @Test
    fun minusSeconds_long() {
        for (testCase in minusSecondsTestCases) {
            val (seconds, nanos, amount, expectedSeconds, expectedNanoOfSecond) = testCase
            var t = Duration.ofSeconds(seconds, nanos)
            t = t.minusSeconds(amount)
            assertEquals(t.seconds, expectedSeconds)
            assertEquals(t.nanos, expectedNanoOfSecond)
        }
    }

    @Test
    fun minusSeconds_long_overflowTooBig() {
        assertFailsWith<ArithmeticException> {
            val t = Duration.ofSeconds(1, 0)
            t.minusSeconds(Long.MIN_VALUE + 1)
        }
    }

    @Test
    fun minusSeconds_long_overflowTooSmall() {
        assertFailsWith<ArithmeticException> {
            val t = Duration.ofSeconds(-2, 0)
            t.minusSeconds(Long.MAX_VALUE)
        }
    }

    //-----------------------------------------------------------------------
    private data class MinusMillisTestCase(
        val seconds: Long,
        val nanos: Long,
        val amount: Long,
        val expectedSeconds: Long,
        val expectedNanoOfSecond: Int
    )

    private val minusMillisTestCases = arrayOf(
        MinusMillisTestCase(0, 0, 0, 0, 0),
        MinusMillisTestCase(0, 0, 1, -1, 999000000),
        MinusMillisTestCase(0, 0, 999, -1, 1000000),
        MinusMillisTestCase(0, 0, 1000, -1, 0),
        MinusMillisTestCase(0, 0, 1001, -2, 999000000),
        MinusMillisTestCase(0, 0, 1999, -2, 1000000),
        MinusMillisTestCase(0, 0, 2000, -2, 0),
        MinusMillisTestCase(0, 0, -1, 0, 1000000),
        MinusMillisTestCase(0, 0, -999, 0, 999000000),
        MinusMillisTestCase(0, 0, -1000, 1, 0),
        MinusMillisTestCase(0, 0, -1001, 1, 1000000),
        MinusMillisTestCase(0, 0, -1999, 1, 999000000),

        MinusMillisTestCase(0, 1, 0, 0, 1),
        MinusMillisTestCase(0, 1, 1, -1, 999000001),
        MinusMillisTestCase(0, 1, 998, -1, 2000001),
        MinusMillisTestCase(0, 1, 999, -1, 1000001),
        MinusMillisTestCase(0, 1, 1000, -1, 1),
        MinusMillisTestCase(0, 1, 1998, -2, 2000001),
        MinusMillisTestCase(0, 1, 1999, -2, 1000001),
        MinusMillisTestCase(0, 1, 2000, -2, 1),
        MinusMillisTestCase(0, 1, -1, 0, 1000001),
        MinusMillisTestCase(0, 1, -2, 0, 2000001),
        MinusMillisTestCase(0, 1, -1000, 1, 1),
        MinusMillisTestCase(0, 1, -1001, 1, 1000001),

        MinusMillisTestCase(0, 1000000, 0, 0, 1000000),
        MinusMillisTestCase(0, 1000000, 1, 0, 0),
        MinusMillisTestCase(0, 1000000, 998, -1, 3000000),
        MinusMillisTestCase(0, 1000000, 999, -1, 2000000),
        MinusMillisTestCase(0, 1000000, 1000, -1, 1000000),
        MinusMillisTestCase(0, 1000000, 1998, -2, 3000000),
        MinusMillisTestCase(0, 1000000, 1999, -2, 2000000),
        MinusMillisTestCase(0, 1000000, 2000, -2, 1000000),
        MinusMillisTestCase(0, 1000000, -1, 0, 2000000),
        MinusMillisTestCase(0, 1000000, -2, 0, 3000000),
        MinusMillisTestCase(0, 1000000, -999, 1, 0),
        MinusMillisTestCase(0, 1000000, -1000, 1, 1000000),
        MinusMillisTestCase(0, 1000000, -1001, 1, 2000000),
        MinusMillisTestCase(0, 1000000, -1002, 1, 3000000),

        MinusMillisTestCase(0, 999999999, 0, 0, 999999999),
        MinusMillisTestCase(0, 999999999, 1, 0, 998999999),
        MinusMillisTestCase(0, 999999999, 999, 0, 999999),
        MinusMillisTestCase(0, 999999999, 1000, -1, 999999999),
        MinusMillisTestCase(0, 999999999, 1001, -1, 998999999),
        MinusMillisTestCase(0, 999999999, -1, 1, 999999),
        MinusMillisTestCase(0, 999999999, -1000, 1, 999999999),
        MinusMillisTestCase(0, 999999999, -1001, 2, 999999)
    )

    @Test
    fun minusMillis_long() {
        for (testCase in minusMillisTestCases) {
            val (seconds, nanos, amount, expectedSeconds, expectedNanoOfSecond) = testCase
            var t = Duration.ofSeconds(seconds, nanos)
            t = t.minusMillis(amount)
            assertEquals(t.seconds, expectedSeconds)
            assertEquals(t.nanos, expectedNanoOfSecond)
        }
    }

    @Test
    fun minusMillis_long_oneMore() {
        for (testCase in minusMillisTestCases) {
            val (seconds, nanos, amount, expectedSeconds, expectedNanoOfSecond) = testCase
            var t = Duration.ofSeconds(seconds + 1, nanos)
            t = t.minusMillis(amount)
            assertEquals(t.seconds, expectedSeconds + 1)
            assertEquals(t.nanos, expectedNanoOfSecond)
        }
    }

    @Test
    fun minusMillis_long_minusOneLess() {
        for (testCase in minusMillisTestCases) {
            val (seconds, nanos, amount, expectedSeconds, expectedNanoOfSecond) = testCase
            var t = Duration.ofSeconds(seconds - 1, nanos)
            t = t.minusMillis(amount)
            assertEquals(t.seconds, expectedSeconds - 1)
            assertEquals(t.nanos, expectedNanoOfSecond)
        }
    }

    @Test
    fun minusMillis_long_max() {
        var t = Duration.ofSeconds(Long.MAX_VALUE, 998999999)
        t = t.minusMillis(-1)
        assertEquals(t.seconds, Long.MAX_VALUE)
        assertEquals(t.nanos, 999999999)
    }

    @Test
    fun minusMillis_long_overflowTooBig() {
        assertFailsWith<ArithmeticException> {
            val t = Duration.ofSeconds(Long.MAX_VALUE, 999000000)
            t.minusMillis(-1)
        }
    }

    @Test
    fun minusMillis_long_min() {
        var t = Duration.ofSeconds(Long.MIN_VALUE, 1000000)
        t = t.minusMillis(1)
        assertEquals(t.seconds, Long.MIN_VALUE)
        assertEquals(t.nanos, 0)
    }

    @Test
    fun minusMillis_long_overflowTooSmall() {
        assertFailsWith<ArithmeticException> {
            val t = Duration.ofSeconds(Long.MIN_VALUE, 0)
            t.minusMillis(1)
        }
    }

    //-----------------------------------------------------------------------
    private data class MinusNanosTestCase(
        val seconds: Long,
        val nanos: Long,
        val amount: Long,
        val expectedSeconds: Long,
        val expectedNanoOfSecond: Int
    )

    private val minusNanosTestCases = arrayOf(
        MinusNanosTestCase(0, 0, 0, 0, 0),
        MinusNanosTestCase(0, 0, 1, -1, 999999999),
        MinusNanosTestCase(0, 0, 999999999, -1, 1),
        MinusNanosTestCase(0, 0, 1000000000, -1, 0),
        MinusNanosTestCase(0, 0, 1000000001, -2, 999999999),
        MinusNanosTestCase(0, 0, 1999999999, -2, 1),
        MinusNanosTestCase(0, 0, 2000000000, -2, 0),
        MinusNanosTestCase(0, 0, -1, 0, 1),
        MinusNanosTestCase(0, 0, -999999999, 0, 999999999),
        MinusNanosTestCase(0, 0, -1000000000, 1, 0),
        MinusNanosTestCase(0, 0, -1000000001, 1, 1),
        MinusNanosTestCase(0, 0, -1999999999, 1, 999999999),

        MinusNanosTestCase(1, 0, 0, 1, 0),
        MinusNanosTestCase(1, 0, 1, 0, 999999999),
        MinusNanosTestCase(1, 0, 999999999, 0, 1),
        MinusNanosTestCase(1, 0, 1000000000, 0, 0),
        MinusNanosTestCase(1, 0, 1000000001, -1, 999999999),
        MinusNanosTestCase(1, 0, 1999999999, -1, 1),
        MinusNanosTestCase(1, 0, 2000000000, -1, 0),
        MinusNanosTestCase(1, 0, -1, 1, 1),
        MinusNanosTestCase(1, 0, -999999999, 1, 999999999),
        MinusNanosTestCase(1, 0, -1000000000, 2, 0),
        MinusNanosTestCase(1, 0, -1000000001, 2, 1),
        MinusNanosTestCase(1, 0, -1999999999, 2, 999999999),

        MinusNanosTestCase(-1, 0, 0, -1, 0),
        MinusNanosTestCase(-1, 0, 1, -2, 999999999),
        MinusNanosTestCase(-1, 0, 999999999, -2, 1),
        MinusNanosTestCase(-1, 0, 1000000000, -2, 0),
        MinusNanosTestCase(-1, 0, 1000000001, -3, 999999999),
        MinusNanosTestCase(-1, 0, 1999999999, -3, 1),
        MinusNanosTestCase(-1, 0, 2000000000, -3, 0),
        MinusNanosTestCase(-1, 0, -1, -1, 1),
        MinusNanosTestCase(-1, 0, -999999999, -1, 999999999),
        MinusNanosTestCase(-1, 0, -1000000000, 0, 0),
        MinusNanosTestCase(-1, 0, -1000000001, 0, 1),
        MinusNanosTestCase(-1, 0, -1999999999, 0, 999999999),

        MinusNanosTestCase(1, 1, 0, 1, 1),
        MinusNanosTestCase(1, 1, 1, 1, 0),
        MinusNanosTestCase(1, 1, 999999998, 0, 3),
        MinusNanosTestCase(1, 1, 999999999, 0, 2),
        MinusNanosTestCase(1, 1, 1000000000, 0, 1),
        MinusNanosTestCase(1, 1, 1999999998, -1, 3),
        MinusNanosTestCase(1, 1, 1999999999, -1, 2),
        MinusNanosTestCase(1, 1, 2000000000, -1, 1),
        MinusNanosTestCase(1, 1, -1, 1, 2),
        MinusNanosTestCase(1, 1, -2, 1, 3),
        MinusNanosTestCase(1, 1, -1000000000, 2, 1),
        MinusNanosTestCase(1, 1, -1000000001, 2, 2),
        MinusNanosTestCase(1, 1, -1000000002, 2, 3),
        MinusNanosTestCase(1, 1, -2000000000, 3, 1),

        MinusNanosTestCase(1, 999999999, 0, 1, 999999999),
        MinusNanosTestCase(1, 999999999, 1, 1, 999999998),
        MinusNanosTestCase(1, 999999999, 999999999, 1, 0),
        MinusNanosTestCase(1, 999999999, 1000000000, 0, 999999999),
        MinusNanosTestCase(1, 999999999, 1000000001, 0, 999999998),
        MinusNanosTestCase(1, 999999999, -1, 2, 0),
        MinusNanosTestCase(1, 999999999, -1000000000, 2, 999999999),
        MinusNanosTestCase(1, 999999999, -1000000001, 3, 0),
        MinusNanosTestCase(1, 999999999, -1999999999, 3, 999999998),
        MinusNanosTestCase(1, 999999999, -2000000000, 3, 999999999),

        MinusNanosTestCase(Long.MAX_VALUE, 0, -999999999, Long.MAX_VALUE, 999999999),
        MinusNanosTestCase(Long.MAX_VALUE - 1, 0, -1999999999, Long.MAX_VALUE, 999999999),
        MinusNanosTestCase(Long.MIN_VALUE, 1, 1, Long.MIN_VALUE, 0),
        MinusNanosTestCase(Long.MIN_VALUE + 1, 1, 1000000001, Long.MIN_VALUE, 0)
    )

    @Test
    fun minusNanos_long() {
        for (testCase in minusNanosTestCases) {
            val (seconds, nanos, amount, expectedSeconds, expectedNanoOfSecond) = testCase
            var t = Duration.ofSeconds(seconds, nanos)
            t = t.minusNanos(amount)
            assertEquals(t.seconds, expectedSeconds)
            assertEquals(t.nanos, expectedNanoOfSecond)
        }
    }

    @Test
    fun minusNanos_long_overflowTooBig() {
        assertFailsWith<ArithmeticException> {
            val t = Duration.ofSeconds(Long.MAX_VALUE, 999999999)
            t.minusNanos(-1)
        }
    }

    @Test
    fun minusNanos_long_overflowTooSmall() {
        assertFailsWith<ArithmeticException> {
            val t = Duration.ofSeconds(Long.MIN_VALUE, 0)
            t.minusNanos(1)
        }
    }

    //-----------------------------------------------------------------------
    // multipliedBy()
    //-----------------------------------------------------------------------
    private data class MultipliedByTestCase(
        val seconds: Long,
        val nanos: Long,
        val multiplicand: Long,
        val expectedSeconds: Long,
        val expectedNanos: Int
    )

    private val multipliedByTestCases = arrayOf(
        MultipliedByTestCase(-4, 666666667, -3, 9, 999999999),
        MultipliedByTestCase(-4, 666666667, -2, 6, 666666666),
        MultipliedByTestCase(-4, 666666667, -1, 3, 333333333),
        MultipliedByTestCase(-4, 666666667, 0, 0, 0),
        MultipliedByTestCase(-4, 666666667, 1, -4, 666666667),
        MultipliedByTestCase(-4, 666666667, 2, -7, 333333334),
        MultipliedByTestCase(-4, 666666667, 3, -10, 1),

        MultipliedByTestCase(-3, 0, -3, 9, 0),
        MultipliedByTestCase(-3, 0, -2, 6, 0),
        MultipliedByTestCase(-3, 0, -1, 3, 0),
        MultipliedByTestCase(-3, 0, 0, 0, 0),
        MultipliedByTestCase(-3, 0, 1, -3, 0),
        MultipliedByTestCase(-3, 0, 2, -6, 0),
        MultipliedByTestCase(-3, 0, 3, -9, 0),

        MultipliedByTestCase(-2, 0, -3, 6, 0),
        MultipliedByTestCase(-2, 0, -2, 4, 0),
        MultipliedByTestCase(-2, 0, -1, 2, 0),
        MultipliedByTestCase(-2, 0, 0, 0, 0),
        MultipliedByTestCase(-2, 0, 1, -2, 0),
        MultipliedByTestCase(-2, 0, 2, -4, 0),
        MultipliedByTestCase(-2, 0, 3, -6, 0),

        MultipliedByTestCase(-1, 0, -3, 3, 0),
        MultipliedByTestCase(-1, 0, -2, 2, 0),
        MultipliedByTestCase(-1, 0, -1, 1, 0),
        MultipliedByTestCase(-1, 0, 0, 0, 0),
        MultipliedByTestCase(-1, 0, 1, -1, 0),
        MultipliedByTestCase(-1, 0, 2, -2, 0),
        MultipliedByTestCase(-1, 0, 3, -3, 0),

        MultipliedByTestCase(-1, 500000000, -3, 1, 500000000),
        MultipliedByTestCase(-1, 500000000, -2, 1, 0),
        MultipliedByTestCase(-1, 500000000, -1, 0, 500000000),
        MultipliedByTestCase(-1, 500000000, 0, 0, 0),
        MultipliedByTestCase(-1, 500000000, 1, -1, 500000000),
        MultipliedByTestCase(-1, 500000000, 2, -1, 0),
        MultipliedByTestCase(-1, 500000000, 3, -2, 500000000),

        MultipliedByTestCase(0, 0, -3, 0, 0),
        MultipliedByTestCase(0, 0, -2, 0, 0),
        MultipliedByTestCase(0, 0, -1, 0, 0),
        MultipliedByTestCase(0, 0, 0, 0, 0),
        MultipliedByTestCase(0, 0, 1, 0, 0),
        MultipliedByTestCase(0, 0, 2, 0, 0),
        MultipliedByTestCase(0, 0, 3, 0, 0),

        MultipliedByTestCase(0, 500000000, -3, -2, 500000000),
        MultipliedByTestCase(0, 500000000, -2, -1, 0),
        MultipliedByTestCase(0, 500000000, -1, -1, 500000000),
        MultipliedByTestCase(0, 500000000, 0, 0, 0),
        MultipliedByTestCase(0, 500000000, 1, 0, 500000000),
        MultipliedByTestCase(0, 500000000, 2, 1, 0),
        MultipliedByTestCase(0, 500000000, 3, 1, 500000000),

        MultipliedByTestCase(1, 0, -3, -3, 0),
        MultipliedByTestCase(1, 0, -2, -2, 0),
        MultipliedByTestCase(1, 0, -1, -1, 0),
        MultipliedByTestCase(1, 0, 0, 0, 0),
        MultipliedByTestCase(1, 0, 1, 1, 0),
        MultipliedByTestCase(1, 0, 2, 2, 0),
        MultipliedByTestCase(1, 0, 3, 3, 0),

        MultipliedByTestCase(2, 0, -3, -6, 0),
        MultipliedByTestCase(2, 0, -2, -4, 0),
        MultipliedByTestCase(2, 0, -1, -2, 0),
        MultipliedByTestCase(2, 0, 0, 0, 0),
        MultipliedByTestCase(2, 0, 1, 2, 0),
        MultipliedByTestCase(2, 0, 2, 4, 0),
        MultipliedByTestCase(2, 0, 3, 6, 0),

        MultipliedByTestCase(3, 0, -3, -9, 0),
        MultipliedByTestCase(3, 0, -2, -6, 0),
        MultipliedByTestCase(3, 0, -1, -3, 0),
        MultipliedByTestCase(3, 0, 0, 0, 0),
        MultipliedByTestCase(3, 0, 1, 3, 0),
        MultipliedByTestCase(3, 0, 2, 6, 0),
        MultipliedByTestCase(3, 0, 3, 9, 0),

        MultipliedByTestCase(3, 333333333, -3, -10, 1),
        MultipliedByTestCase(3, 333333333, -2, -7, 333333334),
        MultipliedByTestCase(3, 333333333, -1, -4, 666666667),
        MultipliedByTestCase(3, 333333333, 0, 0, 0),
        MultipliedByTestCase(3, 333333333, 1, 3, 333333333),
        MultipliedByTestCase(3, 333333333, 2, 6, 666666666),
        MultipliedByTestCase(3, 333333333, 3, 9, 999999999)
    )

    @Ignore // TODO: kotlin.math
    @Test
    fun multipliedBy() {
        for (testCase in multipliedByTestCases) {
            val (seconds, nanos, multiplicand, expectedSeconds, expectedNanos) = testCase
            val t = Duration.ofSeconds(seconds, nanos) * multiplicand
            assertEquals(t.seconds, expectedSeconds)
            assertEquals(t.nanos, expectedNanos)
        }
    }

    @Ignore // TODO: kotlin.math
    @Test
    fun multipliedBy_max() {
        val test = Duration.ofSeconds(1)
        assertEquals(test * Long.MAX_VALUE, Duration.ofSeconds(Long.MAX_VALUE))
    }

    @Ignore // TODO: kotlin.math
    @Test
    fun multipliedBy_min() {
        val test = Duration.ofSeconds(1)
        assertEquals(test * Long.MIN_VALUE, Duration.ofSeconds(Long.MIN_VALUE))
    }

    @Ignore // TODO: kotlin.math
    @Test
    fun multipliedBy_tooBig() {
        assertFailsWith<ArithmeticException> {
            val test = Duration.ofSeconds(1, 1)
            test * Long.MAX_VALUE
        }
    }

    @Ignore // TODO: kotlin.math
    @Test
    fun multipliedBy_tooBig_negative() {
        assertFailsWith<ArithmeticException> {
            val test = Duration.ofSeconds(1, 1)
            test * Long.MIN_VALUE
        }
    }

    //-----------------------------------------------------------------------
    // negated()
    //-----------------------------------------------------------------------
    @Ignore // TODO: kotlin.math
    @Test
    fun test_negated() {
        assertEquals(-Duration.ofSeconds(0), Duration.ofSeconds(0))
        assertEquals(-Duration.ofSeconds(12), Duration.ofSeconds(-12))
        assertEquals(-Duration.ofSeconds(-12), Duration.ofSeconds(12))
        assertEquals(-Duration.ofSeconds(12, 20), Duration.ofSeconds(-12, -20))
        assertEquals(-Duration.ofSeconds(12, -20), Duration.ofSeconds(-12, 20))
        assertEquals(-Duration.ofSeconds(-12, -20), Duration.ofSeconds(12, 20))
        assertEquals(-Duration.ofSeconds(-12, 20), Duration.ofSeconds(12, -20))
        assertEquals(-Duration.ofSeconds(Long.MAX_VALUE), Duration.ofSeconds(-Long.MAX_VALUE))
    }

    @Ignore // TODO: kotlin.math
    @Test
    fun test_negated_overflow() {
        assertFailsWith<ArithmeticException> {
            -Duration.ofSeconds(Long.MIN_VALUE)
        }
    }

    //-----------------------------------------------------------------------
    // dividedBy()
    //-----------------------------------------------------------------------
    private data class DividedByTestCase(
        val seconds: Long,
        val nanos: Long,
        val divisor: Long,
        val expectedSeconds: Long,
        val expectedNanos: Int
    )

    private val dividedByTestCases = arrayOf(
        DividedByTestCase(-4, 666666667, -3, 1, 111111111),
        DividedByTestCase(-4, 666666667, -2, 1, 666666666),
        DividedByTestCase(-4, 666666667, -1, 3, 333333333),
        DividedByTestCase(-4, 666666667, 1, -4, 666666667),
        DividedByTestCase(-4, 666666667, 2, -2, 333333334),
        DividedByTestCase(-4, 666666667, 3, -2, 888888889),

        DividedByTestCase(-3, 0, -3, 1, 0),
        DividedByTestCase(-3, 0, -2, 1, 500000000),
        DividedByTestCase(-3, 0, -1, 3, 0),
        DividedByTestCase(-3, 0, 1, -3, 0),
        DividedByTestCase(-3, 0, 2, -2, 500000000),
        DividedByTestCase(-3, 0, 3, -1, 0),

        DividedByTestCase(-2, 0, -3, 0, 666666666),
        DividedByTestCase(-2, 0, -2, 1, 0),
        DividedByTestCase(-2, 0, -1, 2, 0),
        DividedByTestCase(-2, 0, 1, -2, 0),
        DividedByTestCase(-2, 0, 2, -1, 0),
        DividedByTestCase(-2, 0, 3, -1, 333333334),

        DividedByTestCase(-1, 0, -3, 0, 333333333),
        DividedByTestCase(-1, 0, -2, 0, 500000000),
        DividedByTestCase(-1, 0, -1, 1, 0),
        DividedByTestCase(-1, 0, 1, -1, 0),
        DividedByTestCase(-1, 0, 2, -1, 500000000),
        DividedByTestCase(-1, 0, 3, -1, 666666667),

        DividedByTestCase(-1, 500000000, -3, 0, 166666666),
        DividedByTestCase(-1, 500000000, -2, 0, 250000000),
        DividedByTestCase(-1, 500000000, -1, 0, 500000000),
        DividedByTestCase(-1, 500000000, 1, -1, 500000000),
        DividedByTestCase(-1, 500000000, 2, -1, 750000000),
        DividedByTestCase(-1, 500000000, 3, -1, 833333334),

        DividedByTestCase(0, 0, -3, 0, 0),
        DividedByTestCase(0, 0, -2, 0, 0),
        DividedByTestCase(0, 0, -1, 0, 0),
        DividedByTestCase(0, 0, 1, 0, 0),
        DividedByTestCase(0, 0, 2, 0, 0),
        DividedByTestCase(0, 0, 3, 0, 0),

        DividedByTestCase(0, 500000000, -3, -1, 833333334),
        DividedByTestCase(0, 500000000, -2, -1, 750000000),
        DividedByTestCase(0, 500000000, -1, -1, 500000000),
        DividedByTestCase(0, 500000000, 1, 0, 500000000),
        DividedByTestCase(0, 500000000, 2, 0, 250000000),
        DividedByTestCase(0, 500000000, 3, 0, 166666666),

        DividedByTestCase(1, 0, -3, -1, 666666667),
        DividedByTestCase(1, 0, -2, -1, 500000000),
        DividedByTestCase(1, 0, -1, -1, 0),
        DividedByTestCase(1, 0, 1, 1, 0),
        DividedByTestCase(1, 0, 2, 0, 500000000),
        DividedByTestCase(1, 0, 3, 0, 333333333),

        DividedByTestCase(2, 0, -3, -1, 333333334),
        DividedByTestCase(2, 0, -2, -1, 0),
        DividedByTestCase(2, 0, -1, -2, 0),
        DividedByTestCase(2, 0, 1, 2, 0),
        DividedByTestCase(2, 0, 2, 1, 0),
        DividedByTestCase(2, 0, 3, 0, 666666666),

        DividedByTestCase(3, 0, -3, -1, 0),
        DividedByTestCase(3, 0, -2, -2, 500000000),
        DividedByTestCase(3, 0, -1, -3, 0),
        DividedByTestCase(3, 0, 1, 3, 0),
        DividedByTestCase(3, 0, 2, 1, 500000000),
        DividedByTestCase(3, 0, 3, 1, 0),

        DividedByTestCase(3, 333333333, -3, -2, 888888889),
        DividedByTestCase(3, 333333333, -2, -2, 333333334),
        DividedByTestCase(3, 333333333, -1, -4, 666666667),
        DividedByTestCase(3, 333333333, 1, 3, 333333333),
        DividedByTestCase(3, 333333333, 2, 1, 666666666),
        DividedByTestCase(3, 333333333, 3, 1, 111111111)
    )

    @Ignore // TODO: kotlin.math
    @Test
    fun dividedBy() {
        for (testCase in dividedByTestCases) {
            val (seconds, nanos, divisor, expectedSeconds, expectedNanos) = testCase
            val t = Duration.ofSeconds(seconds, nanos) / divisor
            assertEquals(t.seconds, expectedSeconds)
            assertEquals(t.nanos, expectedNanos)
        }
    }

    @Ignore // TODO: kotlin.math
    @Test
    fun dividedByZero() {
        for (testCase in dividedByTestCases) {
            val (seconds, nanos, _, _, _) = testCase
            assertFailsWith<ArithmeticException> {
                Duration.ofSeconds(seconds, nanos) / 0
            }
        }
    }

    @Ignore // TODO: kotlin.math
    @Test
    fun dividedBy_max() {
        val test = Duration.ofSeconds(Long.MAX_VALUE)
        assertEquals(test / Long.MAX_VALUE, Duration.ofSeconds(1))
    }

    //-----------------------------------------------------------------------
    // dividedbyDur()
    //-----------------------------------------------------------------------

    private data class DividedByDurTestCase(val dividend: Duration, val divisor: Duration, val expected: Long)

    private val dividedByDurTestCases = arrayOf(
        DividedByDurTestCase(Duration.ofSeconds(0, 0), Duration.ofSeconds(1, 0), 0),
        DividedByDurTestCase(Duration.ofSeconds(1, 0), Duration.ofSeconds(1, 0), 1),
        DividedByDurTestCase(Duration.ofSeconds(6, 0), Duration.ofSeconds(3, 0), 2),
        DividedByDurTestCase(Duration.ofSeconds(3, 0), Duration.ofSeconds(6, 0), 0),
        DividedByDurTestCase(Duration.ofSeconds(7, 0), Duration.ofSeconds(3, 0), 2),

        DividedByDurTestCase(Duration.ofSeconds(0, 333333333), Duration.ofSeconds(0, 333333333), 1),
        DividedByDurTestCase(Duration.ofSeconds(0, 666666666), Duration.ofSeconds(0, 333333333), 2),
        DividedByDurTestCase(Duration.ofSeconds(0, 333333333), Duration.ofSeconds(0, 666666666), 0),
        DividedByDurTestCase(Duration.ofSeconds(0, 777777777), Duration.ofSeconds(0, 333333333), 2),

        DividedByDurTestCase(Duration.ofSeconds(-7, 0), Duration.ofSeconds(3, 0), -2),
        DividedByDurTestCase(Duration.ofSeconds(0, 7), Duration.ofSeconds(0, -3), -2),
        DividedByDurTestCase(Duration.ofSeconds(0, -777777777), Duration.ofSeconds(0, 333333333), -2),

        DividedByDurTestCase(Duration.ofSeconds(432000L, -777777777L), Duration.ofSeconds(14400L, 333333333L), 29),
        DividedByDurTestCase(Duration.ofSeconds(-432000L, 777777777L), Duration.ofSeconds(14400L, 333333333L), -29),
        DividedByDurTestCase(Duration.ofSeconds(-432000L, -777777777L), Duration.ofSeconds(14400L, 333333333L), -29),
        DividedByDurTestCase(Duration.ofSeconds(-432000L, -777777777L), Duration.ofSeconds(14400L, -333333333L), -30),
        DividedByDurTestCase(Duration.ofSeconds(432000L, -777777777L), Duration.ofSeconds(-14400L, 333333333L), -30),
        DividedByDurTestCase(Duration.ofSeconds(432000L, -777777777L), Duration.ofSeconds(-14400L, -333333333L), -29),
        DividedByDurTestCase(Duration.ofSeconds(-432000L, -777777777L), Duration.ofSeconds(-14400L, -333333333L), 29),

        DividedByDurTestCase(Duration.ofSeconds(Long.MAX_VALUE, 0), Duration.ofSeconds(1, 0), Long.MAX_VALUE),
        DividedByDurTestCase(Duration.ofSeconds(Long.MAX_VALUE, 0), Duration.ofSeconds(Long.MAX_VALUE, 0), 1)
    )

    @Ignore // TODO: kotlin.math
    @Test
    fun test_dividedByDur() {
        for (testCase in dividedByDurTestCases) {
            val (dividend, divisor, expected) = testCase
            assertEquals(dividend / divisor, expected)
        }
    }

    @Ignore // TODO: kotlin.math
    @Test
    fun test_dividedByDur_zero() {
        assertFailsWith<ArithmeticException> {
            Duration.ofSeconds(1, 0) / Duration.ZERO
        }
    }

    @Ignore // TODO: kotlin.math
    @Test
    fun test_dividedByDur_overflow() {
        assertFailsWith<ArithmeticException> {
            Duration.ofSeconds(Long.MAX_VALUE, 0) / Duration.ofNanos(1)
        }
    }

    //-----------------------------------------------------------------------
    // abs()
    //-----------------------------------------------------------------------
    @Ignore // TODO: kotlin.math
    @Test
    fun test_abs() {
        assertEquals(Duration.ofSeconds(0).abs(), Duration.ofSeconds(0))
        assertEquals(Duration.ofSeconds(12).abs(), Duration.ofSeconds(12))
        assertEquals(Duration.ofSeconds(-12).abs(), Duration.ofSeconds(12))
        assertEquals(Duration.ofSeconds(12, 20).abs(), Duration.ofSeconds(12, 20))
        assertEquals(Duration.ofSeconds(12, -20).abs(), Duration.ofSeconds(12, -20))
        assertEquals(Duration.ofSeconds(-12, -20).abs(), Duration.ofSeconds(12, 20))
        assertEquals(Duration.ofSeconds(-12, 20).abs(), Duration.ofSeconds(12, -20))
        assertEquals(Duration.ofSeconds(Long.MAX_VALUE).abs(), Duration.ofSeconds(Long.MAX_VALUE))
    }

    @Ignore // TODO: kotlin.math
    @Test
    fun test_abs_overflow() {
        assertFailsWith<ArithmeticException> {
            Duration.ofSeconds(Long.MIN_VALUE).abs()
        }
    }

    //-----------------------------------------------------------------------
    // toNanos()
    //-----------------------------------------------------------------------
    @Test
    fun test_toNanos() {
        assertEquals(Duration.ofSeconds(321, 123456789).toNanos(), 321123456789L)
        assertEquals(Duration.ofNanos(Long.MAX_VALUE).toNanos(), Long.MAX_VALUE)
        assertEquals(Duration.ofNanos(Long.MIN_VALUE).toNanos(), Long.MIN_VALUE)
    }

    @Test
    fun test_toNanos_max() {
        val test = Duration.ofSeconds(0, Long.MAX_VALUE)
        assertEquals(test.toNanos(), Long.MAX_VALUE)
    }

    @Test
    fun test_toNanos_tooBig() {
        assertFailsWith<ArithmeticException> {
            val test = Duration.ofSeconds(0, Long.MAX_VALUE).plusNanos(1)
            test.toNanos()
        }
    }

    @Test
    fun test_toNanos_min() {
        val test = Duration.ofSeconds(0, Long.MIN_VALUE)
        assertEquals(test.toNanos(), Long.MIN_VALUE)
    }

    @Test
    fun test_toNanos_tooSmall() {
        assertFailsWith<ArithmeticException> {
            val test = Duration.ofSeconds(0, Long.MIN_VALUE).minusNanos(1)
            test.toNanos()
        }
    }

    private data class ToNanosTestCase(val dur: Duration, val nanos: Long)

    private val toNanosPartTestCases = arrayOf(
        ToNanosTestCase(Duration.ofSeconds(365 * 86400 + 5 * 3600 + 48 * 60 + 46, 123456789), 123456789),
        ToNanosTestCase(Duration.ofSeconds(-365 * 86400 - 5 * 3600 - 48 * 60 - 46, -123456789), 876543211),
        ToNanosTestCase(Duration.ofSeconds(5 * 3600 + 48 * 60 + 46, 0), 0),
        ToNanosTestCase(Duration.ofNanos(123456789), 123456789),
        ToNanosTestCase(Duration.ofHours(2), 0),
        ToNanosTestCase(Duration.ofHours(-2), 0)
    )

    @Test
    fun test_toNanosPart() {
        for (testCase in toNanosPartTestCases) {
            val (dur, nanos) = testCase
            assertEquals(dur.toNanosPart(), nanos.toInt())
        }
    }

    //-----------------------------------------------------------------------
    // toMillis()
    //-----------------------------------------------------------------------
    @Test
    fun test_toMillis() {
        assertEquals(Duration.ofSeconds(321, 123456789).toMillis(), 321000 + 123)
        assertEquals(Duration.ofMillis(Long.MAX_VALUE).toMillis(), Long.MAX_VALUE)
        assertEquals(Duration.ofMillis(Long.MIN_VALUE).toMillis(), Long.MIN_VALUE)
    }

    @Test
    fun test_toMillis_max() {
        val test = Duration.ofSeconds(
            Long.MAX_VALUE / MILLIS_PER_SECOND, (Long.MAX_VALUE % MILLIS_PER_SECOND) * NANOS_PER_MILLI
        )
        assertEquals(test.toMillis(), Long.MAX_VALUE)
    }

    @Test
    fun test_toMillis_tooBig() {
        assertFailsWith<ArithmeticException> {
            val test = Duration.ofSeconds(
                Long.MAX_VALUE / MILLIS_PER_SECOND, (Long.MAX_VALUE % MILLIS_PER_SECOND + 1) * NANOS_PER_MILLI
            )
            test.toMillis()
        }
    }

    @Test
    fun test_toMillis_min() {
        val test = Duration.ofSeconds(
            Long.MIN_VALUE / MILLIS_PER_SECOND, (Long.MIN_VALUE % MILLIS_PER_SECOND) * NANOS_PER_MILLI
        )
        assertEquals(test.toMillis(), Long.MIN_VALUE)
    }

    @Test
    fun test_toMillis_tooSmall() {
        assertFailsWith<ArithmeticException> {
            val test = Duration.ofSeconds(
                Long.MIN_VALUE / MILLIS_PER_SECOND, (Long.MIN_VALUE % MILLIS_PER_SECOND - 1) * NANOS_PER_MILLI
            )
            test.toMillis()
        }
    }

    private data class ToMillisTestCase(val dur: Duration, val millis: Long)

    private val toMillisPartTestCases = arrayOf(
        ToMillisTestCase(Duration.ofSeconds(365 * 86400 + 5 * 3600 + 48 * 60 + 46, 123456789), 123),
        ToMillisTestCase(Duration.ofSeconds(-365 * 86400 - 5 * 3600 - 48 * 60 - 46, -123456789), 876),
        ToMillisTestCase(Duration.ofSeconds(5 * 3600 + 48 * 60 + 46, 0), 0),
        ToMillisTestCase(Duration.ofMillis(123), 123),
        ToMillisTestCase(Duration.ofHours(2), 0),
        ToMillisTestCase(Duration.ofHours(-2), 0)
    )

    @Test
    fun test_toMillisPart() {
        for (testCase in toMillisPartTestCases) {
            val (dur, millis) = testCase
            assertEquals(dur.toMillisPart(), millis.toInt())
        }
    }

    //-----------------------------------------------------------------------
    // toSeconds()
    //-----------------------------------------------------------------------
    private data class ToSecondsTestCase(val dur: Duration, val seconds: Long)

    private val toSecondsTestCases = arrayOf(
        ToSecondsTestCase(Duration.ofSeconds(365 * 86400 + 5 * 3600 + 48 * 60 + 46, 123456789), 31556926L),
        ToSecondsTestCase(Duration.ofSeconds(-365 * 86400 - 5 * 3600 - 48 * 60 - 46, -123456789), -31556927L),
        ToSecondsTestCase(Duration.ofSeconds(-365 * 86400 - 5 * 3600 - 48 * 60 - 46, 123456789), -31556926L),
        ToSecondsTestCase(Duration.ofSeconds(0), 0L),
        ToSecondsTestCase(Duration.ofSeconds(0, 123456789), 0L),
        ToSecondsTestCase(Duration.ofSeconds(0, -123456789), -1L),
        ToSecondsTestCase(Duration.ofSeconds(Long.MAX_VALUE), Long.MAX_VALUE),
        ToSecondsTestCase(Duration.ofSeconds(Long.MIN_VALUE), Long.MIN_VALUE)
    )

    @Test
    fun test_toSeconds() {
        for (testCase in toSecondsTestCases) {
            val (dur, seconds) = testCase
            assertEquals(dur.seconds, seconds)
        }
    }

    private val toSecondsPartTestCases = arrayOf(
        ToSecondsTestCase(Duration.ofSeconds(365 * 86400 + 5 * 3600 + 48 * 60 + 46, 123_456_789), 46),
        ToSecondsTestCase(Duration.ofSeconds(-365 * 86400 - 5 * 3600 - 48 * 60 - 46, -123_456_789), -47),
        ToSecondsTestCase(Duration.ofSeconds(0, 123_456_789), 0),
        ToSecondsTestCase(Duration.ofSeconds(46), 46),
        ToSecondsTestCase(Duration.ofHours(2), 0),
        ToSecondsTestCase(Duration.ofHours(-2), 0)
    )

    @Test
    fun test_toSecondsPart() {
        for (testCase in toSecondsPartTestCases) {
            val (dur, seconds) = testCase
            assertEquals(dur.toSecondsPart(), seconds.toInt())
        }
    }

    //-----------------------------------------------------------------------
    // toDays()
    //-----------------------------------------------------------------------
    private data class ToDaysTestCase(val dur: Duration, val days: Long)

    private val toDaysPartTestCases = arrayOf(
        ToDaysTestCase(Duration.ofSeconds(365 * 86400 + 5 * 3600 + 48 * 60 + 46, 123456789), 365L),
        ToDaysTestCase(Duration.ofSeconds(-365 * 86400 - 5 * 3600 - 48 * 60 - 46, -123456789), -365L),
        ToDaysTestCase(Duration.ofSeconds(5 * 3600 + 48 * 60 + 46, 123456789), 0L),
        ToDaysTestCase(Duration.ofDays(365), 365L),
        ToDaysTestCase(Duration.ofHours(2), 0L),
        ToDaysTestCase(Duration.ofHours(-2), 0L)
    )

    @Test
    fun test_toDaysPart() {
        for (testCase in toDaysPartTestCases) {
            val (dur, days) = testCase
            assertEquals(dur.toDaysPart(), days)
        }
    }

    //-----------------------------------------------------------------------
    // toHours()
    //-----------------------------------------------------------------------
    private data class ToHoursTestCase(val dur: Duration, val hours: Long)

    private val toHoursPartTestCases = arrayOf(
        ToHoursTestCase(Duration.ofSeconds(365 * 86400 + 5 * 3600 + 48 * 60 + 46, 123456789), 5),
        ToHoursTestCase(Duration.ofSeconds(-365 * 86400 - 5 * 3600 - 48 * 60 - 46, -123456789), -5),
        ToHoursTestCase(Duration.ofSeconds(48 * 60 + 46, 123456789), 0),
        ToHoursTestCase(Duration.ofHours(2), 2),
        ToHoursTestCase(Duration.ofHours(-2), -2)
    )

    @Test
    fun test_toHoursPart() {
        for (testCase in toHoursPartTestCases) {
            val (dur, hours) = testCase
            assertEquals(dur.toHoursPart(), hours.toInt())
        }
    }

    //-----------------------------------------------------------------------
    // toMinutes()
    //-----------------------------------------------------------------------
    private data class ToMinutesTestCase(val dur: Duration, val minutes: Long)

    private val toMinutesPartTestCases = arrayOf(
        ToMinutesTestCase(Duration.ofSeconds(365 * 86400 + 5 * 3600 + 48 * 60 + 46, 123456789), 48),
        ToMinutesTestCase(Duration.ofSeconds(-365 * 86400 - 5 * 3600 - 48 * 60 - 46, -123456789), -48),
        ToMinutesTestCase(Duration.ofSeconds(46, 123456789), 0),
        ToMinutesTestCase(Duration.ofMinutes(48), 48),
        ToMinutesTestCase(Duration.ofHours(2), 0),
        ToMinutesTestCase(Duration.ofHours(-2), 0)
    )

    @Test
    fun test_toMinutesPart() {
        for (testCase in toMinutesPartTestCases) {
            val (dur, minutes) = testCase
            assertEquals(dur.toMinutesPart(), minutes.toInt())
        }
    }

    //-----------------------------------------------------------------------
    // compareTo()
    //-----------------------------------------------------------------------
    @Test
    fun test_comparisons() {
        doTest_comparisons_Duration(
            Duration.ofSeconds(-2L, 0),
            Duration.ofSeconds(-2L, 999999998),
            Duration.ofSeconds(-2L, 999999999),
            Duration.ofSeconds(-1L, 0),
            Duration.ofSeconds(-1L, 1),
            Duration.ofSeconds(-1L, 999999998),
            Duration.ofSeconds(-1L, 999999999),
            Duration.ofSeconds(0L, 0),
            Duration.ofSeconds(0L, 1),
            Duration.ofSeconds(0L, 2),
            Duration.ofSeconds(0L, 999999999),
            Duration.ofSeconds(1L, 0),
            Duration.ofSeconds(2L, 0)
        )
    }

    private fun doTest_comparisons_Duration(vararg durations: Duration) {
        durations.forEachIndexed { i, a ->
            durations.forEachIndexed { j, b ->
                when {
                    i < j -> {
                        assertEquals(a < b, true, "$a <=> $b")
                        assertEquals(a == b, false, "$a <=> $b")
                    }
                    i > j -> {
                        assertEquals(a > b, true, "$a <=> ")
                        assertEquals(a == b, false, "$a <=> $b")
                    }
                    else -> {
                        assertEquals(a.compareTo(b), 0, "$a <=> $b")
                        assertEquals(a == b, true, "$a <=> $b")
                    }
                }
            }
        }
    }

    //-----------------------------------------------------------------------
    // equals()
    //-----------------------------------------------------------------------
    @Test
    fun test_equals() {
        val test5a = Duration.ofSeconds(5L, 20)
        val test5b = Duration.ofSeconds(5L, 20)
        val test5n = Duration.ofSeconds(5L, 30)
        val test6 = Duration.ofSeconds(6L, 20)

        assertEquals(test5a, test5a)
        assertEquals(test5a, test5b)
        assertNotEquals(test5a, test5n)
        assertNotEquals(test5a, test6)

        assertEquals(test5b, test5a)
        assertEquals(test5b, test5b)
        assertNotEquals(test5b, test5n)
        assertNotEquals(test5b, test6)

        assertNotEquals(test5n, test5a)
        assertNotEquals(test5n, test5b)
        assertEquals(test5n, test5n)
        assertNotEquals(test5n, test6)

        assertNotEquals(test6, test5a)
        assertNotEquals(test6, test5b)
        assertNotEquals(test6, test5n)
        assertEquals(test6, test6)
    }

    //-----------------------------------------------------------------------
    // hashCode()
    //-----------------------------------------------------------------------
    @Test
    fun test_hashCode() {
        val test5a = Duration.ofSeconds(5L, 20)
        val test5b = Duration.ofSeconds(5L, 20)
        val test5n = Duration.ofSeconds(5L, 30)
        val test6 = Duration.ofSeconds(6L, 20)

        assertEquals(test5a.hashCode(), test5a.hashCode())
        assertEquals(test5a.hashCode(), test5b.hashCode())
        assertEquals(test5b.hashCode(), test5b.hashCode())

        assertNotEquals(test5a.hashCode(), test5n.hashCode())
        assertNotEquals(test5a.hashCode(), test6.hashCode())
    }

    //-----------------------------------------------------------------------
    private data class WithNanosTestCase(
        val seconds: Long,
        val nanos: Long,
        val amount: Int,
        val expectedSeconds: Long,
        val exzpectedNanoOfSecond: Int
    )

    private val withNanosTestCases = arrayOf(
        WithNanosTestCase(0, 0, 0, 0, 0),
        WithNanosTestCase(0, 0, 1, 0, 1),
        WithNanosTestCase(0, 0, 999999999, 0, 999999999),

        WithNanosTestCase(1, 0, 0, 1, 0),
        WithNanosTestCase(1, 0, 1, 1, 1),
        WithNanosTestCase(1, 0, 999999999, 1, 999999999),

        WithNanosTestCase(-1, 0, 0, -1, 0),
        WithNanosTestCase(-1, 0, 1, -1, 1),
        WithNanosTestCase(-1, 0, 999999999, -1, 999999999),

        WithNanosTestCase(1, 999999999, 0, 1, 0),
        WithNanosTestCase(1, 999999999, 1, 1, 1),
        WithNanosTestCase(1, 999999998, 2, 1, 2),

        WithNanosTestCase(Long.MAX_VALUE, 0, 999999999, Long.MAX_VALUE, 999999999),
        WithNanosTestCase(Long.MIN_VALUE, 0, 999999999, Long.MIN_VALUE, 999999999)
    )

    @Test
    fun withNanos_long() {
        for (testCase in withNanosTestCases) {
            val (seconds, nanos, amount, expectedSeconds, expectedNanoOfSecond) = testCase
            var t = Duration.ofSeconds(seconds, nanos)
            t = t.withNanos(amount)
            assertEquals(t.seconds, expectedSeconds)
            assertEquals(t.nanos, expectedNanoOfSecond)
        }
    }

    //-----------------------------------------------------------------------
    private data class WithSecondsTestCase(
        val seconds: Long,
        val nanos: Long,
        val amount: Long,
        val expectedSeconds: Long,
        val exzpectedNanoOfSecond: Int
    )

    private val withSecondsTestCases = arrayOf(
        WithSecondsTestCase(0, 0, 0, 0, 0),
        WithSecondsTestCase(0, 0, 1, 1, 0),
        WithSecondsTestCase(0, 0, -1, -1, 0),
        WithSecondsTestCase(0, 0, Long.MAX_VALUE, Long.MAX_VALUE, 0),
        WithSecondsTestCase(0, 0, Long.MIN_VALUE, Long.MIN_VALUE, 0),

        WithSecondsTestCase(1, 0, 0, 0, 0),
        WithSecondsTestCase(1, 0, 2, 2, 0),
        WithSecondsTestCase(1, 0, -1, -1, 0),
        WithSecondsTestCase(1, 0, Long.MAX_VALUE, Long.MAX_VALUE, 0),
        WithSecondsTestCase(1, 0, Long.MIN_VALUE, Long.MIN_VALUE, 0),

        WithSecondsTestCase(-1, 1, 0, 0, 1),
        WithSecondsTestCase(-1, 1, 1, 1, 1),
        WithSecondsTestCase(-1, 1, -1, -1, 1),
        WithSecondsTestCase(-1, 1, Long.MAX_VALUE, Long.MAX_VALUE, 1),
        WithSecondsTestCase(-1, 1, Long.MIN_VALUE, Long.MIN_VALUE, 1)
    )

    @Test
    fun withSeconds_long() {
        for (testCase in withSecondsTestCases) {
            val (seconds, nanos, amount, expectedSeconds, expectedNanoOfSecond) = testCase
            var t = Duration.ofSeconds(seconds, nanos)
            t = t.withSeconds(amount)
            assertEquals(t.seconds, expectedSeconds)
            assertEquals(t.nanos, expectedNanoOfSecond)
        }
    }

    //-----------------------------------------------------------------------
    // toString()
    //-----------------------------------------------------------------------
    private data class ToStringTestCase(val seconds: Long, val nanos: Long, val expected: String)

    private val toStringTestCases = arrayOf(
        ToStringTestCase(0, 0, "PT0S"),
        ToStringTestCase(0, 1, "PT0.000000001S"),
        ToStringTestCase(0, 10, "PT0.00000001S"),
        ToStringTestCase(0, 100, "PT0.0000001S"),
        ToStringTestCase(0, 1000, "PT0.000001S"),
        ToStringTestCase(0, 10000, "PT0.00001S"),
        ToStringTestCase(0, 100000, "PT0.0001S"),
        ToStringTestCase(0, 1000000, "PT0.001S"),
        ToStringTestCase(0, 10000000, "PT0.01S"),
        ToStringTestCase(0, 100000000, "PT0.1S"),
        ToStringTestCase(0, 120000000, "PT0.12S"),
        ToStringTestCase(0, 123000000, "PT0.123S"),
        ToStringTestCase(0, 123400000, "PT0.1234S"),
        ToStringTestCase(0, 123450000, "PT0.12345S"),
        ToStringTestCase(0, 123456000, "PT0.123456S"),
        ToStringTestCase(0, 123456700, "PT0.1234567S"),
        ToStringTestCase(0, 123456780, "PT0.12345678S"),
        ToStringTestCase(0, 123456789, "PT0.123456789S"),
        ToStringTestCase(1, 0, "PT1S"),
        ToStringTestCase(59, 0, "PT59S"),
        ToStringTestCase(60, 0, "PT1M"),
        ToStringTestCase(61, 0, "PT1M1S"),
        ToStringTestCase(3599, 0, "PT59M59S"),
        ToStringTestCase(3600, 0, "PT1H"),
        ToStringTestCase(3601, 0, "PT1H1S"),
        ToStringTestCase(3661, 0, "PT1H1M1S"),
        ToStringTestCase(86399, 0, "PT23H59M59S"),
        ToStringTestCase(86400, 0, "PT24H"),
        ToStringTestCase(59, 0, "PT59S"),
        ToStringTestCase(59, 0, "PT59S"),
        ToStringTestCase(-1, 0, "PT-1S"),
        ToStringTestCase(-1, 1000, "PT-0.999999S"),
        ToStringTestCase(-1, 900000000, "PT-0.1S"),
        ToStringTestCase(-60, 100000000, "PT-59.9S"),
        ToStringTestCase(-59, -900000000, "PT-59.9S"),
        ToStringTestCase(-60, -100000000, "PT-1M-0.1S"),
        ToStringTestCase(
            Long.MAX_VALUE, 0, "PT${Long.MAX_VALUE / 3600}H${(Long.MAX_VALUE % 3600) / 60}M${Long.MAX_VALUE % 60}S"
        ),
        ToStringTestCase(
            Long.MIN_VALUE, 0, "PT${Long.MIN_VALUE / 3600}H${(Long.MIN_VALUE % 3600) / 60}M${Long.MIN_VALUE % 60}S"
        )
    )

    @Test
    fun test_toString() {
        for (testCase in toStringTestCases) {
            val (seconds, nanos, expected) = testCase
            val t = Duration.ofSeconds(seconds, nanos)
            assertEquals(t.toString(), expected)
        }
    }
}
