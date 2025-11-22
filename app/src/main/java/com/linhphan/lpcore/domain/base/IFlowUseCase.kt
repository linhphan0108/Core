package com.linhphan.lpcore.domain.base

import kotlinx.coroutines.flow.Flow

/**
 * @author Linh Phan
 * @since 2025-11-22
 */
interface IFlowUseCase<in P, R> {
    operator fun invoke(parameters: P): Flow<Result<R>>
}