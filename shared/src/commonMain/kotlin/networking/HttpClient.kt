package networking

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig

expect fun buildHttpClient(
    block: HttpClientConfig<*>.() -> Unit,
): HttpClient

