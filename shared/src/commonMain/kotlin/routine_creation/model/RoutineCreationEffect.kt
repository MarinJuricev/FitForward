package routine_creation.model

sealed interface RoutineCreationEffect {
    data object NavigateBack : RoutineCreationEffect
}