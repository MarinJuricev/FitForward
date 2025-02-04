package networking

import arrow.core.Either
import arrow.fx.coroutines.parZip
import io.ktor.client.plugins.ResponseException

sealed interface NetworkError {

    data class MappedError(
        val code: Int,
        val message: String?,
    ) : NetworkError

    data object UnknownError : NetworkError
}

//TODO: Expose this through some kind of a lesser scope ? This way everybody in shared has access to this
internal suspend fun <T> safeApiCall(
    action: suspend () -> T
): Either<NetworkError, T> = Either.catch {
    action()
}.mapLeft { throwable: Throwable ->
    when (throwable) {
        is ResponseException -> NetworkError.MappedError(
            code = throwable.response.status.value,
            message = throwable.message,
        )

        else -> NetworkError.UnknownError
    }
}
