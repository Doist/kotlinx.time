/*
 * Copyright (c) 2019, Doist
 *
 * This code has been rewritten in Kotlin. It is redistributed under the terms
 * of the GNU General Public License version 2 license, as was the prior work.
 */

/*
 * Copyright (c) 2012, 2018, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
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

package kotlinx.time

import addExact
import floorDiv
import floorMod
import kotlinx.time.LocalTime.Companion.HOURS_PER_DAY
import kotlinx.time.LocalTime.Companion.MILLIS_PER_SECOND
import kotlinx.time.LocalTime.Companion.MINUTES_PER_HOUR
import kotlinx.time.LocalTime.Companion.NANOS_PER_MILLI
import kotlinx.time.LocalTime.Companion.NANOS_PER_SECOND
import kotlinx.time.LocalTime.Companion.SECONDS_PER_DAY
import kotlinx.time.LocalTime.Companion.SECONDS_PER_HOUR
import kotlinx.time.LocalTime.Companion.SECONDS_PER_MINUTE
import kotlinx.time.format.DateTimeParseException
import multiplyExact

/**
 * A time-based amount of time, such as '34.5 seconds'.
 *
 * This class is modelled after [`java.time.Duration`](https://docs.oracle.com/javase/10/docs/api/java/time/Duration.html).
 * See that class for additional documentation.
 */
class Duration private constructor(val seconds: Long, val nanos: Int) : Comparable<Duration> {
    companion object {
        val ZERO = Duration(0, 0)

        private val PATTERN = Regex(
            "([-+]?)P(?:([-+]?[0-9]+)D)?" +
                    "(T(?:([-+]?[0-9]+)H)?(?:([-+]?[0-9]+)M)?(?:([-+]?[0-9]+)(?:[.,]([0-9]{0,9}))?S)?)?",
            RegexOption.IGNORE_CASE
        )

        fun ofDays(days: Long): Duration = create(multiplyExact(days, SECONDS_PER_DAY), 0)

        fun ofHours(hours: Long): Duration = create(multiplyExact(hours, SECONDS_PER_HOUR), 0)

        fun ofMinutes(minutes: Long): Duration = create(multiplyExact(minutes, SECONDS_PER_MINUTE), 0)

        fun ofSeconds(seconds: Long): Duration = create(seconds, 0)

        fun ofSeconds(seconds: Long, nanoAdjustment: Long): Duration {
            val secs = addExact(seconds, floorDiv(nanoAdjustment, NANOS_PER_SECOND))
            val nos = floorMod(nanoAdjustment, NANOS_PER_SECOND).toInt()
            return create(secs, nos)
        }

        fun ofMillis(millis: Long): Duration {
            var secs = millis / MILLIS_PER_SECOND
            var mos = millis % MILLIS_PER_SECOND
            if (mos < 0) {
                mos += MILLIS_PER_SECOND
                secs--
            }
            return create(secs, (mos * NANOS_PER_MILLI).toInt())
        }

        fun ofNanos(nanos: Long): Duration {
            var secs = nanos / NANOS_PER_SECOND
            var nos = nanos % NANOS_PER_SECOND
            if (nos < 0) {
                nos += NANOS_PER_SECOND
                secs--
            }
            return create(secs, nos.toInt())
        }

        fun parse(text: CharSequence): Duration {
            val result = PATTERN.matchEntire(text)
            if (result != null) {
                // Check for letter T but no time sections.
                if (result.groupValues[3] != "T") {

                    val (negateMatch, dayMatch, _, hourMatch, minMatch, secMatch, fractionMatch) = result.destructured
                    if (dayMatch.isNotEmpty() || hourMatch.isNotEmpty()
                        || minMatch.isNotEmpty() || secMatch.isNotEmpty()
                    ) {
                        val negate = negateMatch == "-"
                        val daysAsSecs = parseNumber(text, dayMatch, SECONDS_PER_DAY, "days")
                        val hoursAsSecs = parseNumber(text, hourMatch, SECONDS_PER_HOUR, "hours")
                        val minsAsSecs = parseNumber(text, minMatch, SECONDS_PER_MINUTE, "minutes")
                        val seconds = parseNumber(text, secMatch, 1, "seconds")
                        val negativeSeconds = secMatch.getOrNull(0) == '-'
                        val nanos = parseFraction(text, fractionMatch, if (negativeSeconds) -1 else 1)
                        try {
                            return create(negate, daysAsSecs, hoursAsSecs, minsAsSecs, seconds, nanos)
                        } catch (ex: ArithmeticException) {
                            throw DateTimeParseException("Text cannot be parsed to a Duration: overflow", text, 0, ex)
                        }
                    }
                }
            }
            throw DateTimeParseException("Text cannot be parsed to a Duration", text, 0)
        }

        private fun parseNumber(text: CharSequence, parsed: String, multiplier: Int, errorText: String): Long {
            // regex limits to [-+]?[0-9]+
            if (parsed.isEmpty()) {
                return 0
            }
            try {
                val value = parsed.toLong()
                return multiplyExact(value, multiplier)
            } catch (ex: NumberFormatException) {
                throw DateTimeParseException("Text cannot be parsed to a Duration: $errorText", text, 0, ex)
            } catch (ex: ArithmeticException) {
                throw DateTimeParseException("Text cannot be parsed to a Duration: $errorText", text, 0, ex)
            }
        }

        private fun parseFraction(text: CharSequence, parsed: String, negate: Int): Int {
            // regex limits to [0-9]{0,9}
            if (parsed.isEmpty()) {
                return 0
            }
            try {
                val value = parsed.padEnd(9, '0')
                return value.toInt() * negate
            } catch (ex: NumberFormatException) {
                throw DateTimeParseException("Text cannot be parsed to a Duration: fraction", text, 0, ex)
            } catch (ex: ArithmeticException) {
                throw DateTimeParseException("Text cannot be parsed to a Duration: fraction", text, 0, ex)
            }

        }

        private fun create(
            negate: Boolean, daysAsSecs: Long, hoursAsSecs: Long, minsAsSecs: Long, secs: Long, nanos: Int
        ): Duration {
            val seconds = addExact(daysAsSecs, addExact(hoursAsSecs, addExact(minsAsSecs, secs)))
            return if (negate) {
                -ofSeconds(seconds, nanos.toLong())
            } else {
                ofSeconds(seconds, nanos.toLong())
            }
        }

        fun create(seconds: Long, nanoAdjustment: Int): Duration {
            if (seconds == 0L && nanoAdjustment == 0) {
                return ZERO
            }
            return Duration(seconds, nanoAdjustment)
        }
    }

