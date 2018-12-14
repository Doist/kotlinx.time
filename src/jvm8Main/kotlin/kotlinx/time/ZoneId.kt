package kotlinx.time

import java.util.*

actual fun getActualTimeZoneId(): String = TimeZone.getDefault().id

actual fun getActualAvailableTimeZoneIds(): Set<String> = setOf(*TimeZone.getAvailableIDs())
