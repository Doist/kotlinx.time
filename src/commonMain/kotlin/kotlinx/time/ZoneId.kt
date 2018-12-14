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

abstract class ZoneId internal constructor() {
    abstract val id: String

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ZoneId) return false
        return id == other.id
    }

    override fun hashCode() = id.hashCode()

    override fun toString() = id

    companion object {
        val SHORT_IDS = mapOf(
            "ACT" to "Australia/Darwin",
            "AET" to "Australia/Sydney",
            "AGT" to "America/Argentina/Buenos_Aires",
            "ART" to "Africa/Cairo",
            "AST" to "America/Anchorage",
            "BET" to "America/Sao_Paulo",
            "BST" to "Asia/Dhaka",
            "CAT" to "Africa/Harare",
            "CNT" to "America/St_Johns",
            "CST" to "America/Chicago",
            "CTT" to "Asia/Shanghai",
            "EAT" to "Africa/Addis_Ababa",
            "ECT" to "Europe/Paris",
            "IET" to "America/Indiana/Indianapolis",
            "IST" to "Asia/Kolkata",
            "JST" to "Asia/Tokyo",
            "MIT" to "Pacific/Apia",
            "NET" to "Asia/Yerevan",
            "NST" to "Pacific/Auckland",
            "PLT" to "Asia/Karachi",
            "PNT" to "America/Phoenix",
            "PRT" to "America/Puerto_Rico",
            "PST" to "America/Los_Angeles",
            "SST" to "Pacific/Guadalcanal",
            "VST" to "Asia/Ho_Chi_Minh",
            // XXX: This will likely break non-Java implementations.
            "EST" to "-05:00",
            "MST" to "-07:00",
            "HST" to "-10:00"
        )

        fun systemDefault() = of(getActualTimeZoneId(), SHORT_IDS)

        fun getAvailableZoneIds() = getActualAvailableTimeZoneIds()

        fun of(zoneId: String, aliasMap: Map<String, String>) = of(aliasMap.getOrElse(zoneId) { zoneId })

        fun of(zoneId: String) = of(zoneId, true)

        fun ofOffset(prefix: String, offset: ZoneOffset): ZoneId {
            return if (prefix.isEmpty()) {
                return offset
            } else if (prefix != "GMT" && prefix != "UTC" && prefix != "UT") {
                throw IllegalArgumentException("prefix should be GMT, UTC or UT, is: $prefix")
            } else if (offset.totalSeconds != 0) {
                ZoneRegion("$prefix${offset.id}")
            } else {
                ZoneRegion(prefix)
            }
        }

        fun of(prefix: String, offset: ZoneOffset): ZoneId {
            return if (prefix.isEmpty()) {
                return offset
            } else if (prefix != "GMT" && prefix != "UTC" && prefix != "UT") {
                throw IllegalArgumentException("prefix should be GMT, UTC or UT, is: $prefix")
            } else if (offset.totalSeconds != 0) {
                ZoneRegion("$prefix${offset.id}")
            } else {
                ZoneRegion(prefix)
            }
        }

        fun of(zoneId: String, checkAvailable: Boolean): ZoneId {
            return if (zoneId.length <= 1 || zoneId.startsWith('+') || zoneId.startsWith('-')) {
                ZoneOffset.of(zoneId)
            } else if (zoneId.startsWith("UTC") || zoneId.startsWith("GMT")) {
                ofWithPrefix(zoneId, 3, checkAvailable)
            } else if (zoneId.startsWith("UT")) {
                ofWithPrefix(zoneId, 2, checkAvailable)
            } else {
                ZoneRegion.ofId(zoneId, checkAvailable)
            }
        }

        private fun ofWithPrefix(zoneId: String, prefixLength: Int, checkAvailable: Boolean): ZoneId {
            val prefix = zoneId.substring(0, prefixLength)
            if (zoneId.length == prefixLength) {
                return ofOffset(prefix, ZoneOffset.UTC)
            }
            if (zoneId[prefixLength] != '+' && zoneId[prefixLength] != '-') {
                return ZoneRegion.ofId(zoneId, checkAvailable)  // drop through to ZoneRulesProvider
            }
            try {
                val offset = ZoneOffset.of(zoneId.substring(prefixLength))
                return ofOffset(prefix, offset)
            } catch (ex: DateTimeException) {
                throw DateTimeException("Invalid ID for offset-based ZoneId: $zoneId", ex)
            }

        }
    }
}

expect fun getActualTimeZoneId(): String

expect fun getActualAvailableTimeZoneIds(): Set<String>
