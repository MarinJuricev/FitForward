package core.di

import db.DriverFactory
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual fun platformModule() = module {
    singleOf(::DriverFactory)
}