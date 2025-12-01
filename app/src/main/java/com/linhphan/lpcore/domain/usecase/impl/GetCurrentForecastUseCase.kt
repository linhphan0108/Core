package com.linhphan.lpcore.domain.usecase.impl

import com.linhphan.lpcore.di.IoDispatcher
import com.linhphan.lpcore.domain.base.Result
import com.linhphan.lpcore.domain.base.SuspendUseCase
import com.linhphan.lpcore.domain.model.CurrentForecast
import com.linhphan.lpcore.domain.repository.ForecastRepository
import com.linhphan.lpcore.domain.usecase.IGetCurrentForecastUseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class GetCurrentForecastUseCase @Inject constructor(
    @IoDispatcher ioDispatcher: CoroutineDispatcher,
    private val repository: ForecastRepository,
) : SuspendUseCase<IGetCurrentForecastUseCase.Params, CurrentForecast>(ioDispatcher), IGetCurrentForecastUseCase {

    override suspend fun execute(parameters: IGetCurrentForecastUseCase.Params): Result<CurrentForecast> {
        return repository.getCurrentForecast(
            lat = parameters.lat,
            lon = parameters.lon,
            timezone = parameters.timezone,
        )
    }
}
