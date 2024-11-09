package core.di

import home.di.homeModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(
    consumerDeclaration: KoinAppDeclaration = {},
) = startKoin {
    consumerDeclaration()
    modules(
        listOf(
            coreModule,
            homeModule,
        )
    )
}

// Called by non Android consumers
fun initKoin() = initKoin {}
