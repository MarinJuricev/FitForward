package core

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.koin.dsl.module

val coreModule = module {
  factory {
    DateProvider {
      Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault())
    }
  }
}