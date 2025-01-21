package home.di

import home.HomeViewModel
import home.presenter.CalendarPresenterFactory
import home.presenter.ExercisePresenterFactory
import home.presenter.RoutinePickerPresenterFactory
import home.repository.InMemoryRoutineRepository
import home.repository.RoutineRepository
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val homeModule = module {
    viewModelOf(::HomeViewModel)
    factoryOf(::CalendarPresenterFactory)
    factoryOf(::RoutinePickerPresenterFactory)
    factoryOf(::ExercisePresenterFactory)
    factoryOf(::InMemoryRoutineRepository) bind RoutineRepository::class
}