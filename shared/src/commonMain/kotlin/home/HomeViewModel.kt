package home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import core.date.toDayMonthYear
import home.presenter.CalendarPresenterFactory
import home.presenter.RoutinePickerPresenterFactory
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.LocalDate

class HomeViewModel(
    calendarPresenterFactory: CalendarPresenterFactory,
    routinePickerPresenterFactory: RoutinePickerPresenterFactory,
) : ViewModel() {

    val calendarState = calendarPresenterFactory.create(viewModelScope)
    val selectedDate = calendarState
        .map { state -> state.days.find { it.isSelected }?.date }
        .filterNotNull()
        .map(LocalDate::toDayMonthYear)
        .stateIn(viewModelScope, SharingStarted.Lazily, "Date not selected")

    val routinePickerState = routinePickerPresenterFactory.create(viewModelScope)
}
