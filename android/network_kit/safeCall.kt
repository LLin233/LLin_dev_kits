package {package}.core.data.networking

import {package}.core.domain.util.NetworkError
import {package}.core.domain.util.Result
import kotlinx.coroutines.ensureActive
import kotlinx.serialization.SerializationException
import retrofit2.Response
import java.net.UnknownHostException
import kotlin.coroutines.coroutineContext

suspend inline fun <reified T> safeCall(execute: () -> Response<T>): Result<T, NetworkError> {
    val response =
        try {
            execute()
        } catch (e: UnknownHostException) {
            return Result.Error(NetworkError.NO_INTERNET)
        } catch (e: retrofit2.HttpException) {
            return Result.Error(NetworkError.UNKNOWN) // Customize as needed for specific HTTP errors
        } catch (e: SerializationException) {
            return Result.Error(NetworkError.SERIALIZATION)
        } catch (e: Exception) {
            coroutineContext.ensureActive()
            return Result.Error(NetworkError.UNKNOWN)
        }

    return responseToResult(response)
}
