package kotlinx.time

import stub.moment

actual fun getActualTimeZoneId() = moment.tz.guess()

actual fun getActualAvailableTimeZoneIds() = setOf(*moment.tz.names())
