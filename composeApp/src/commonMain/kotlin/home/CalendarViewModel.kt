package home

import core.DateProvider
import home.model.DayInfo
import kotlinx.coroutines.flow.MutableStateFlow

class CalendarPresenter(
  private val dateProvider: DateProvider,
) {

  private val selectedDate = MutableStateFlow<Int?>(null)
  private val availableDates = MutableStateFlow<List<List<DayInfo>>>(
   dateProvider.generate().
  )

  fun generateDate(): String {
    val test = arrayOf(2)
    return dateProvider.generate()
  }
}