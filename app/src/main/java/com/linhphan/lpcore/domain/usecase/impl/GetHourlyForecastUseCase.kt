package com.linhphan.lpcore.domain.usecase.impl

import com.linhphan.lpcore.di.IoDispatcher
import com.linhphan.lpcore.domain.base.Result
import com.linhphan.lpcore.domain.base.SuspendUseCase
import com.linhphan.lpcore.domain.model.HourlyForecasts
import com.linhphan.lpcore.domain.repository.ForecastRepository
import com.linhphan.lpcore.domain.usecase.IGetHourlyForecastUseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class GetHourlyForecastUseCase @Inject constructor(
    private val repository: ForecastRepository,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : SuspendUseCase<IGetHourlyForecastUseCase.Params, HourlyForecasts>(ioDispatcher), IGetHourlyForecastUseCase {

    override suspend fun execute(parameters: IGetHourlyForecastUseCase.Params): Result<HourlyForecasts> {
        return repository.getHourlyForecast(
            lat = parameters.lat,
            lon = parameters.lon,
            timezone = parameters.timezone,
            startDate = parameters.startDate,
            endDate = parameters.endDate
        )
    }
}