    val isZero get() = (seconds or nanos.toLong()) == 0L

    val isNegative get() = seconds < 0

    fun withSeconds(seconds: Long) = create(seconds, nanos)

    fun withNanos(nanosOfSecond: Int) = create(seconds, nanosOfSecond)

    operator fun plus(duration: Duration) = plus(duration.seconds, duration.nanos.toLong())

    fun plusDays(daysToAdd: Long) = plus(multiplyExact(daysToAdd, SECONDS_PER_DAY), 0)

    fun plusHours(hoursToAdd: Long) = plus(multiplyExact(hoursToAdd, SECONDS_PER_HOUR), 0)

    fun plusMinutes(minutesToAdd: Long) = plus(multiplyExact(minutesToAdd, SECONDS_PER_MINUTE), 0)

    fun plusSeconds(secondsToAdd: Long) = plus(secondsToAdd, 0)

    fun plusMillis(millisToAdd: Long) =
        plus(millisToAdd / MILLIS_PER_SECOND, (millisToAdd % MILLIS_PER_SECOND) * NANOS_PER_MILLI)

    fun plusNanos(nanosToAdd: Long) = plus(0L, nanosToAdd)

    fun plus(secondsToAdd: Long, nanosToAdd: Long): Duration {
        if (secondsToAdd == 0L && nanosToAdd == 0L) {
            return this
        }
        val epochSec = addExact(addExact(seconds, secondsToAdd), nanosToAdd / NANOS_PER_SECOND)
        val nanoAdjustment = nanos + (nanosToAdd % NANOS_PER_SECOND) // safe int + NANOS_PER_SECOND
        return ofSeconds(epochSec, nanoAdjustment)
    }

    operator fun minus(duration: Duration): Duration {
        val secsToSubtract = duration.seconds
        val nanosToSubtract = duration.nanos
        return if (secsToSubtract == Long.MIN_VALUE) {
            plus(Long.MAX_VALUE, -nanosToSubtract.toLong()).plus(1, 0)
        } else {
            plus(-secsToSubtract, -nanosToSubtract.toLong())
        }
    }

    fun minusDays(daysToSubtract: Long): Duration {
        return if (daysToSubtract == Long.MIN_VALUE) {
            plusDays(Long.MAX_VALUE).plusDays(1L)
        } else {
            plusDays(-daysToSubtract)
        }
    }

    fun minusHours(hoursToSubtract: Long): Duration {
        return if (hoursToSubtract == Long.MIN_VALUE) {
            plusHours(Long.MAX_VALUE).plusHours(1L)
        } else {
            plusHours(-hoursToSubtract)
        }
    }

    fun minusMinutes(minutesToSubtract: Long): Duration {
        return if (minutesToSubtract == Long.MIN_VALUE) {
            plusMinutes(Long.MAX_VALUE).plusMinutes(1L)
        } else {
            plusMinutes(-minutesToSubtract)
        }
    }

    fun minusSeconds(secondsToSubtract: Long): Duration {
        return if (secondsToSubtract == Long.MIN_VALUE) {
            plusSeconds(Long.MAX_VALUE).plusSeconds(1L)
        } else {
            plusSeconds(-secondsToSubtract)
        }
    }

