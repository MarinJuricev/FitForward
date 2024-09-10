package core

import kotlinx.datetime.LocalDate

fun interface DateProvider {

  fun generate(): LocalDate
}
