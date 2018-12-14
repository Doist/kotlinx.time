package kotlinx.time

import java.util.*

actual fun getPlatformCurrentTimeZoneId(): String = TimeZone.getDefault().id

actual fun getPlatformAvailableTimeZoneIds(): Set<String> = setOf(*TimeZone.getAvailableIDs())
