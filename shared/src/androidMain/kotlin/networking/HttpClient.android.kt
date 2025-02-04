package networking

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import kotlinx.serialization.json.Json

actual fun buildHttpClient(): HttpClient = HttpClient(OkHttp) {
    engine {
        // this: OkHttpConfig
        config {
            // this: OkHttpClient.Builder
            followRedirects(true)
            // ...
        }
    }
    install(ContentNegotiation) {
        Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        }
    }
    install(Auth) {
        bearer {
            loadTokens {
                val tokens = getAccessToken()
                BearerTokens(accessToken = tokens.accessToken, refreshToken = tokens.refreshToken)
            }
            refreshTokens {
                val tokens = getAccessToken()
                BearerTokens(accessToken = tokens.accessToken, refreshToken = tokens.refreshToken)
            }
        }
    }
}

private data class TokenResult(
    val accessToken: String,
    val refreshToken: String
)

private suspend fun getAccessToken(): TokenResult = TokenResult("", "")
private suspend fun refreshTokens(): TokenResult {
    // Clear existing tokens, then
    return TokenResult("", "")
}
