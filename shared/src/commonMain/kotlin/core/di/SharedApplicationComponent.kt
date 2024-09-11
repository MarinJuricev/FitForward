package core.di

import core.AppCoroutineDispatchers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides

expect val ioDispatcher: CoroutineDispatcher

@Component
interface SharedApplicationComponent {

    @ApplicationScope
    @Provides
    fun provideCoroutineDispatchers(): AppCoroutineDispatchers = AppCoroutineDispatchers(
        io = ioDispatcher,
        computation = Dispatchers.Default,
        main = Dispatchers.Main,
    )
}
