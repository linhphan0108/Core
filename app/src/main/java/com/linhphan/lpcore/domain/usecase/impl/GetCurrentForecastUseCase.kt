package com.linhphan.lpcore.domain.usecase.impl

import com.linhphan.lpcore.data.forecast.ForecastMapper
import com.linhphan.lpcore.domain.base.Result
import com.linhphan.lpcore.domain.model.CurrentForecast
import com.linhphan.lpcore.domain.repository.ForecastRepository
import com.linhphan.lpcore.domain.usecase.IGetCurrentForecastUseCase
import javax.inject.Inject

class GetCurrentForecastUseCase @Inject constructor(
    private val repository: ForecastRepository,
) : IGetCurrentForecastUseCase {
    override suspend fun invoke(params: IGetCurrentForecastUseCase.Params): Result<CurrentForecast> {
        return repository.getCurrentForecast(
            lat = params.lat,
            lon = params.lon,
            timezone = params.timezone,
        )
    }
}
