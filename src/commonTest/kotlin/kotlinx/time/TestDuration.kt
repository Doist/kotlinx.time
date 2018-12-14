/*
 * Copyright (c) 2019, Doist
 *
 * This code has been rewritten in Kotlin. It is redistributed under the terms
 * of the GNU General Public License version 2 license, as was the prior work.
 */

/*
 * Copyright (c) 2012, 2013, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
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

@file:Suppress("FunctionName")

package kotlinx.time

import kotlin.test.Test
import kotlin.test.assertSame

/**
 * Test Duration.
 *
 * Based on OpenJDK 11's `test/java/time/TestDuration.java`.
 */
class TestDuration {
    @Test
    fun plus_zeroReturnsThis() {
        val t = Duration.ofSeconds(-1)
        assertSame(t.plus(Duration.ZERO), t)
    }

    @Test
    fun plus_zeroSingleton() {
        val t = Duration.ofSeconds(-1)
        assertSame(t.plus(Duration.ofSeconds(1)), Duration.ZERO)
    }

    @Test
    fun plusSeconds_zeroReturnsThis() {
        val t = Duration.ofSeconds(-1)
        assertSame(t.plusSeconds(0), t)
    }

    @Test
    fun plusSeconds_zeroSingleton() {
        val t = Duration.ofSeconds(-1)
        assertSame(t.plusSeconds(1), Duration.ZERO)
    }

    @Test
    fun plusMillis_zeroReturnsThis() {
        val t = Duration.ofSeconds(-1, 2000000)
        assertSame(t.plusMillis(0), t)
    }

    @Test
    fun plusMillis_zeroSingleton() {
        val t = Duration.ofSeconds(-1, 2000000)
        assertSame(t.plusMillis(998), Duration.ZERO)
    }

    @Test
    fun plusNanos_zeroReturnsThis() {
        val t = Duration.ofSeconds(-1, 2000000)
        assertSame(t.plusNanos(0), t)
    }

    @Test
    fun plusNanos_zeroSingleton() {
        val t = Duration.ofSeconds(-1, 2000000)
        assertSame(t.plusNanos(998000000), Duration.ZERO)
    }

    @Test
    fun minus_zeroReturnsThis() {
        val t = Duration.ofSeconds(1)
        assertSame(t.minus(Duration.ZERO), t)
    }

    @Test
    fun minus_zeroSingleton() {
        val t = Duration.ofSeconds(1)
        assertSame(t.minus(Duration.ofSeconds(1)), Duration.ZERO)
    }

    @Test
    fun minusSeconds_zeroReturnsThis() {
        val t = Duration.ofSeconds(1)
        assertSame(t.minusSeconds(0), t)
    }

    @Test
    fun minusSeconds_zeroSingleton() {
        val t = Duration.ofSeconds(1)
        assertSame(t.minusSeconds(1), Duration.ZERO)
    }

    @Test
    fun minusMillis_zeroReturnsThis() {
        val t = Duration.ofSeconds(1, 2000000)
        assertSame(t.minusMillis(0), t)
    }

    @Test
    fun minusMillis_zeroSingleton() {
        val t = Duration.ofSeconds(1, 2000000)
        assertSame(t.minusMillis(1002), Duration.ZERO)
    }

    @Test
    fun minusNanos_zeroReturnsThis() {
        val t = Duration.ofSeconds(1, 2000000)
        assertSame(t.minusNanos(0), t)
    }

    @Test
    fun minusNanos_zeroSingleton() {
        val t = Duration.ofSeconds(1, 2000000)
        assertSame(t.minusNanos(1002000000), Duration.ZERO)
    }

    @Test
    fun test_abs_same() {
        val base = Duration.ofSeconds(12)
        assertSame(base.abs(), base)
    }
}
