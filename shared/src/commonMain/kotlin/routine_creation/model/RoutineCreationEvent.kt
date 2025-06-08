package routine_creation.model

import home.model.Routine

sealed interface RoutineCreationEvent {
    data object OnExploreClicked : RoutineCreationEvent
    data object OnCreateRoutineClicked : RoutineCreationEvent
    data object OnBackClicked: RoutineCreationEvent
    data class OnRoutineClicked(val routine: Routine) : RoutineCreationEvent
}