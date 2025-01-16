package home.presenter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import app.cash.molecule.RecompositionMode
import app.cash.molecule.launchMolecule
import home.repository.Routine
import home.repository.RoutineRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map

fun Routine.toRoutineInfo(
    isSelected: Boolean = false,
) = RoutineInfo(
    id = id,
    name = name,
    description = description,
    isSelected = isSelected,
)

data class RoutinePickerState(
    val routines: List<RoutineInfo>,
    val onRoutineClick: (RoutineInfo) -> Unit,
)

data class RoutineInfo(
    val id: String,
    val name: String,
    val description: String,
    val isSelected: Boolean,
)

class RoutinePickerPresenterFactory(
    private val routineRepository: RoutineRepository,
) {

    fun create(
        coroutineScope: CoroutineScope
    ): StateFlow<RoutinePickerState> = coroutineScope.launchMolecule(RecompositionMode.Immediate) {
        RoutinePickerPresenter(
            routineRepository = routineRepository
        )
    }
}

@Composable
internal fun RoutinePickerPresenter(
    routineRepository: RoutineRepository,
): RoutinePickerState {
    var availableRoutines = produceState(emptyList<RoutineInfo>()) {
        routineRepository
            .observeRoutines()
            .map { routine -> routine.map(Routine::toRoutineInfo) }
            .collectLatest { value = it }
    }

    return RoutinePickerState(
        routines = availableRoutines.value,
        onRoutineClick = {},
    )
}
