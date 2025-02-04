package networking

import org.koin.dsl.module

val clientNetworkModule = module {
    single { buildHttpClient() }
}