package {pack}.{namePath}

suspend inline fun <reified T> responseToResult(response: Response<T>): Result<T, NetworkError> =
    if (response.isSuccessful) {
        val body = response.body()
        body?.let { Result.Success(it) } ?: Result.Error(NetworkError.SERIALIZATION)
    } else {
        when (response.code()) {
            408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
            429 -> Result.Error(NetworkError.TOO_MANY_REQUESTS)
            in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
            else -> Result.Error(NetworkError.UNKNOWN)
        }
    }
