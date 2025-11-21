package com.linhphan.lpcore.domain.usecase

import com.linhphan.lpcore.domain.base.Result
import com.linhphan.lpcore.domain.repository.ForecastRepository
import com.linhphan.lpcore.di.IoDispatcher
import com.linhphan.lpcore.domain.base.BaseUseCase
import com.linhphan.lpcore.domain.model.Forecasts
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class GetForecastUseCase @Inject constructor(
    private val repository: ForecastRepository,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : BaseUseCase<IGetForecastUseCase.Params, Forecasts>(ioDispatcher), IGetForecastUseCase {

    override suspend fun execute(parameters: IGetForecastUseCase.Params): Result<Forecasts> {
        return repository.getForecast(parameters.lat, parameters.lon)
    }
}