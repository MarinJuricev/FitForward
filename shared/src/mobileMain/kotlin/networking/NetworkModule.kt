package networking

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import org.koin.dsl.module

val clientNetworkModule = module {
    single { buildHttpClient() }
    HttpClient {

    }.get("htttps:").body<String>() { exception ->
        when(exception) {

        }
    }
}