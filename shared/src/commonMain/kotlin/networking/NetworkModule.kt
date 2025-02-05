package networking

import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val networkModule = module {
    single {
        buildHttpClient {
            install(DefaultRequest) {
                header("Accept", "application/json")
            }
            install(HttpTimeout) {
                requestTimeoutMillis = NETWORK_TIMEOUT_MILLIS
                connectTimeoutMillis = NETWORK_TIMEOUT_MILLIS
                socketTimeoutMillis = NETWORK_TIMEOUT_MILLIS
            }
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
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
                        BearerTokens(
                            accessToken = tokens.accessToken,
                            refreshToken = tokens.refreshToken
                        )
                    }
                    refreshTokens {
                        val tokens = getAccessToken()
                        BearerTokens(
                            accessToken = tokens.accessToken,
                            refreshToken = tokens.refreshToken
                        )
                    }
                }
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

private const val NETWORK_TIMEOUT_MILLIS = 60_000L
