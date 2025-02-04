package networking

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.Darwin

actual fun buildHttpClient(): HttpClient = HttpClient(Darwin) {
    engine {
        configureRequest {
            setAllowsCellularAccess(true)
        }
    }
}
