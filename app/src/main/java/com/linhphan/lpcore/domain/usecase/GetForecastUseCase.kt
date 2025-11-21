package com.linhphan.lpcore.domain.usecase

import com.linhphan.lpcore.data.Result
import com.linhphan.lpcore.data.forecast.ForecastRepository
import com.linhphan.lpcore.domain.model.Forecast
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetForecastUseCase @Inject constructor(
    private val repository: ForecastRepository
) {
    operator fun invoke(lat: Double, lon: Double): Flow<Result<Forecast>> {
        return repository.getForecast(lat, lon)
    }
}