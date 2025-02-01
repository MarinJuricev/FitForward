package home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import core.date.toDayMonthYear
import home.presenter.CalendarPresenterFactory
import home.presenter.ExerciseByDatePresenterFactory
import home.presenter.ExerciseState
import home.presenter.RoutinePickerPresenterFactory
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
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

    val routinePickerState = routinePickerPresenterFactory.create(viewModelScope)

    val exerciseState = combine(
        routinePickerState,
        selectedDate,
    ) { routinePickerState, selectedDate ->
        exerciseByDatePresenterFactory.create(
            coroutineScope = viewModelScope,
            routineId = routinePickerState.selectedRoutine?.id,
            date = selectedDate,
        )
    }
        .flatMapLatest { it }
        .stateIn(viewModelScope, SharingStarted.Lazily, ExerciseState())
}
