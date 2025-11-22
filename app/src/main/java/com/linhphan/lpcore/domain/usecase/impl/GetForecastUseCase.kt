package com.linhphan.lpcore.domain.usecase.impl

import com.linhphan.lpcore.di.IoDispatcher
import com.linhphan.lpcore.domain.base.FlowUseCase
import com.linhphan.lpcore.domain.base.Result
import com.linhphan.lpcore.domain.model.Forecasts
import com.linhphan.lpcore.domain.repository.ForecastRepository
import com.linhphan.lpcore.domain.usecase.IGetForecastUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetForecastUseCase @Inject constructor(
    private val repository: ForecastRepository,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : FlowUseCase<IGetForecastUseCase.Params, Forecasts>(ioDispatcher), IGetForecastUseCase {

    override fun execute(parameters: IGetForecastUseCase.Params): Flow<Result<Forecasts>> {
        return repository.getForecast(parameters.lat, parameters.lon)
    }
}