package networking

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.Darwin

actual fun buildHttpClient(
    block: HttpClientConfig<*>.() -> Unit,
) = HttpClient(Darwin) {
    block()
    engine {
        configureRequest {
            setAllowsCellularAccess(true)
        }
    }
}
