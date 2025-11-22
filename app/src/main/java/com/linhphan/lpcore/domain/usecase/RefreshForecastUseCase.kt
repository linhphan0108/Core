package com.linhphan.lpcore.domain.usecase

import com.linhphan.lpcore.di.IoDispatcher
import com.linhphan.lpcore.domain.base.BaseSuspendUseCase
import com.linhphan.lpcore.domain.base.Result
import com.linhphan.lpcore.domain.repository.ForecastRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class RefreshForecastUseCase @Inject constructor(
    private val repository: ForecastRepository,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : BaseSuspendUseCase<IRefreshForecastUseCase.Params, Unit>(ioDispatcher), IRefreshForecastUseCase {

    override suspend fun invoke(lat: Double, lon: Double): Result<Unit> {
        return super.invoke(IRefreshForecastUseCase.Params(lat, lon))
    }

    override suspend fun execute(parameters: IRefreshForecastUseCase.Params): Result<Unit> {
        return repository.refreshForecast(parameters.lat, parameters.lon)
    }
}