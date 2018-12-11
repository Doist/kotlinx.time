package kotlinx.time

class LocalTime {
    companion object {
        /** Hours per day. */
        internal const val HOURS_PER_DAY = 24
        /** Minutes per hour. */
        internal const val MINUTES_PER_HOUR = 60
        /** Minutes per day. */
        internal const val MINUTES_PER_DAY = MINUTES_PER_HOUR * HOURS_PER_DAY
        /** Seconds per minute. */
        internal const val SECONDS_PER_MINUTE = 60
        /** Seconds per hour. */
        internal const val SECONDS_PER_HOUR = SECONDS_PER_MINUTE * MINUTES_PER_HOUR
        /** Seconds per day. */
        internal const val SECONDS_PER_DAY = SECONDS_PER_HOUR * HOURS_PER_DAY
        /** Milliseconds per second. */
        internal const val MILLIS_PER_SECOND = 1000L
        /** Milliseconds per day. */
        internal const val MILLIS_PER_DAY = SECONDS_PER_DAY * MILLIS_PER_SECOND
        /** Microseconds per millisecond. */
        internal const val MICROS_PER_MILLI = 1000L
        /** Microseconds per second. */
        internal const val MICROS_PER_SECOND = MICROS_PER_MILLI * MILLIS_PER_SECOND
        /** Microseconds per day. */
        internal const val MICROS_PER_DAY = SECONDS_PER_DAY * MICROS_PER_SECOND
        /** Nanos per microseconf. */
        internal const val NANOS_PER_MICRO = 1000L
        /** Nanos per millisecond. */
        internal const val NANOS_PER_MILLI = NANOS_PER_MICRO * MICROS_PER_MILLI
        /** Nanos per second. */
        internal const val NANOS_PER_SECOND = NANOS_PER_MILLI * MILLIS_PER_SECOND
        /** Nanos per minute. */
        internal const val NANOS_PER_MINUTE = NANOS_PER_SECOND * SECONDS_PER_MINUTE
        /** Nanos per hour. */
        internal const val NANOS_PER_HOUR = NANOS_PER_MINUTE * MINUTES_PER_HOUR
        /** Nanos per day. */
        internal const val NANOS_PER_DAY = NANOS_PER_HOUR * HOURS_PER_DAY
    }
}
