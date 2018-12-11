package kotlinx.time

/**
 * Exception used to indicate a problem while calculating a date-time.
 *
 * This exception is used to indicate problems with creating, querying and manipulating date-time objects.
 */
open class DateTimeException : RuntimeException {
    /**
     * Constructs a new date-time exception with the specified message.
     *
     * @param message  the message to use for this exception, may be null
     */
    constructor(message: String) : super(message)

    /**
     * Constructs a new date-time exception with the specified message and cause.
     *
     * @param message  the message to use for this exception, may be null
     * @param cause  the cause of the exception, may be null
     */
    constructor(message: String, cause: Throwable) : super(message, cause)
}
