package com.linhphan.lpcore.domain.usecase.impl

import com.linhphan.lpcore.di.IoDispatcher
import com.linhphan.lpcore.domain.base.Result
import com.linhphan.lpcore.domain.base.SuspendUseCase
import com.linhphan.lpcore.domain.model.DailyForecasts
import com.linhphan.lpcore.domain.repository.ForecastRepository
import com.linhphan.lpcore.domain.usecase.IGetDailyForecastUseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class GetDailyForecastUseCase @Inject constructor(
    private val repository: ForecastRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : SuspendUseCase<IGetDailyForecastUseCase.Params, DailyForecasts>(dispatcher), IGetDailyForecastUseCase {
    override suspend fun execute(parameters: IGetDailyForecastUseCase.Params): Result<DailyForecasts> {
        return repository.getDailyForecast(
            parameters.lat,
            parameters.lon,
            parameters.timezone,
            parameters.startDate,
            parameters.endDate
        )
    }
}
