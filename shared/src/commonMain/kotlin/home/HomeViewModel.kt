package home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import core.date.toDayMonthYear
import home.presenter.CalendarPresenterFactory
import home.presenter.ExercisePresenterFactory
import home.presenter.ExerciseState
import home.presenter.RoutinePickerPresenterFactory
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
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
    exercisePresenterFactory: ExercisePresenterFactory,
) : ViewModel() {

    val calendarState = calendarPresenterFactory.create(viewModelScope)
    val selectedDate = calendarState
        .map { state -> state.days.find { it.isSelected }?.date }
        .filterNotNull()
        .map(LocalDate::toDayMonthYear)
        .stateIn(viewModelScope, SharingStarted.Lazily, "")

    val routinePickerState = routinePickerPresenterFactory.create(viewModelScope)

    val exerciseState: StateFlow<ExerciseState> = combine(
        routinePickerState,
        selectedDate,
    ) { routinePickerState, selectedDate ->
        val selectedRoutine = routinePickerState.selectedRoutine
        if (selectedRoutine == null) return@combine flowOf(ExerciseState())

        exercisePresenterFactory.create(
            coroutineScope = viewModelScope,
            routineId = selectedRoutine.id,
            date = "",
        )
    }
        .flatMapLatest { it }
        .stateIn(viewModelScope, SharingStarted.Lazily, ExerciseState())
}
