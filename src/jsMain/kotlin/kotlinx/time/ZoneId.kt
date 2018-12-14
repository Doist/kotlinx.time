package kotlinx.time

import stub.moment

actual fun getPlatformCurrentTimeZoneId() = moment.tz.guess()

actual fun getPlatformAvailableTimeZoneIds() = setOf(*moment.tz.names())
