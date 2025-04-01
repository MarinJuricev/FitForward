package home.presenter

import app.cash.molecule.RecompositionMode
import app.cash.molecule.moleculeFlow
import app.cash.turbine.test
import core.di.coreModule
import fixtures.FakeRoutineRepository
import home.di.homeModule
import home.repository.RoutineRepository
import kotlinx.coroutines.runBlocking
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class RoutinePickerPresenterMoleculeFlowTest : KoinTest {

    private val repository: RoutineRepository by inject()

    @BeforeTest
    fun setup() {
        startKoin {
            allowOverride(true)
            modules(
                listOf(
                    coreModule,
                    homeModule,
                    module {
                        single<RoutineRepository> { FakeRoutineRepository() }
                    }
                )
            )
        }
    }

    @AfterTest
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `test routine picker presenter emits view effect on navigate event using moleculeFlow`() =
        runBlocking {
            val selectedDate = "2021-01-01"

            moleculeFlow(mode = RecompositionMode.Immediate) {
                RoutinePickerPresenter(this, selectedDate, repository)
            }.test {
                val state = awaitItem()

                state.onRoutineEvent(RoutinePickerEvent.NavigateToRoutines)

                state.viewEffect.test {
                    val effect = awaitItem()
                    assertEquals(RoutinePickerEffect.OnRoutineClicked, effect)
                    cancelAndIgnoreRemainingEvents()
                }
                cancelAndIgnoreRemainingEvents()
            }
        }
}