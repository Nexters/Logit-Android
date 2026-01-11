package com.useai.core.network.error

import com.useai.core.model.error.RootError
import kotlin.coroutines.cancellation.CancellationException

fun <T: RootError, R> runCatchingWith(
    errorType: T,
    block: () -> R
): Result<R> {
    return try {
        Result.success(block())
    } catch (e: RemoteError) {
        errorType.createErrorInstances().find { definedError ->
            e.errorCode == definedError.code
        }?.let { Result.failure(it) } ?: Result.failure(e)
    } catch (e: CancellationException) {
        throw e
    } catch (e: Throwable) {
        Result.failure(e)
    }
}

fun <R> runCatchingWith(
    block: () -> R
): Result<R> {
    return try {
        Result.success(block())
    } catch (e: RemoteError) {
        Result.failure(e)
    } catch (e: CancellationException) {
        throw e
    } catch (e: Throwable) {
        Result.failure(e)
    }
}