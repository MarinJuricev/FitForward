package routine_creation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import home.model.Routine
import home.repository.RoutineRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import routine_creation.model.RoutineCreationEffect
import routine_creation.model.RoutineCreationEffect.NavigateBack
import routine_creation.model.RoutineCreationEvent.OnBackClicked
import routine_creation.model.RoutineCreationEvent.OnCreateRoutineClicked
import routine_creation.model.RoutineCreationEvent.OnExploreClicked
import routine_creation.model.RoutineCreationEvent.OnRoutineClicked
import routine_creation.model.RoutineCreationState

class RoutineCreationViewModel(
    private val routineRepository: RoutineRepository
) : ViewModel() {

    private val activeRoutines = MutableStateFlow<List<Routine>>(emptyList())
    private val isCreationActive = MutableStateFlow(false)

    private val _viewEffect = Channel<RoutineCreationEffect>(Channel.BUFFERED)
    val viewEffect: Flow<RoutineCreationEffect> = _viewEffect.receiveAsFlow()

    val viewState = combine(
        activeRoutines,
        isCreationActive,
    ) { activeRoutines, isCreationActive ->
        RoutineCreationState(
            routines = activeRoutines,
            isCreateActive = isCreationActive,
            routineCreationEvent = { event ->
                when (event) {
                    OnBackClicked -> _viewEffect.trySend(NavigateBack)
                    OnCreateRoutineClicked -> TODO()
                    OnExploreClicked -> TODO()
                    is OnRoutineClicked -> TODO()
                }

            }
        )
    }.onStart {
        observeRoutines()
    }.stateIn(viewModelScope, SharingStarted.Lazily, RoutineCreationState())

    private fun observeRoutines() {
        routineRepository
            .observeRoutines()
            .onEach { routines ->
                activeRoutines.update { routines }
            }.launchIn(viewModelScope)
    }
}