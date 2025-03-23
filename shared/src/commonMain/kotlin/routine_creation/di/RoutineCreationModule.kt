package routine_creation.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import routine_creation.RoutineCreationViewModel

val routineCreationModule = module {
    viewModelOf(::RoutineCreationViewModel)
}