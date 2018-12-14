package kotlinx.time

import kotlinx.time.LocalTime.Companion.MINUTES_PER_HOUR
import kotlinx.time.LocalTime.Companion.SECONDS_PER_HOUR
import kotlinx.time.LocalTime.Companion.SECONDS_PER_MINUTE
import kotlin.math.abs

class ZoneOffset private constructor(val totalSeconds: Int) : ZoneId(), Comparable<ZoneOffset> {
    override val id = buildId(totalSeconds)

    override fun compareTo(other: ZoneOffset): Int {
        // abs(totalSeconds) <= MAX_SECONDS, so no overflow can happen here
        return other.totalSeconds - totalSeconds
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ZoneOffset) return false
        return totalSeconds == other.totalSeconds
    }

    override fun hashCode() = totalSeconds

    override fun toString() = id

    companion object {
        private val SECONDS_CACHE = mutableMapOf<Int, ZoneOffset>()
        private val ID_CACHE = mutableMapOf<String, ZoneOffset>()

        private val MAX_SECONDS = 18 * SECONDS_PER_HOUR

        val UTC = ZoneOffset.ofTotalSeconds(0)
        val MIN = ZoneOffset.ofTotalSeconds(-MAX_SECONDS)
        val MAX = ZoneOffset.ofTotalSeconds(MAX_SECONDS)

        fun of(offsetId: String): ZoneOffset {
            // "Z" is always in the cache
            val offset = ID_CACHE[offsetId]
            if (offset != null) {
                return offset
            }

            val (hours, minutes, seconds) = when (offsetId.length) {
                2 -> {
                    // +h
                    Triple(parseNumber("${offsetId[0]}0${offsetId[1]}", 1, false), 0, 0)  // fallthru
                }
                3 -> {
                    // +hh
                    Triple(parseNumber(offsetId, 1, false), 0, 0)
                }
                5 -> {
                    // +hhmm
                    Triple(parseNumber(offsetId, 1, false), parseNumber(offsetId, 3, false), 0)
                }
                6 -> {
                    // +hh:mm
                    Triple(parseNumber(offsetId, 1, false), parseNumber(offsetId, 4, false), 0)
                }
                7 -> {
                    // +hhmmss
                    Triple(
                        parseNumber(offsetId, 1, false),
                        parseNumber(offsetId, 3, false),
                        parseNumber(offsetId, 5, false)
                    )
                }
                9 -> {
                    // +hh:mm:ss
                    Triple(
                        parseNumber(offsetId, 1, false),
                        parseNumber(offsetId, 4, false),
                        parseNumber(offsetId, 7, false)
                    )
                }
                else -> throw DateTimeException("Invalid ID for ZoneOffset, invalid format: $offsetId")
            }
            val first = offsetId[0]
            if (first != '+' && first != '-') {
                throw DateTimeException("Invalid ID for ZoneOffset, plus/minus not found when expected: $offsetId")
            }
            return if (first == '-') {
                ofHoursMinutesSeconds(-hours, -minutes, -seconds)
            } else {
                ofHoursMinutesSeconds(hours, minutes, seconds)
            }
        }

        private fun parseNumber(offsetId: CharSequence, pos: Int, precededByColon: Boolean): Int {
            if (precededByColon && offsetId[pos - 1] != ':') {
                throw DateTimeException("Invalid ID for ZoneOffset, colon not found when expected: $offsetId")
            }
            val ch1 = offsetId[pos]
            val ch2 = offsetId[pos + 1]
            if (ch1 < '0' || ch1 > '9' || ch2 < '0' || ch2 > '9') {
                throw DateTimeException("Invalid ID for ZoneOffset, non numeric characters found: $offsetId")
            }
            return (ch1.toInt() - 48) * 10 + (ch2.toInt() - 48)
        }

        fun ofHours(hours: Int) = ofHoursMinutesSeconds(hours, 0, 0)

        fun ofHoursMinutes(hours: Int, minutes: Int) = ofHoursMinutesSeconds(hours, minutes, 0)

        fun ofHoursMinutesSeconds(hours: Int, minutes: Int, seconds: Int): ZoneOffset {
            validate(hours, minutes, seconds)
            val totalSeconds = totalSeconds(hours, minutes, seconds)
            return ofTotalSeconds(totalSeconds)
        }

        private fun validate(hours: Int, minutes: Int, seconds: Int) {
            if (hours < -18 || hours > 18) {
                throw DateTimeException(
                    "Zone offset hours not in valid range: value $hours is not in the range -18 to 18"
                )
            }
            if (hours > 0) {
                if (minutes < 0 || seconds < 0) {
                    throw DateTimeException(
                        "Zone offset minutes and seconds must be positive because hours is positive"
                    )
                }
            } else if (hours < 0) {
                if (minutes > 0 || seconds > 0) {
                    throw DateTimeException(
                        "Zone offset minutes and seconds must be negative because hours is negative"
                    )
                }
            } else if ((minutes > 0 && seconds < 0) || (minutes < 0 && seconds > 0)) {
                throw DateTimeException("Zone offset minutes and seconds must have the same sign")
            }
            if (minutes < -59 || minutes > 59) {
                throw DateTimeException(
                    "Zone offset minutes not in valid range: value $minutes is not in the range -59 to 59"
                )
            }
            if (seconds < -59 || seconds > 59) {
                throw DateTimeException(
                    "Zone offset seconds not in valid range: value $seconds is not in the range -59 to 59"
                )
            }
            if (abs(hours) == 18 && (minutes or seconds) != 0) {
                throw DateTimeException("Zone offset not in valid range: -18:00 to +18:00")
            }
        }

        private fun totalSeconds(hours: Int, minutes: Int, seconds: Int): Int {
            return hours * SECONDS_PER_HOUR + minutes * SECONDS_PER_MINUTE + seconds
        }

        fun ofTotalSeconds(totalSeconds: Int): ZoneOffset {
            return when {
                totalSeconds < -MAX_SECONDS || totalSeconds > MAX_SECONDS -> {
                    throw DateTimeException("Zone offset not in valid range: -18:00 to +18:00")
                }
                totalSeconds % (15 * SECONDS_PER_MINUTE) == 0 -> {
                    SECONDS_CACHE.getOrPut(totalSeconds) {
                        ZoneOffset(totalSeconds).apply { ID_CACHE.getOrPut(id) { this } }
                    }
                }
                else -> ZoneOffset(totalSeconds)
            }
        }

        private fun buildId(totalSeconds: Int): String {
            if (totalSeconds == 0) return "Z"
            return StringBuilder().apply {
                val absTotalSeconds = abs(totalSeconds)
                val absHours = absTotalSeconds / SECONDS_PER_HOUR
                val absMinutes = (absTotalSeconds / SECONDS_PER_MINUTE) % MINUTES_PER_HOUR
                val absSeconds = absTotalSeconds % SECONDS_PER_MINUTE
                append(if (totalSeconds < 0) "-" else "+")
                append(if (absHours < 10) "0" else "").append(absHours)
                append(if (absMinutes < 10) ":0" else ":").append(absMinutes)
                if (absSeconds != 0) {
                    append(if (absSeconds < 10) ":0" else ":").append(absSeconds)
                }
            }.toString()
        }
    }
}
