package kotlinx.time

import kotlinx.cinterop.pointed
import kotlinx.cinterop.toKString
import platform.posix.*


/*
 * The following implementation follows various POSIX standards. For more information, see the following documentation:
 * * [`man environ.7`](http://man7.org/linux/man-pages/man7/environ.7.html)
 * * [`man localtime.5`](http://man7.org/linux/man-pages/man5/localtime.5.html)
 * * [Specifying the Time Zone with TZ](https://www.gnu.org/software/libc/manual/html_node/TZ-Variable.html)
 */

private val zoneInfoPath =
    getenv("TZDIR")?.toKString()?.trimEnd('/')?.padEnd(1, '/') ?: "/usr/share/zoneinfo/"

private val zonePath =
    getenv("TZ")?.toKString()?.takeIf { it.startsWith(':') }?.let { tz ->
        val path = if (tz.startsWith(":/")) {
            tz.substring(1)
        } else {
            "$zoneInfoPath${tz.substring(1)}"
        }
        path.takeIf { access(it, R_OK) == 0 }
    } ?: "/etc/localtime"

actual fun getActualTimeZoneId() =
    realpath(zonePath, null)?.toKString()?.replace(zoneInfoPath, "") ?: "GMT"

actual fun getActualAvailableTimeZoneIds() = listDir(zoneInfoPath)

private fun listDir(fileName: String, prefix: String? = null): Set<String> {
    val dir = opendir(fileName) ?: return emptySet()
    try {
        val zoneIds = mutableSetOf<String>()
        while (true) {
            val de = readdir(dir) ?: break
            val name = de.pointed.d_name.toKString()
            if (!name[0].isUpperCase()) continue
            val zoneId = if (!prefix.isNullOrEmpty()) "$prefix/$name" else name
            if (de.pointed.d_type.toInt() == DT_DIR) {
                zoneIds.addAll(listDir("$fileName/$name", zoneId))
            } else {
                zoneIds.add(zoneId)
            }
        }
        return zoneIds
    } finally {
        closedir(dir)
    }
}
