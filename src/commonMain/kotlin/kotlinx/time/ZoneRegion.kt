package kotlinx.time

internal class ZoneRegion(override val id: String) : ZoneId() {
    companion object {
        fun ofId(zoneId: String, @Suppress("UNUSED_PARAMETER") checkAvailable: Boolean): ZoneRegion {
            checkName(zoneId)
            return ZoneRegion(zoneId)
        }

        /**
         * Checks that the given string is a legal ZondId name.
         *
         * @param zoneId  the time-zone ID, not null
         * @throws DateTimeException if the ID format is invalid
         */
        private fun checkName(zoneId: String) {
            val n = zoneId.length
            if (n < 2) {
                throw DateTimeException("Invalid ID for region-based ZoneId, invalid format: $zoneId")
            }
            for (i in 0 until n) {
                val c = zoneId[i]
                if (c in 'a'..'z') continue
                if (c in 'A'..'Z') continue
                if (c == '/' && i != 0) continue
                if (c in '0'..'9' && i != 0) continue
                if (c == '~' && i != 0) continue
                if (c == '.' && i != 0) continue
                if (c == '_' && i != 0) continue
                if (c == '+' && i != 0) continue
                if (c == '-' && i != 0) continue
                throw DateTimeException("Invalid ID for region-based ZoneId, invalid format: $zoneId")
            }
        }
    }
}
