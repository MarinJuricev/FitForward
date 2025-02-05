package exercisedetail.di

import exercisedetail.ExerciseDetailViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val exerciseDetailModule = module {
    viewModelOf(::ExerciseDetailViewModel)
}