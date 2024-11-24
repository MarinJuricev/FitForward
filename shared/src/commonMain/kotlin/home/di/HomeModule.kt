package home.di

import home.presenter.CalendarPresenterFactory
import home.HomeViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val homeModule = module {
    viewModelOf(::HomeViewModel)
    factoryOf(::CalendarPresenterFactory)
}