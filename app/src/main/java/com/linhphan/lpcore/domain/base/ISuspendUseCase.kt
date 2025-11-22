package com.linhphan.lpcore.domain.base

/**
 * @author Linh Phan
 * @since 2025-11-22
 */
interface ISuspendUseCase<in P, R> {
    suspend operator fun invoke(parameters: P): Result<R>
}