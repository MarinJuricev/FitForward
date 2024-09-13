package core.di

import core.AppCoroutineDispatchers
import core.DateProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import me.tatarka.inject.annotations.Provides

expect val ioDispatcher: CoroutineDispatcher

interface SharedApplicationComponent {

    @ApplicationScope
    @Provides
    fun provideDateProvider(): DateProvider = DateProvider {
        Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date
    }

    @ApplicationScope
    @Provides
    fun provideCoroutineDispatchers(): AppCoroutineDispatchers = AppCoroutineDispatchers(
        io = ioDispatcher,
        computation = Dispatchers.Default,
        main = Dispatchers.Main,
    )
}
