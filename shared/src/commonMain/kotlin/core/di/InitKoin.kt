package core.di

import exercisedetail.di.exerciseDetailModule
import home.di.homeModule
import networking.networkModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import routine_creation.di.routineCreationModule

fun initKoin(
    consumerDeclaration: KoinAppDeclaration = {},
) = startKoin {
    consumerDeclaration()
    modules(
        listOf(
            platformModule(),
            networkModule,
            coreModule,
            homeModule,
            exerciseDetailModule,
            routineCreationModule,
        )
    )
}

// Called by non Android consumers
fun initKoin() = initKoin {}
