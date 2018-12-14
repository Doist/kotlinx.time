@file:Suppress("ClassName")

package stub

@JsModule("moment-timezone")
@JsNonModule
external object moment {
    val tz: Tz

    object Tz {
        /* https://momentjs.com/timezone/docs/#/using-timezones/guessing-user-timezone */
        fun guess(): String
        /* https://momentjs.com/timezone/docs/#/data-loading/getting-zone-names/ */
        fun names(): Array<String>
    }
}
