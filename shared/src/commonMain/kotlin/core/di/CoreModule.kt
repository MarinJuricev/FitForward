@file:OptIn(DelicateCoroutinesApi::class)

package core.di

import core.AppCoroutineDispatchers
import core.DateProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.koin.dsl.module

expect val ioDispatcher: CoroutineDispatcher

val coreModule = module {
    factory {
        DateProvider {
            Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        }
    }
    factory {
        AppCoroutineDispatchers(
//            io = ioDispatcher,
            // For now just live with this, we won't be utilizing the IO dispatcher
            // that often, we rely on the libraries that expose suspend functions to
            // to be main "safe"
            io = Dispatchers.Default,
            computation = Dispatchers.Default,
            main = Dispatchers.Main,
        )
    }

    factory<CoroutineScope> { GlobalScope }
}
