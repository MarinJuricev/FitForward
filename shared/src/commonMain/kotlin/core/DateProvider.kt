package core

import kotlinx.datetime.LocalDateTime

fun interface DateProvider {

  fun generate(): LocalDateTime
}
