@file:Suppress("NOTHING_TO_INLINE")

import kotlin.math.abs

/**
 * See [Math.addExact(long, long)](https://docs.oracle.com/javase/8/docs/api/java/lang/Math.html#addExact-long-long-).
 */
inline fun addExact(x: Long, y: Long): Long {
    val r = x + y
    if (x xor r and (y xor r) < 0) {
        throw ArithmeticException("long overflow")
    }
    return r
}
inline fun addExact(x: Long, y: Int) = addExact(x, y.toLong())

/**
 * See [Math.multiplyExact(long, long)](https://docs.oracle.com/javase/8/docs/api/java/lang/Math.html#multiplyExact-long-long-).
 */
inline fun multiplyExact(x: Long, y: Long): Long {
    val r = x * y
    val ax = abs(x)
    val ay = abs(y)
    if ((ax or ay).ushr(31) != 0L) {
        if (((y != 0L) && (r / y != x)) || (x == Long.MIN_VALUE && y == -1L)) {
            throw ArithmeticException("long overflow")
        }
    }
    return r
}
inline fun multiplyExact(x: Long, y: Int): Long = multiplyExact(x, y.toLong())

/**
 * See [Math.floorDiv(long, long)](https://docs.oracle.com/javase/8/docs/api/java/lang/Math.html#floorDiv-long-long).
 */
inline fun floorDiv(x: Long, y: Long): Long {
    var r = x / y
    if ((x xor y < 0) && (r * y != x)) {
        r--
    }
    return r
}

/**
 * See [Math.floorMod(long, long)](https://docs.oracle.com/javase/8/docs/api/java/lang/Math.html#floorMod-long-long).
 */
inline fun floorMod(x: Long, y: Long): Long {
    return x - floorDiv(x, y) * y
}
