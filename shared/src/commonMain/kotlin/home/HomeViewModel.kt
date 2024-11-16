package home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate

class HomeViewModel(
    calendarPresenterFactory: CalendarPresenterFactory
) : ViewModel() {

    val calendarState = calendarPresenterFactory.create(viewModelScope)
    val mappedCalendar = calendarState
        .map { state -> state.days.find { it.isSelected } }
        .filterNotNull()
        .map {
            LocalDate.parse(it.name)
        }
        .launchIn(viewModelScope)
}