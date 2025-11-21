package com.linhphan.lpcore.domain.base

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn

/**
 * Executes business logic synchronously or asynchronously using Coroutines.
 */
abstract class BaseUseCase<in P, R>(private val coroutineDispatcher: CoroutineDispatcher) {

    /**
     * Executes the use case asynchronously and returns a [kotlinx.coroutines.flow.Flow].
     *
     * @return a [kotlinx.coroutines.flow.Flow] emitting the result.
     */
    operator fun invoke(parameters: P): Flow<Result<R>> {
        return execute(parameters)
            .catch { e -> emit(Result.Error(Exception(e))) }
            .flowOn(coroutineDispatcher)
    }

    /**
     * Override this to set the code to be executed.
     */
    protected abstract fun execute(parameters: P): Flow<Result<R>>
}