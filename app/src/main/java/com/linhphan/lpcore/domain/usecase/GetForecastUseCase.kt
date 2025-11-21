package com.linhphan.lpcore.domain.usecase

import com.linhphan.lpcore.data.Result
import com.linhphan.lpcore.data.forecast.ForecastRepository
import com.linhphan.lpcore.di.IoDispatcher
import com.linhphan.lpcore.domain.base.BaseUseCase
import com.linhphan.lpcore.domain.model.Forecasts
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetForecastUseCase @Inject constructor(
    private val repository: ForecastRepository,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : BaseUseCase<IGetForecastUseCase.Params, Forecasts>(ioDispatcher), IGetForecastUseCase {

    override fun execute(parameters: IGetForecastUseCase.Params): Flow<Result<Forecasts>> {
        return repository.getForecast(parameters.lat, parameters.lon)
    }
}