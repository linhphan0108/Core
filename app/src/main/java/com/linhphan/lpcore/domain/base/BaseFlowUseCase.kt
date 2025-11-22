package com.linhphan.lpcore.domain.base

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn

/**
 * Executes business logic returning a Flow.
 */
abstract class BaseFlowUseCase<in P, R>(private val coroutineDispatcher: CoroutineDispatcher) {

    /**
     * Executes the use case and returns a [Flow].
     */
    operator fun invoke(parameters: P): Flow<Result<R>> {
        return execute(parameters)
            .catch { e -> emit(Result.Error(Exception(e))) }
            .flowOn(coroutineDispatcher)
    }

    protected abstract fun execute(parameters: P): Flow<Result<R>>
}