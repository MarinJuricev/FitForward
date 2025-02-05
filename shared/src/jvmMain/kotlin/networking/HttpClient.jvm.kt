package networking

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.jetty.Jetty
import org.eclipse.jetty.util.ssl.SslContextFactory

actual fun buildHttpClient(
    block: HttpClientConfig<*>.() -> Unit,
) = HttpClient(Jetty) {
    block()
    engine {
        // this: JettyEngineConfig
        sslContextFactory = SslContextFactory.Client()
        clientCacheSize = 12
    }
}