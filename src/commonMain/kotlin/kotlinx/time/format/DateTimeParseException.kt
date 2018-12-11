package kotlinx.time.format

import kotlinx.time.DateTimeException

/**
 * An exception thrown when an error occurs during parsing.
 *
 * This exception includes the text being parsed and the error index.
 */
class DateTimeParseException : DateTimeException {

    /**
     * The string that was being parsed.
     */
    val parsedString: String

    /**
     * The error index in the text.
     */
    val errorIndex: Int

    /**
     * Constructs a new exception with the specified message.
     *
     * @param message  the message to use for this exception, may be null
     * @param parsedData  the parsed text, should not be null
     * @param errorIndex  the index in the parsed string that was invalid, should be a valid index
     */
    constructor(message: String, parsedData: CharSequence, errorIndex: Int) : super(message) {
        this.parsedString = parsedData.toString()
        this.errorIndex = errorIndex
    }

    /**
     * Constructs a new exception with the specified message and cause.
     *
     * @param message  the message to use for this exception, may be null
     * @param parsedData  the parsed text, should not be null
     * @param errorIndex  the index in the parsed string that was invalid, should be a valid index
     * @param cause  the cause exception, may be null
     */
    constructor(message: String, parsedData: CharSequence, errorIndex: Int, cause: Throwable) : super(message, cause) {
        this.parsedString = parsedData.toString()
        this.errorIndex = errorIndex
    }
}
