package home.presenter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import app.cash.molecule.RecompositionMode
import app.cash.molecule.launchMolecule
import home.model.Routine
import home.model.RoutineHistory
import home.presenter.RoutinePickerEvent.NavigateToRoutines
import home.presenter.RoutinePickerEvent.RoutineSelected
import home.repository.RoutineRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

sealed interface RoutinePickerEvent {
    data class RoutineSelected(val routine: RoutineInfo) : RoutinePickerEvent
    data object NavigateToRoutines : RoutinePickerEvent
}

fun Routine.toRoutineInfo(
    isSelected: Boolean = false
) = RoutineInfo(
    id = id,
    name = name,
    description = "$exercisesCount exercises",
    isSelected = isSelected,
    exerciseCount = exercisesCount,
)

data class RoutinePickerState(
    val routines: List<RoutineInfo> = emptyList(),
    val selectedRoutine: RoutineInfo? = null,
    val viewEffect: Flow<RoutinePickerEffect> = emptyFlow(),
    val onRoutineEvent: (RoutinePickerEvent) -> Unit = {},
)

sealed interface RoutinePickerEffect {
    data object OnRoutineClicked : RoutinePickerEffect
}

data class RoutineInfo(
    val id: String,
    val name: String,
    val isSelected: Boolean,
    val description: String,
    val exerciseCount: Int,
)

class RoutinePickerPresenterFactory(
    private val routineRepository: RoutineRepository,
) {

    fun create(
        coroutineScope: CoroutineScope,
        selectedDate: String,
    ): StateFlow<RoutinePickerState> = coroutineScope.launchMolecule(RecompositionMode.Immediate) {
        RoutinePickerPresenter(
            coroutineScope = coroutineScope,
            selectedDate = selectedDate,
            routineRepository = routineRepository
        )
    }
}

@Composable
internal fun RoutinePickerPresenter(
    coroutineScope: CoroutineScope,
    selectedDate: String,
    routineRepository: RoutineRepository,
): RoutinePickerState {
    val availableRoutines by routineRepository
        .observeRoutines()
        .map { routine -> routine.map(Routine::toRoutineInfo) }
        .collectAsState(emptyList())
    val viewEffect = remember { Channel<RoutinePickerEffect>(Channel.BUFFERED) }
    var selectedRoutine by remember { mutableStateOf<RoutineInfo?>(null) }

    LaunchedEffect(selectedDate) {
        routineRepository
            .observeRoutinesByDate(selectedDate)
            .collectLatest {
                selectedRoutine = it.firstOrNull()?.toRoutineInfo()
            }
    }

    return RoutinePickerState(
        routines = availableRoutines,
        selectedRoutine = selectedRoutine,
        viewEffect = viewEffect.receiveAsFlow(),
        onRoutineEvent = { event ->
            when (event) {
                is RoutineSelected -> coroutineScope.launch {
                    val routineHistory = RoutineHistory(
                        routineId = event.routine.id,
                        performedAt = selectedDate,
                        exercises = routineRepository
                            .observeExercises(event.routine.id)
                            .firstOrNull()
                            .orEmpty(),
                    )

                    routineRepository.deleteWorkoutExerciseByDate(selectedDate)
                    routineRepository.upsertRoutineHistory(routineHistory)
                }

                is NavigateToRoutines -> coroutineScope.launch {
                    viewEffect.send(RoutinePickerEffect.OnRoutineClicked)
                }
            }
        }
    )
}
