package home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import core.date.toDayMonthYear
import home.presenter.CalendarPresenterFactory
import home.presenter.ExerciseByDatePresenterFactory
import home.presenter.ExerciseState
import home.presenter.RoutinePickerPresenterFactory
import home.presenter.RoutinePickerState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.LocalDate

class HomeViewModel(
    calendarPresenterFactory: CalendarPresenterFactory,
    routinePickerPresenterFactory: RoutinePickerPresenterFactory,
    exerciseByDatePresenterFactory: ExerciseByDatePresenterFactory,
) : ViewModel() {

    val calendarState = calendarPresenterFactory.create(viewModelScope)

    val selectedDate = calendarState
        .map { state -> state.days.find { it.isSelected }?.date }
        .filterNotNull()
        .map(LocalDate::toDayMonthYear)
        .stateIn(viewModelScope, SharingStarted.Lazily, "")

    val routinePickerState = selectedDate.flatMapLatest { selectedDate ->
        routinePickerPresenterFactory.create(
            selectedDate = selectedDate,
            coroutineScope = viewModelScope
        )
    }.stateIn(viewModelScope, SharingStarted.Lazily, RoutinePickerState())

    val exerciseState = selectedDate.flatMapLatest { date ->
        exerciseByDatePresenterFactory.create(
            coroutineScope = viewModelScope,
            date = date,
        )
    }.stateIn(viewModelScope, SharingStarted.Lazily, ExerciseState())
}