    fun minusMillis(millisToSubtract: Long): Duration {
        return if (millisToSubtract == Long.MIN_VALUE) {
            plusMillis(Long.MAX_VALUE).plusMillis(1L)
        } else {
            plusMillis(-millisToSubtract)
        }
    }

    fun minusNanos(nanosToSubtract: Long): Duration {
        return if (nanosToSubtract == Long.MIN_VALUE) {
            plusNanos(Long.MAX_VALUE).plusNanos(1)
        } else {
            plusNanos(-nanosToSubtract)
        }
    }

    /** See [`java.time.Duration#multipliedBy(long)](https://docs.oracle.com/javase/10/docs/api/java/time/Duration.html#multipliedBy(long)) */
    operator fun times(multiplicand: Long): Duration {
        // TODO: kotlinx.math
        throw NotImplementedError("Pending kotlin.math: multipliedBy($multiplicand)")
    }

    /** See [`java.time.Duration#dividedBy(long)](https://docs.oracle.com/javase/10/docs/api/java/time/Duration.html#dividedBy(long)) */
    operator fun div(divisor: Long): Duration {
        // TODO: kotlinx.math
        throw NotImplementedError("Pending kotlin.math: dividedBy($divisor)")
    }

    /** See [`java.time.Duration#dividedBy(Duration)](https://docs.oracle.com/javase/10/docs/api/java/time/Duration.html#dividedBy(Duration)) */
    operator fun div(divisor: Duration): Long {
        // TODO: kotlinx.math
        throw NotImplementedError("Pending kotlin.math: dividedBy($divisor)")
    }

    // TODO: Overload operator
    /** See [`java.time.duration#negated()](https://docs.oracle.com/javase/10/docs/api/java/time/Duration.html#negated()) */
    operator fun unaryMinus() = this * -1

    fun abs() = if (isNegative) -this else this

    fun toDays(): Long = seconds / SECONDS_PER_DAY

    fun toHours(): Long = seconds / SECONDS_PER_HOUR

    fun toMinutes(): Long = seconds / SECONDS_PER_MINUTE

    fun toMillis(): Long {
        var tempSeconds = seconds
        var tempNanos = nanos
        if (tempSeconds < 0) {
            tempSeconds += 1
            tempNanos = (tempNanos - NANOS_PER_SECOND).toInt()
        }
        var millis = multiplyExact(tempSeconds, MILLIS_PER_SECOND)
        millis = addExact(millis, tempNanos / NANOS_PER_MILLI)
        return millis
    }

    fun toNanos(): Long {
        var tempSeconds = seconds
        var tempNanos = nanos
        if (tempSeconds < 0) {
            tempSeconds += 1
            tempNanos = (tempNanos - NANOS_PER_SECOND).toInt()
        }
        var totalNanos = multiplyExact(tempSeconds, NANOS_PER_SECOND)
        totalNanos = addExact(totalNanos, tempNanos)
        return totalNanos
    }

    fun toDaysPart() = seconds / SECONDS_PER_DAY

    fun toHoursPart() = (toHours() % HOURS_PER_DAY).toInt()

    fun toMinutesPart() = (toMinutes() % MINUTES_PER_HOUR).toInt()

    fun toSecondsPart() = (seconds % SECONDS_PER_MINUTE).toInt()

    fun toMillisPart() = (nanos / NANOS_PER_MILLI).toInt()

    fun toNanosPart() = nanos

    override fun compareTo(other: Duration) = compareValuesBy(this, other, { it.seconds }, { it.nanos })

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || other !is Duration) return false
        return seconds == other.seconds && nanos == other.nanos
    }

    override fun hashCode(): Int {
        return (seconds xor seconds.ushr(32)).toInt() + 51 * nanos
    }

    override fun toString(): String {
        if (this === ZERO) return "PT0S"
        var effectiveTotalSecs = seconds
        if (seconds < 0 && nanos > 0) {
            effectiveTotalSecs++
        }
        val hours = effectiveTotalSecs / SECONDS_PER_HOUR
        val minutes = ((effectiveTotalSecs % SECONDS_PER_HOUR) / SECONDS_PER_MINUTE).toInt()
        val secs = (effectiveTotalSecs % SECONDS_PER_MINUTE).toInt()
        val buf = StringBuilder(24)
        buf.append("PT")
        if (hours != 0L) {
            buf.append(hours).append('H')
        }
        if (minutes != 0) {
            buf.append(minutes).append('M')
        }
        if (secs == 0 && nanos == 0 && buf.length > 2) {
            return buf.toString()
        }
        if (seconds < 0 && nanos > 0) {
            if (secs == 0) {
                buf.append("-0")
            } else {
                buf.append(secs)
            }
        } else {
            buf.append(secs)
        }
        if (nanos > 0) {
            val nos = if (seconds < 0) {
                2 * NANOS_PER_SECOND - nanos
            } else {
                nanos + NANOS_PER_SECOND
            }
            buf.append('.').append(nos.toString().run { subSequence(1, length).trimEnd('0') })
        }
        buf.append('S')
        return buf.toString()
    }
}
