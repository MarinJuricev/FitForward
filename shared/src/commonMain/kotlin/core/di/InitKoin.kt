package core.di

import home.di.homeModule
import networking.networkModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

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
        )
    )
}

// Called by non Android consumers
fun initKoin() = initKoin {}
