package core.di

import db.DriverFactory
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

//TODO: Introduce a mobileMain sourceSet potentially and avoid this expect/actual fun
// platformModule(): Module, when I start tinkering with the BE client I might use it
actual fun platformModule() = module {
    singleOf(::DriverFactory)
}